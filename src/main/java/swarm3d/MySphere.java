package swarm3d;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class MySphere extends Sphere {

	public MySphere(float x, float y, float z, float[] color) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
	}



	private float x,y,z;
	private float[] color;



	public void display() {

		GL11.glPushMatrix();
		{

			GL11.glTranslatef(x, y, z);
			GL11.glColor3f(color[0], color[1], color[2]);
			draw(10F, 15, 15);

		}
		GL11.glPopMatrix();
	}


}
