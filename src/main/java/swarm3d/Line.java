package swarm3d;

import org.lwjgl.opengl.GL11;

public class Line {

	public Line(float x1, float y1, float z1, float x2, float y2, float z2) {
		super();
		this.x1 = x1;
		this.y1 = y1;
		this.z1 = z1;
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
	}


	private float x1=0,y1=0,z1=0;
	private float x2,y2,z2;
	public Line(float x2, float y2, float z2) {
		this.x2 = x2;
		this.y2 = y2;
		this.z2 = z2;
	}


	public void display() {

		GL11.glPushMatrix();
		{

			GL11.glLineWidth(1.0F);
			GL11.glColor3f(0F, 1F, 1F);
			GL11.glBegin(GL11.GL_LINES);
			{
				GL11.glVertex3f(x1, y1, z1);
				GL11.glVertex3f(x2, y2, z2);
			}
			GL11.glEnd();

		}
		GL11.glPopMatrix();
	}

}
