package swarm3d;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class MainDisplay {
	
	Camera camera;
	List<Box> boxes = new ArrayList<>();
	Sphere sphere = new Sphere();
	Random random = new Random();
	
	public MainDisplay() throws LWJGLException {

//		for(int i=0;i<=200;i++) {
//			addBox();
//		}
		initDisplay();

		float aspectRatio = (float)Display.getWidth()/ (float)Display.getHeight();
		camera = new Camera(70, aspectRatio, 0.3f, 1000f);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

//		boxes.add(new Box(0f, 0f, -20f, 2f, 5f, 7f));
		boxes.add(new Box(0f, 10f, -40f, 2f, 5f, 7f));
		
		
	}

	public static void main(String[] args) throws Exception {
		
		MainDisplay mainDisplay = new MainDisplay();
		mainDisplay.run();
		
	}
	
	public void run() {
		
		PhysicThread physicThread = new PhysicThread(boxes);
		
		physicThread.start();
		
		while(!Display.isCloseRequested()) {
			
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			float speed=0.7f;
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
				physicThread.issueStop();
				Display.destroy();
			}		

			if(Keyboard.isKeyDown(Keyboard.KEY_N)) {
				addBox();
			}	
			camera.rotateY(Mouse.getDX()*mouseSpeed);
			camera.rotateX(Mouse.getDY()*-mouseSpeed);
			camera.updateView();
			
			for(Box box : boxes) {
				box.display();
			}
			
//			sphere.draw(5f, 30, 30);

			Display.update();
		}
		
		Display.destroy();	
		physicThread.issueStop();
	}
	
	private void addBox() {
		
		float bigBox = 20;
		float boxBox = 1;
		Box box = new Box(bigBox*random.nextFloat()*(random.nextBoolean()?1.0f:-1.0f), 
				bigBox*random.nextFloat()*(random.nextBoolean()?1.0f:-1.0f), 
				bigBox*random.nextFloat()*(random.nextBoolean()?1.0f:-1.0f),
				random.nextFloat()*boxBox, 
				random.nextFloat()*boxBox, 
				random.nextFloat()*boxBox);
		box.setBackColor(new float[] {random.nextFloat(), random.nextFloat(), random.nextFloat()});
		box.setFrontColor(new float[] {random.nextFloat(), random.nextFloat(), random.nextFloat()});
		box.setLeftColor(new float[] {random.nextFloat(), random.nextFloat(), random.nextFloat()});
		box.setRightColor(new float[] {random.nextFloat(), random.nextFloat(), random.nextFloat()});
		box.setTopColor(new float[] {random.nextFloat(), random.nextFloat(), random.nextFloat()});
		box.setBottomColor(new float[] {random.nextFloat(), random.nextFloat(), random.nextFloat()});
		boxes.add(box);
	}
	
	private void initDisplay() throws LWJGLException {
		DisplayMode[] availableDisplayModes = Display.getAvailableDisplayModes();
		
		for(DisplayMode displayMode : availableDisplayModes) {
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
