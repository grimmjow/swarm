package swarm3d;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class MainDisplay {

	Camera camera;

	List<Displayable> displayables = new ArrayList<>();
	Texture texture;

	public MainDisplay() throws LWJGLException, FileNotFoundException, IOException {

		float r = 10;
		float d = r*2;

//		spheres.add(new MySphere(0, 0, 0, new float[] {0.5f, 0.5f, 0.5f}));
//
//		lines.add(new Line(d,0,0));
//		lines.add(new Line(-d,0,0));
//		lines.add(new Line((float) Math.sin(Math.toRadians(60))*d,0,-(float) Math.cos(Math.toRadians(60)*d)));
//		lines.add(new Line((float) Math.sin(Math.toRadians(120))*d,0,-(float) Math.cos(Math.toRadians(120)*d)));

//		lines.add(new Line(-d,-d,-d));
//		lines.add(new Line(-d,d,d));
//		lines.add(new Line(d,-d,d));
//		lines.add(new Line(d,d,-d));

//		spheres.add(new MySphere(r*2, r, 0, new float[] {0.8f, 0.8f, 0f}));
//		spheres.add(new MySphere(-(r*2), r, 0, new float[] {0.7f, 0.7f, 0f}));
//
//		spheres.add(new MySphere(r, r, (float)(2*r*Math.sin(Math.toRadians(60))), new float[] {0f, 0.8f, 0.8f}));
//		spheres.add(new MySphere(r, r, -(float)(2*r*Math.sin(Math.toRadians(60))), new float[] {0f, 0.7f, 0.7f}));
//
//		spheres.add(new MySphere(-r, r, (float)(2*r*Math.sin(Math.toRadians(60))), new float[] {0f, 0.8f, 0.8f}));
//		spheres.add(new MySphere(-r, r, -(float)(2*r*Math.sin(Math.toRadians(60))), new float[] {0f, 0.7f, 0.7f}));
//
//		spheres.add(new MySphere(r, r*2+(r*2/6*(float)Math.sqrt(3)), r*2/6*(float)Math.sqrt(3), new float[] {0.7f, 0f, 0.7f}));

//		lines.add(new Line(2*r, r, r, r, r + (float)Math.sqrt(3)*r, r));

		// level 1
		// row 1
		displayables.add(new MySphere(new Position(2*r, 0, 0), new Color(0.8f, 0.8f, 0f)));
		displayables.add(new MySphere(new Position(4*r, 0, 0), new Color(0.7f, 0.7f, 0f)));
		displayables.add(new MySphere(new Position(6*r, 0, 0), new Color(0.7f, 0.7f, 0f)));

		// row 2
		displayables.add(new MySphere(new Position(r, (float)Math.sqrt(3)*r, 0),   new Color(0.7f, 0.7f, 0.0f)));
		displayables.add(new MySphere(new Position(3*r, (float)Math.sqrt(3)*r, 0), new Color(0.7f, 0.7f, 0.0f)));
		displayables.add(new MySphere(new Position(5*r, (float)Math.sqrt(3)*r, 0), new Color(0.7f, 0.7f, 0.0f)));

		// row 3
		displayables.add(new MySphere(new Position(r,   -(float)Math.sqrt(3)*r, 0),   new Color(0.7f, 0.7f, 0f)));
		displayables.add(new MySphere(new Position(3*r, -(float)Math.sqrt(3)*r, 0), new Color(0.7f, 0.7f, 0f)));
		displayables.add(new MySphere(new Position(5*r, -(float)Math.sqrt(3)*r, 0), new Color(0.7f, 0.7f, 0f)));

		// level 2
		// row 1
		displayables.add(new MySphere(new Position(r,   (float)Math.sqrt(3)*r / 3, (float)Math.sqrt(6)*r*2 / 3), new Color(0.0f, 0f, 0.9f)));
		displayables.add(new MySphere(new Position(3*r, (float)Math.sqrt(3)*r / 3, (float)Math.sqrt(6)*r*2 / 3), new Color(0.0f, 0f, 0.9f)));
		displayables.add(new MySphere(new Position(5*r, (float)Math.sqrt(3)*r / 3, (float)Math.sqrt(6)*r*2 / 3), new Color(0.0f, 0f, 0.9f)));

		// row 2
		displayables.add(new MySphere(new Position(2*r, 4*(float)Math.sqrt(3)*r / 3, (float)Math.sqrt(6)*r*2 / 3), new Color(0.3f, 0f, 0.5f)));
		displayables.add(new MySphere(new Position(4*r, 4*(float)Math.sqrt(3)*r / 3, (float)Math.sqrt(6)*r*2 / 3), new Color(0.3f, 0f, 0.5f)));
		displayables.add(new MySphere(new Position(6*r, 4*(float)Math.sqrt(3)*r / 3, (float)Math.sqrt(6)*r*2 / 3), new Color(0.3f, 0f, 0.5f)));

		// row 3
		displayables.add(new MySphere(new Position(2*r, -(2*(float)Math.sqrt(3)*r / 3), (float)Math.sqrt(6)*r*2 / 3), new Color(0.9f, 0f, 0.0f)));
		displayables.add(new MySphere(new Position(4*r, -(2*(float)Math.sqrt(3)*r / 3), (float)Math.sqrt(6)*r*2 / 3), new Color(0.9f, 0f, 0.0f)));
		displayables.add(new MySphere(new Position(6*r, -(2*(float)Math.sqrt(3)*r / 3), (float)Math.sqrt(6)*r*2 / 3), new Color(0.9f, 0f, 0.0f)));

		// level 0
		// row 1
		displayables.add(new MySphere(new Position(r,   (float)Math.sqrt(3)*r / 3, -(float)Math.sqrt(6)*r*2 / 3), new Color(0.5f, 0f, 0.5f)));
		displayables.add(new MySphere(new Position(3*r, (float)Math.sqrt(3)*r / 3, -(float)Math.sqrt(6)*r*2 / 3), new Color(0.5f, 0f, 0.5f)));
		displayables.add(new MySphere(new Position(5*r, (float)Math.sqrt(3)*r / 3, -(float)Math.sqrt(6)*r*2 / 3), new Color(0.5f, 0f, 0.5f)));

		// row 2
		displayables.add(new MySphere(new Position(2*r, 4*(float)Math.sqrt(3)*r / 3, -(float)Math.sqrt(6)*r*2 / 3), new Color(0.3f, 0f, 0.5f)));
		displayables.add(new MySphere(new Position(4*r, 4*(float)Math.sqrt(3)*r / 3, -(float)Math.sqrt(6)*r*2 / 3), new Color(0.3f, 0f, 0.5f)));
		displayables.add(new MySphere(new Position(6*r, 4*(float)Math.sqrt(3)*r / 3, -(float)Math.sqrt(6)*r*2 / 3), new Color(0.3f, 0f, 0.5f)));

		// row 3
		displayables.add(new MySphere(new Position(2*r, -(2*(float)Math.sqrt(3)*r / 3), -(float)Math.sqrt(6)*r*2 / 3), new Color(0.3f, 0f, 0.5f)));
		displayables.add(new MySphere(new Position(4*r, -(2*(float)Math.sqrt(3)*r / 3), -(float)Math.sqrt(6)*r*2 / 3), new Color(0.3f, 0f, 0.5f)));
		displayables.add(new MySphere(new Position(6*r, -(2*(float)Math.sqrt(3)*r / 3), -(float)Math.sqrt(6)*r*2 / 3), new Color(0.3f, 0f, 0.5f)));

		// x, y, z lines
		displayables.add(new Raster(100, 5));
		
		displayables.add(new TimeDisplay(
				new Position(0,150,-300), 
				new Dimension2d(500, 150f), 
				Color.GREEN,
				new Rotation(45f, 1f, 0f, 0f)));

		displayables.add(new TimeDisplay(
				new Position(0,150,300), 
				new Dimension2d(500, 150f), 
				Color.GREEN,
				new Rotation(180f, 0f, 1f, -0.25f)));

		displayables.add(new Box(new Position(0, 0, 0), new Dimension3d(10, 10, 10)));
		
		initDisplay();
		
		texture = TextureLoader.getTexture("png", new FileInputStream(new File("test.png")), GL11.GL_NEAREST);
		float aspectRatio = (float)Display.getWidth()/ (float)Display.getHeight();
		camera = new Camera(70, aspectRatio, 0.3f, 1000f);
		camera.moveZ(-50);
		camera.moveY(-10);

//		camera.moveY(-100);
//		camera.rotateX(90f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);


	}

	public static void main(String[] args) throws Exception {

		MainDisplay mainDisplay = new MainDisplay();
		mainDisplay.run();

	}

	public void run() {

//		PhysicThread physicThread = new PhysicThread(boxes);
//
//		physicThread.start();

		while(!Display.isCloseRequested()) {

//			physicThread.run();

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			float speed=2.5f;
			float mouseSpeed=0.2f;

			if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
				camera.move(speed, 1);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
				camera.move(-speed, 1);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
				camera.move(speed, 0);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
				camera.move(-speed, 0);
			}

			if(Keyboard.isKeyDown(Keyboard.KEY_UP)) {
				camera.moveY(speed);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
				camera.moveY(-speed);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
				camera.rotateY(speed);
			}
			if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
				camera.rotateY(-speed);
			}

			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
//				physicThread.issueStop();
				Display.destroy();
			}

			if(Keyboard.isKeyDown(Keyboard.KEY_N)) {
//				addBox();
			}
			camera.rotateY(Mouse.getDX()*mouseSpeed);
			camera.rotateX(Mouse.getDY()*-mouseSpeed);
			camera.updateView();

//			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());

		 	for(Displayable displayable : displayables) {
		 		displayable.display();
			}		 	

			GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);

			
			Display.update();
//			physicThread.suspend();
			Display.sync(60);
//			physicThread.resume();
		}

		Display.destroy();
//		physicThread.issueStop();
	}

	private void initDisplay() throws LWJGLException {
		DisplayMode[] availableDisplayModes = Display.getAvailableDisplayModes();

		for(DisplayMode displayMode : availableDisplayModes) {
			if(displayMode.isFullscreenCapable() && displayMode.getWidth() == 1024) {
				Display.setDisplayMode(displayMode);
				break;
			}
		}
//		Display.setFullscreen(true);
		Display.create();
		Mouse.setGrabbed(true);
	}

}
