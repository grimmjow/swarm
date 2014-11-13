package swarm3d;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class MySphere extends Sphere implements Displayable {

	private Position position;
	private Color color;

	public MySphere(Position position, Color color) {
		this.position = position;
		this.color = color;
	}

	@Override
	public void display() {

		GL11.glPushMatrix();
		{
//			textureFlag = true;
			position.bindPosition();
			color.bind();
			draw(10F, 25, 25);
		}
		GL11.glPopMatrix();
	}


}
