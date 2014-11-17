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
import com.bulletphysics.collision.shapes.CylinderShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.linearmath.Transform;

public class Orb implements Displayable {

	List<Magnet> magnets = new ArrayList<>();
	CompoundShape shape;
	private float	radius;
	FloatBuffer transformationBuffer = BufferUtils.createFloatBuffer(32);
	private Position	position;
	private Transform transform;


	public Orb(float radius, Position position) {

		shape = new CompoundShape();

		SphereShape sphereShape = new SphereShape(radius);
		transform = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), position.toVector(), 1));
		shape.addChildShape(transform, sphereShape);

		addMagnets(radius);
		this.radius = radius;
		this.position = position;
	}

	private void addMagnet(Vector3f position, Color color) {

		float radius = 1f;
		float height = 0.1f;

		Magnet m = new Magnet(position, new CylinderShape(new Vector3f(radius, 0, height)), color);
		magnets.add(m);

		if(Color.RED == color)  {
			m.setRw(0);
			m.setRx(3.2f);
			m.setRy(5.5f);
			m.setRz(0.7f);

		}

		Transform t = new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), position, 1));

		shape.addChildShape(t, m.shape);
	}

	private void addMagnet(Vector3f position) {
		addMagnet(position, Color.GREEN);
	}

	private void addMagnets(float r) {

		r /= 2.1f;

		addMagnet(new Vector3f(-r*2, 0, 0));
		addMagnet(new Vector3f(r*2, 0, 0));

		addMagnet(new Vector3f(r, (float)Math.sqrt(3)*r, 0));
		addMagnet(new Vector3f(-r, (float)Math.sqrt(3)*r, 0));

		addMagnet(new Vector3f(r, -(float)Math.sqrt(3)*r, 0));
		addMagnet(new Vector3f(-r, -(float)Math.sqrt(3)*r, 0));

		addMagnet(new Vector3f(-r, (float)Math.sqrt(3)*r / 3, (float)Math.sqrt(6)*r*2 / 3));
		addMagnet(new Vector3f(r, (float)Math.sqrt(3)*r / 3, (float)Math.sqrt(6)*r*2 / 3));

		addMagnet(new Vector3f(0, -(2*(float)Math.sqrt(3)*r / 3), (float)Math.sqrt(6)*r*2 / 3));
		addMagnet(new Vector3f(0, (2*(float)Math.sqrt(3)*r / 3), -(float)Math.sqrt(6)*r*2 / 3));

		addMagnet(new Vector3f(-r, -(float)Math.sqrt(3)*r / 3, -(float)Math.sqrt(6)*r*2 / 3));
		addMagnet(new Vector3f(r, -(float)Math.sqrt(3)*r / 3, -(float)Math.sqrt(6)*r*2 / 3), Color.RED);

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

	public void putTransformMatrix(float[] matrix) {
		transformationBuffer.clear();
		transformationBuffer.put(matrix);
		transformationBuffer.flip();

	}

	public List<Magnet> getMagnets() {
		return magnets;
	}

}
