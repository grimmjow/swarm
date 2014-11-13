package swarm3d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.Sys;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.CollisionObject;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.CollisionShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.BoxShape;
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
	Map<RigidBody, Displayable> things = new HashMap<>();
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
	    dynamicsWorld.setGravity(new Vector3f(0, -10, 0));

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
		        things.put(body, box);
		        box.activatePhysics();
	    	}
	    	if(displayable instanceof MySphere) {
	    		MySphere mySphere = (MySphere) displayable;
			    RigidBody body = createRigidBodyFromBox(mySphere);
		        dynamicsWorld.addRigidBody(body);
		        things.put(body, mySphere);
		        mySphere.activatePhysics();
	    	}	        
	    }
		
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


	private RigidBody createRigidBodyFromBox(MySphere sphere) {
		
	    CollisionShape fallShape = new SphereShape(sphere.getRadius());
	    DefaultMotionState fallMotionState = new DefaultMotionState(new Transform(
	    		new Matrix4f(new Quat4f(0.0f, 0.0f, 0.0f, 1f), 
	    				new Vector3f(sphere.getPosition().x, sphere.getPosition().y, sphere.getPosition().z), 1)));
	    Vector3f fallInertia = new Vector3f(0, 0, 0);
        fallShape.calculateLocalInertia(1F, fallInertia);

        RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(0.1f, fallMotionState, fallShape, fallInertia);
        fallRigidBodyCI.restitution = 0.5f;
        fallRigidBodyCI.angularDamping = 0.95f;
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

            for(Map.Entry<RigidBody, Displayable> entry: things.entrySet()) {
            	Displayable displayable = entry.getValue();
	            float[] matrix = new float[16];
	            entry.getKey().getMotionState().getWorldTransform(new Transform()).getOpenGLMatrix(matrix);
	            if(displayable instanceof Box) {
	            	((Box) displayable).putTransformMatrix(matrix);
	            }
	            if(displayable instanceof MySphere) {
	            	((MySphere) displayable).putTransformMatrix(matrix);
	            }	            
            }
            long currentTimeMillis2 = System.currentTimeMillis();
            try {
            	long sleepTime = (syncTime - (currentTimeMillis2 - currentTimeMillis));
				Thread.sleep(sleepTime >= 0 ? sleepTime : 0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            
        }
	}
	
	public RigidBody getFallRigidBody() {
		return fallRigidBody;
	}
	
	public void issueStop() {
		stopIssued = true;
	}
	
}
