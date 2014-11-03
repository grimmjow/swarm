package swarm3d;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glEnd;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.GL11;

import swarm3d.Graphics.Window;
import swarm3d.Physics.World;

public class Main {

	public static void main(String[] args) {

		World world = new World();
		Window window=null;
		try {
			window = new Window();
		} catch (LWJGLException e1) {
			e1.printStackTrace();
			System.exit(1);
		}

		while(!window.isCloseRequested()) {

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

			world.step();
			Vector3f sphere = world.getSphere();

			glBegin(GL11.GL_LINES);
				GL11.glVertex2i(-320, 0);
				GL11.glVertex2i(320, 0);
			glEnd();

			glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(10F, sphere.y*2);
				GL11.glVertex2f(20F, sphere.y*2);
				GL11.glVertex2f(20F, sphere.y*2 + 10);
				GL11.glVertex2f(10F, sphere.y*2 + 10);
			glEnd();
			window.update();
		}

	}

}
