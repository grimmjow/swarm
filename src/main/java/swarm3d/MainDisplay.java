package swarm3d;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Vector3f;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import swarm3d.Orbs.Magnet;
import swarm3d.Orbs.Orb;

public class MainDisplay {

	Camera camera;

	List<Displayable> displayables = new ArrayList<>();
	List<Displayable> physics = new ArrayList<>();
	Texture texture;
	Random random = new Random();
	FloatBuffer position, ambient;
	Orb orb;

	public MainDisplay() throws LWJGLException, FileNotFoundException, IOException {

		// x, y, z lines
		displayables.add(new Raster(1000, 20));

		displayables.addAll(physics);
		initDisplay();

		texture = TextureLoader.getTexture("png", new FileInputStream(new File("test.png")), GL11.GL_NEAREST);
		float aspectRatio = (float)Display.getWidth()/ (float)Display.getHeight();
		camera = new Camera(70, aspectRatio, 0.3f, 1000f);
		camera.moveY(-5);
		camera.moveZ(-40);

		displayables.add(new Orb(0.5f, 10f, new Position(random.nextInt(1), 1, random.nextInt(1)), 0.75f, 5f));
		displayables.add(new Orb(0.5f, 10f, new Position(1, 1, -2), 1.5f, 5f));
		displayables.add(new Orb(0.5f, 10f, new Position(random.nextInt(1), 2, random.nextInt(1)), 0.75f, 5f));
		displayables.add(new Orb(0.5f, 10f, new Position(random.nextInt(1), 3, random.nextInt(1)), 0.75f, 5f));
		displayables.add(new Orb(0.5f, 10f, new Position(random.nextInt(1), 4, random.nextInt(1)), 0.75f, 5f));
		displayables.add(new Orb(1f, 100f, new Position(random.nextInt(1), 6, random.nextInt(1)), 0.75f, 50f));
		displayables.add(new Orb(0.5f, 10f, new Position(random.nextInt(1), 8, random.nextInt(1)), 0.75f, 5f));
		displayables.add(new Orb(0.5f, 10f, new Position(random.nextInt(1), 9, random.nextInt(1)), 0.75f, 5f));
		displayables.add(new Orb(0.5f, 10f, new Position(random.nextInt(1), 10, random.nextInt(1)), 0.75f, 5f));
		displayables.add(new Orb(0.5f, 10f, new Position(random.nextInt(1), 11, random.nextInt(1)), 0.75f, 5f));

//		orb = new Orb(0.5f, new Position(0, 20, 0));
//		displayables.add(orb);
//		orb = new Orb(0.5f, new Position(1, 30, 0));
//		displayables.add(orb);

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glEnable(GL11.GL_TEXTURE_2D);

	}

	public static void main(String[] args) throws Exception {

		MainDisplay mainDisplay = new MainDisplay();
		mainDisplay.run();

	}

	public void run() {

		PhysicThread physicThread = new PhysicThread(displayables);

		physicThread.start();

		while(!Display.isCloseRequested()) {

			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();

			float speed=0.3f;
			float mouseSpeed=0.1f;

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

			if(Keyboard.isKeyDown(Keyboard.KEY_N)) {
//				addBox();
			}

			if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				break;
			}

			if(Keyboard.isKeyDown(Keyboard.KEY_P)) {

				for(Magnet magent : orb.getMagnets()) {
					Vector3f absolutePosition = magent.getAbsolutePosition(orb.getTransform());
					displayables.add(new Box(new Position(absolutePosition.x, absolutePosition.y, absolutePosition.z),
							new Dimension3d(2, 2, 2)));
				}

				System.out.println("Orb: " + orb);
			}


			camera.rotateY(Mouse.getDX()*mouseSpeed);
			camera.rotateX(Mouse.getDY()*-mouseSpeed);
			camera.updateView();

			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
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
		physicThread.issueStop();
	}

	private void addBox() {

		float bigBox = 500;
		float boxBox = 10f;
		Box box = new Box(new Position(bigBox*random.nextFloat()*(random.nextBoolean()?1.0f:-1.0f),
				bigBox*random.nextFloat() + 10,
				bigBox*random.nextFloat()*(random.nextBoolean()?1.0f:-1.0f)),
				new Dimension3d(
				random.nextFloat()*boxBox,
				random.nextFloat()*boxBox,
				random.nextFloat()*boxBox));
		box.setBackColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		box.setFrontColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		box.setLeftColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		box.setRightColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		box.setTopColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		box.setBottomColor(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		physics.add(box);
	}

	private void initDisplay() throws LWJGLException {
		DisplayMode[] availableDisplayModes = Display.getAvailableDisplayModes();

		for(DisplayMode displayMode : availableDisplayModes) {
			System.out.println(displayMode);
			if(displayMode.isFullscreenCapable() && displayMode.getWidth() == 800) {
				Display.setDisplayMode(displayMode);
				break;
			}
		}
//		Display.setFullscreen(true);
		Display.create();
		Mouse.setGrabbed(true);
	}

}
