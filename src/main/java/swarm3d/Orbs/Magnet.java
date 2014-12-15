package swarm3d.Orbs;

import java.nio.FloatBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

import swarm3d.Color;
import swarm3d.Displayable;

import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.linearmath.QuaternionUtil;
import com.bulletphysics.linearmath.Transform;

public class Magnet implements Displayable {

	public BlockingQueue<String> dataInput = new ArrayBlockingQueue<String>(10);
	public BlockingQueue<String> dataOutput = new ArrayBlockingQueue<String>(10);
	public boolean active = false;
	public SphereShape shape;
	FloatBuffer transformationBuffer = BufferUtils.createFloatBuffer(32);
	private Vector3f	position;
	private Color color;

	private float force;
	private float range;


	public Magnet(Vector3f position, SphereShape sphereShape, Color color, float force, float range) {
		shape = sphereShape;
		this.position = position;
		this.color = color;
		this.force = force;
		this.range = range;
	}

	@Override
	public void display() {

		GL11.glPushMatrix();
		{

			color.bind();
			GL11.glTranslatef(position.x, position.y, position.z);
			new Sphere().draw(shape.getRadius(), 5, 5);
		}
		GL11.glPopMatrix();

	}

	public Vector3f getPosition() {
		return position;
	}

	public Vector3f getAbsolutePosition(Transform parentTransform) {
		Transform t = new Transform(parentTransform);
		Vector3f newPosition = QuaternionUtil.quatRotate(t.getRotation(new Quat4f()), position, new Vector3f());

		newPosition.x += parentTransform.origin.x;
		newPosition.y += parentTransform.origin.y;
		newPosition.z += parentTransform.origin.z;

		return newPosition;
	}

	public float getForce() {
		return force;
	}

	public float getRange() {
		return range;
	}
}

