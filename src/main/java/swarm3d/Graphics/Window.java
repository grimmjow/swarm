package swarm3d.Graphics;

import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Window {

	public Window() throws LWJGLException {
		Display.setDisplayMode(new DisplayMode(640, 480));
		Display.setTitle("The Orbses world");
		Display.create();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(-320, 320, -240, 240, 1, -1);
		glMatrixMode(GL_MODELVIEW);


	}

	public void update() {
		Display.update();
		Display.sync(60);
	}

	public boolean isCloseRequested() {

		return Display.isCloseRequested();

	}

	public void close() {
		Display.destroy();
	}

}
