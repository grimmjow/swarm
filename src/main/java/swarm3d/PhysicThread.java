package swarm3d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import swarm3d.Orbs.Magnet;
import swarm3d.Orbs.Orb;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;

public class PhysicThread extends Thread {

	DiscreteDynamicsWorld dynamicsWorld;
	RigidBody fallRigidBody;
	Map<Displayable, RigidBody> things = new HashMap<>();
	List<Orb> orbse = new ArrayList<>();
	boolean stopIssued = false;

	public PhysicThread(List<Displayable> displayables) {

		// Build the broadphase
		BroadphaseInterface broadphase = new DbvtBroadphase();

	    // Set up the collision configuration and dispatcher
		DefaultCollisionConfiguration collisionConfiguration = new DefaultCollisionConfiguration();
		CollisionDispatcher dispatcher = new CollisionDispatcher(collisionConfiguration);

	    // The actual physics solver
		SequentialImpulseConstraintSolver solver = new SequentialImpulseConstraintSolver();

	    // The world.
	    dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);
	    dynamicsWorld.setGravity(new Vector3f(0, 0, 0));

	    // adding the ground
	    CollisionShape groundShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 1f);
	    DefaultMotionState groundMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, -1f, 0), 1)));
	    RigidBodyConstructionInfo groundRigidBodyCI = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new Vector3f(0, 0, 0));
	    groundRigidBodyCI.restitution = 0.25f;
	    RigidBody groundRigidBody = new RigidBody(groundRigidBodyCI);
	    dynamicsWorld.addRigidBody(groundRigidBody);

	    for(Displayable displayable : displayables) {

	    	if(displayable instanceof Box) {
	    		Box box = (Box) displayable;
			    RigidBody body = createRigidBodyFromBox(box);
		        dynamicsWorld.addRigidBody(body);
		        things.put(box, body);
		        box.activatePhysics();
	    	}

	    	if(displayable instanceof Orb) {
	    		Orb orb = (Orb) displayable;
			    RigidBody body = createRigidBodyFromOrb(orb);
		        dynamicsWorld.addRigidBody(body);
		        things.put(orb, body);
		        orbse.add(orb);
	    	}
	    }

	}

	private RigidBody createRigidBodyFromOrb(Orb orb) {

	    DefaultMotionState fallMotionState = new DefaultMotionState(orb.getTransform());
	    Vector3f fallInertia = new Vector3f(0, 0, 0);
        orb.getShape().calculateLocalInertia(orb.getMass(), fallInertia);

        RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(orb.getMass(), fallMotionState, orb.getShape(), fallInertia);
        fallRigidBodyCI.restitution = 0.5f;
        fallRigidBodyCI.angularDamping = 0.95f;
        fallRigidBody = new RigidBody(fallRigidBodyCI);
        fallRigidBody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
        return fallRigidBody;
	}

	private RigidBody createRigidBodyFromBox(Box box) {

	    CollisionShape fallShape = new BoxShape(new Vector3f(box.getWidth()/2, box.getHeight()/2, box.getDepth()/2));
	    DefaultMotionState fallMotionState = new DefaultMotionState(new Transform(
	    		new Matrix4f(new Quat4f(box.getRw(), box.getRx(), box.getRy(), box.getRz()),
	    				new Vector3f(box.getX(), box.getY(), box.getZ()), 1)));
	    Vector3f fallInertia = new Vector3f(0, 0, 0);
        fallShape.calculateLocalInertia(1F, fallInertia);

        RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(0.1f, fallMotionState, fallShape, fallInertia);
        fallRigidBodyCI.restitution = 0.9f;
        fallRigidBodyCI.friction = 0.95f;
        fallRigidBody = new RigidBody(fallRigidBodyCI);

//        fallRigidBody.setActivationState(CollisionObject.DISABLE_DEACTIVATION);
        return fallRigidBody;
	}

	@Override
	public void run() {

		long syncTime = (long) ((1F/60F)*1000L);
        while (!stopIssued) {

			long currentTimeMillis = System.currentTimeMillis();
            dynamicsWorld.stepSimulation(1 / 60.f, 100);

            for(Map.Entry<Displayable, RigidBody> entry: things.entrySet()) {
            	Displayable displayable = entry.getKey();
	            float[] matrix = new float[16];
	            Transform transform = entry.getValue().getMotionState().getWorldTransform(new Transform());
	            transform.getOpenGLMatrix(matrix);
	            if(displayable instanceof Box) {
	            	((Box) displayable).putTransformMatrix(matrix);
	            }
	            if(displayable instanceof Orb) {
	            	Orb orb = (Orb) displayable;
	            	orb.putTransformMatrix(matrix);
	            	orb.setTransform(transform);
	            }
            }

            for(int iter1 = 0; iter1 < orbse.size(); iter1++) {
            	for (int iter2 = iter1+1; iter2 < orbse.size(); iter2++) {
            		Orb orb1 = orbse.get(iter1);
            		Orb orb2 = orbse.get(iter2);

            		Magnet mag1 = getCloseMagnet(orb1, orb2);
            		Magnet mag2 = getCloseMagnet(orb2, orb1);

            		float magnetForce = mag1.getForce() + mag2.getForce();
            		float magnetRange = mag1.getRange() + mag2.getRange();

            		Vector3f force1 = new Vector3f(mag1.getAbsolutePosition(orb1.getTransform()));
            		force1.sub(mag2.getAbsolutePosition(orb2.getTransform()));

            		if (magnetRange <= getDistance(new Vector3f(), force1)) {
            			continue;
            		}

            		Vector3f force2 = new Vector3f(mag2.getAbsolutePosition(orb2.getTransform()));
            		force2.sub(mag1.getAbsolutePosition(orb1.getTransform()));

            		force1.scale(calcForce(magnetForce, magnetRange, force1));
            		things.get(orb1).applyForce(force1, mag1.getPosition());

            		force2.scale(calcForce(magnetForce, magnetRange, force2));
               		things.get(orb2).applyForce(force2, mag2.getPosition());
            	}
            }

            long currentTimeMillis2 = System.currentTimeMillis();
            try {
            	long sleepTime = (syncTime - (currentTimeMillis2 - currentTimeMillis));
//            	System.out.println("sleep: " + sleepTime);
				Thread.sleep(sleepTime >= 0 ? sleepTime : 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

        }
	}

	private float calcForce(float magnetForce, float magnetRange, Vector3f force) {
		float f = magnetRange / (getDistance(new Vector3f(), force)); // normalize
		f = f * (magnetRange - getDistance(new Vector3f(), force)) / magnetRange; //invert
		f = f * (magnetForce / magnetRange); // scale to force
		return f * -1;
	}

	private Magnet getCloseMagnet(Orb orb1, Orb orb2) {
		Magnet best = null;
		float bestDistance = Float.MAX_VALUE;

		for (Magnet mag : orb1.getMagnets()) {
			float dist = getDistance(mag.getAbsolutePosition(orb1.getTransform()), orb2.getTransform().origin);
			if (dist < bestDistance) {
				bestDistance = dist;
				best = mag;
			}
		}

		if(best == null) {
			throw new RuntimeException("null panic");
		}

		return best;
	}

	private float getDistance(Vector3f origin, Vector3f origin2) {
		float x = origin.x - origin2.x;
		float y = origin.y - origin2.y;
		float z = origin.z - origin2.z;
		return (float) Math.sqrt(Math.pow(Math.sqrt(x*x+y*y), 2) + z*z);
	}

	public RigidBody getFallRigidBody() {
		return fallRigidBody;
	}

	public void issueStop() {
		stopIssued = true;
	}

}
