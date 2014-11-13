package swarm3d;

import org.lwjgl.opengl.GL11;

public class Position {

	public float x,y,z;

	public Position(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void bindVertex() {
		GL11.glVertex3f(x, y, z);		
	}

	public void bindPosition() {
		GL11.glTranslatef(x, y, z);		
	}
}
