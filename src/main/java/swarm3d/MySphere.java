package swarm3d;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class MySphere extends Sphere implements Displayable {

	private Position position;
	private Color color;
	private float radius=10F;
	FloatBuffer transformationBuffer;

	public MySphere(Position position, Color color) {
		this.position = position;
		this.color = color;
		System.out.println(position.toString());
	}

	@Override
	public void display() {

		GL11.glPushMatrix();
		{
//			textureFlag = true;
			if(transformationBuffer == null) {
				position.bindPosition();
			} else {
				GL11.glMultMatrix(transformationBuffer);
			}
			color.bind();
			draw(radius, 15, 15);
		}
		GL11.glPopMatrix();
	}

	public float getRadius() {
		return radius;
	}

	public Position getPosition() {
		return position;
	}
	public void putTransformMatrix(float[] matrix) {
		transformationBuffer.clear();
		transformationBuffer.put(matrix);
		transformationBuffer.flip();
	}

	public void activatePhysics() {
		transformationBuffer = BufferUtils.createFloatBuffer(32);
	}
}
