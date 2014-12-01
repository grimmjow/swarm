package swarm3d;

import javax.vecmath.Vector3f;

import org.lwjgl.opengl.GL11;

public class Position {

	public float x,y,z;

	public Position(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Position(Vector3f origin) {
		this(origin.x, origin.y, origin.z);
	}

	public void bindVertex() {
		GL11.glVertex3f(x, y, z);
	}

	public void bindPosition() {
		GL11.glTranslatef(x, y, z);
	}

	@Override
	public String toString() {
		return "Position [x=" + x + ", y=" + y + ", z=" + z + "] Distance to 0: " + Math.sqrt(Math.pow(Math.sqrt(x*x+y*y), 2) + z*z);
	}

	public Vector3f toVector() {
		return new Vector3f(x,y,z);
	}
}
