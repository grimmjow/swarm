package swarm3d;

import org.lwjgl.opengl.GL11;

public class Line implements Displayable {

	private Color color;
	private Position begin;
	private Position end;
	
	public Line(Position begin, Position end, Color color) {
		this.begin = begin;
		this.end = end;
		this.color = color;
	}

	public Line(Position end, Color color) {
		this(new Position(0, 0, 0), end, color);
	}


	public void display() {

		GL11.glPushMatrix();
		{

			GL11.glLineWidth(1.0F);
			color.bind();
			GL11.glBegin(GL11.GL_LINES);
			{
				begin.bindVertex();
				end.bindVertex();
			}
			GL11.glEnd();

		}
		GL11.glPopMatrix();
	}

}
