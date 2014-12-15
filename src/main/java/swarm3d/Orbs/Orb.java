package swarm3d.Orbs;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

import swarm3d.Color;
import swarm3d.Displayable;
import swarm3d.Position;

import com.bulletphysics.collision.shapes.CompoundShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.linearmath.Transform;

public class Orb implements Displayable {

	List<Magnet> magnets = new ArrayList<>();
	CompoundShape shape;
	private float radius;

	FloatBuffer transformationBuffer = BufferUtils.createFloatBuffer(32);
	private Transform transform;
	private float mass;


	public Orb(float radius, float mass, Position position, float magnetRange, float magnetForce) {
		this.radius = radius;
		this.mass = mass;

		shape = new CompoundShape();
		this.transform = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), position.toVector(), 1));

		SphereShape sphereShape = new SphereShape(radius);
		Transform transform = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, 0, 0), 1));
		shape.addChildShape(transform, sphereShape);

		addMagnets(position.toVector(), radius, magnetRange, magnetForce);

		Vector3f center = new Vector3f();
		float[] myRadius = new float[5];
		shape.getBoundingSphere(center, myRadius);
		System.out.println("center: " + center + " radius: " + myRadius);
		System.out.println(transform.origin);
	}

	private void addMagnet(Vector3f position, Color color, float magnetRange, float magnetForce) {

		float radius = this.radius / 20;

		Magnet m = new Magnet(position, new SphereShape(radius), color, magnetForce, magnetRange);
		magnets.add(m);

		Transform t = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), position, 1));

		shape.addChildShape(t, m.shape);
	}

	private void addMagnet(Vector3f position, float magnetRange, float magnetForce) {
		addMagnet(position, Color.GREEN, magnetRange, magnetForce);
	}

	private void addMagnets(Vector3f rootPosition, float r, float magnetRange, float magnetForce) {

		r /= 2.2f;

		addMagnet(new Vector3f(-r*2, 0, 0), magnetRange, magnetForce);
		addMagnet(new Vector3f(r*2, 0, 0), magnetRange, magnetForce);

		addMagnet(new Vector3f(r, (float)Math.sqrt(3)*r, 0), magnetRange, magnetForce);
		addMagnet(new Vector3f(-r, (float)Math.sqrt(3)*r, 0), magnetRange, magnetForce);

		addMagnet(new Vector3f(r, -(float)Math.sqrt(3)*r, 0), magnetRange, magnetForce);
		addMagnet(new Vector3f(-r, -(float)Math.sqrt(3)*r, 0), magnetRange, magnetForce);

		addMagnet(new Vector3f(-r, (float)Math.sqrt(3)*r / 3, (float)Math.sqrt(6)*r*2 / 3), magnetRange, magnetForce);
		addMagnet(new Vector3f(r, (float)Math.sqrt(3)*r / 3, (float)Math.sqrt(6)*r*2 / 3), magnetRange, magnetForce);

		addMagnet(new Vector3f(0, -(2*(float)Math.sqrt(3)*r / 3), (float)Math.sqrt(6)*r*2 / 3), magnetRange, magnetForce);
		addMagnet(new Vector3f(0, (2*(float)Math.sqrt(3)*r / 3), -(float)Math.sqrt(6)*r*2 / 3), magnetRange, magnetForce);

		addMagnet(new Vector3f(-r, -(float)Math.sqrt(3)*r / 3, -(float)Math.sqrt(6)*r*2 / 3), magnetRange, magnetForce);
		addMagnet(new Vector3f(r, -(float)Math.sqrt(3)*r / 3, -(float)Math.sqrt(6)*r*2 / 3), magnetRange, magnetForce);

	}

	public CompoundShape getShape() {
		return shape;
	}

	@Override
	public void display() {

		GL11.glPushMatrix();
		{

			GL11.glMultMatrix(transformationBuffer);

			new Sphere().draw(radius, 15, 15);
			for(Magnet magnet : magnets) {
				magnet.display();
			}
		}
		GL11.glPopMatrix();
	}

	public Transform getTransform() {
		return transform;
	}

	@Override
	public String toString() {
		String out = "Orb " + getPosition()
			+ "\nMagnets\n";

		for(Magnet magnet : magnets) {
			out += new Position(magnet.getAbsolutePosition(transform)) + "\n";
		}

		return out;
	}

	public void putTransformMatrix(float[] matrix) {
		transformationBuffer.clear();
		transformationBuffer.put(matrix);
		transformationBuffer.flip();

	}

	public List<Magnet> getMagnets() {
		return magnets;
	}

	public Position getPosition() {
		return new Position(transform.origin);
	}

	public void setTransform(Transform transform2) {
		this.transform = transform2;
	}

	public float getRadius() {
		return radius;
	}

	public float getMass() {
		return mass;
	}

}
