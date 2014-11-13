package swarm3d;

import org.lwjgl.opengl.GL11;

public class Rotation {

	float w,x,y,z;

	public Rotation(float w, float x, float y, float z) {
		super();
		this.w = w;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public String toString() {
		return "Rotation [w=" + w + ", x=" + x + ", y=" + y + ", z=" + z + "]";
	}
	
	public void bind() {
		GL11.glRotatef(w, x, y, z);		
	}
	
}
