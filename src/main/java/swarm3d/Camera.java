package swarm3d;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;


public class Camera {

	private float x=0, y=0, z=0, rx=0, ry=0, rz=0;
	private float fov, aspect, near, far;

	public Camera(float fov, float aspect, float near, float far) {
		this.fov = fov;
		this.aspect = aspect;
		this.near = near;
		this.far = far;

		initProjection();
	}

	private void initProjection() {

		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GLU.gluPerspective(fov, aspect, near, far);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

	}

	public void updateView() {

		GL11.glRotatef(rx, 1, 0, 0);
		GL11.glRotatef(ry, 0, 1, 0);
		GL11.glRotatef(rz, 0, 0, 1);
		GL11.glTranslatef(x, y, z);

	}

	public void move(float amount, int direction) {

		synchronized (this) {
			z += (float) (amount * Math.sin(Math.toRadians(ry + 90 * direction)));
			x += (float) (amount * Math.cos(Math.toRadians(ry + 90 * direction)));
			if(direction == 1)
				y += (float) (amount * Math.sin(Math.toRadians(rx)));
		}
	}


	public void rotateY(float amount) {
		ry += amount;
	}

	public void rotateX(float amount) {
		rx += amount;
	}

	public void moveY(float amount) {
		y += amount;
	}


	public void moveZ(float amount) {
		z += amount;
	}
}
