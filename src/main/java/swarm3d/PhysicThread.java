package swarm3d;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
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
	Map<RigidBody, Box> things = new HashMap<>();
	boolean stopIssued = false;
	
	public PhysicThread(List<Box> boxes) {

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
	    DefaultMotionState groundMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 0, 0), 1)));
	    RigidBodyConstructionInfo groundRigidBodyCI = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new Vector3f(0, 0, 0));
	    groundRigidBodyCI.restitution = 0.9f;
	    RigidBody groundRigidBody = new RigidBody(groundRigidBodyCI);
	    dynamicsWorld.addRigidBody(groundRigidBody);

	    for(Box box : boxes) {
	    
		    RigidBody body = createRigidBodyFromBox(box);
	        dynamicsWorld.addRigidBody(body);
	        things.put(body, box);
	        
	    }
		
	}

	private RigidBody createRigidBodyFromBox(Box box) {
		
	    CollisionShape fallShape = new BoxShape(new Vector3f(box.getWidth()/2, box.getHeight()/2, box.getDepth()/2));
	    DefaultMotionState fallMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0.5f, 0.5f, 0.5f, 1f), new Vector3f(box.getX(), box.getY(), box.getZ()), 1)));
	    Vector3f fallInertia = new Vector3f(0, 0, 0);
        fallShape.calculateLocalInertia(1F, fallInertia);

        RigidBodyConstructionInfo fallRigidBodyCI = new RigidBodyConstructionInfo(0.1f, fallMotionState, fallShape, fallInertia);
        fallRigidBodyCI.restitution = 0.8f;
        fallRigidBody = new RigidBody(fallRigidBodyCI);
        return fallRigidBody;
	}

	@Override
	public void run() {
		
//		long syncTime = (long) ((1F/60F)*1000L);
//		System.out.println("syncTime: " + syncTime);
		Float lastY = null;
        while (!stopIssued) {

//			long currentTimeMillis = System.currentTimeMillis();
            dynamicsWorld.stepSimulation(1 / 60.f, 100);

            for(Map.Entry<RigidBody, Box> entry: things.entrySet()) {
	            Transform worldTransform = entry.getKey().getMotionState().getWorldTransform(new Transform());
	            Vector3f origin = worldTransform.origin;
	            Box box = entry.getValue();
	            box.setY(origin.y + box.getHeight()/2 + 1f);
	            box.setX(origin.x + box.getWidth()/2 + 1f);
	            box.setZ(origin.z + box.getDepth()/2 + 1f);
	            Quat4f rotation = worldTransform.getRotation(new Quat4f());
	            box.setRx(rotation.x);
	            box.setRy(rotation.y);
	            box.setRz(rotation.z);
	            box.setRw((float) Math.toDegrees(rotation.w));
//	            System.out.println(rotation.w);
            }
//            System.out.println(box.getY());
//            if(lastY == null) {
//            	lastY = box.getY();
//            } else {
//            	if((lastY - box.getY()) == 0F) {
            		// A) directly reposition rigid
//                	box.setY(10F);
//                	fallRigidBody.activate();
//                	fallRigidBody.translate(new Vector3f(box.getX(), box.getY(), box.getZ()));
            		// B) apply force
//                	fallRigidBody.applyCentralForce(new Vector3f(box.getX(), box.getY(), box.getZ()));
//            	} else {
//            		lastY = box.getY();
//            	}
//            }
//            long currentTimeMillis2 = System.currentTimeMillis();
//            try {
////            	System.out.println("sleep: " + (currentTimeMillis2 - currentTimeMillis - syncTime));
////            	long sleepTime = (currentTimeMillis2 - currentTimeMillis - syncTime);
////            	System.out.println("sleepTime: " + sleepTime);
////				Thread.sleep(sleepTime >= 0 ? sleepTime : 0);
////				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
            
        }
	}
	
	public RigidBody getFallRigidBody() {
		return fallRigidBody;
	}
	
	public void issueStop() {
		stopIssued = true;
	}
	
}
