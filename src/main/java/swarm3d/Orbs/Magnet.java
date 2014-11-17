package swarm3d.Orbs;

import java.nio.FloatBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.vecmath.Vector3f;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import swarm3d.Color;
import swarm3d.Displayable;

import com.bulletphysics.collision.shapes.CylinderShape;

public class Magnet implements Displayable {

	public BlockingQueue<String> dataInput = new ArrayBlockingQueue<String>(10);
	public BlockingQueue<String> dataOutput = new ArrayBlockingQueue<String>(10);
	public boolean active = false;
	public CylinderShape shape;
	FloatBuffer transformationBuffer = BufferUtils.createFloatBuffer(32);
	private Vector3f	position;
	private float	rw;
	private float	rx;
	private float	ry;
	private float	rz;
	private Color color;


	public Magnet(Vector3f position, CylinderShape cylinderShape, Color color) {
		shape = cylinderShape;
		this.position = position;
		this.color = color;
		System.out.println("x: "+position.x+ ", y: "+ position.y+", z: "+ position.z);
	}

	@Override
	public void display() {

		GL11.glPushMatrix();
		{

			color.bind();
//			GL11.glRotatef(rw, rx, 0.0f, 0.0f);
//		    GL11.glRotatef(rw, 0.0f, ry, 0.0f);
//		    GL11.glRotatef(rw, 0.0f, 0.0f, rz);
			GL11.glRotatef(rw, rx, ry, rz);
			GL11.glTranslatef(position.x, position.y, position.z);
			new Cylinder().draw(shape.getRadius(), shape.getRadius(), 0.1f, 10, 1);
		}
		GL11.glPopMatrix();

	}

	public float getRw() {
		return rw;
	}

	public void setRw(float rw) {
		System.out.println(rw);
		this.rw = rw;
	}

	public float getRx() {
		return rx;
	}

	public void setRx(float rx) {
		this.rx = rx;
	}

	public float getRy() {
		return ry;
	}

	public void setRy(float ry) {
		this.ry = ry;
	}

	public float getRz() {
		return rz;
	}

	public void setRz(float rz) {
		this.rz = rz;
	};
	public Color getColor() {
		return color;
	}
}
