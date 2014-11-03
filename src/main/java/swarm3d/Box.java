package swarm3d;

import org.lwjgl.opengl.GL11;

public class Box {

	private float x, y, z, width, height, depth;
	private float rx, ry, rz;
	private float[] backColor = {1f,0f,0f};  // Red
	private float[] frontColor = {1f,1f,0f}; 
	private float[] topColor = {1f,0f,1f};
	private float[] bottomColor = {0f,1f,0f}; // Green
	private float[] leftColor = {0f,1f,1f};  
	private float[] rightColor = {0f,0f,1f}; // blue

	public Box(float x, float y, float z, float width, float height,
			float depth) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.width = width;
		this.height = height;
		this.depth = depth;
	}
	
	public void display() {

		GL11.glPushMatrix(); 
		{
			
			GL11.glTranslatef(x, y, z);
			GL11.glRotatef(rx, 1, 0, 0);
			GL11.glRotatef(ry, 0, 1, 0);
			GL11.glRotatef(rz, 0, 0, 1);
			GL11.glBegin(GL11.GL_QUADS); 
			{

				float back = depth/2;
				float front = -back;

				float top = height/2;
				float bottom = -top;
				
				float right = width/2;
				float left = -right;
				
				GL11.glColor3f(frontColor[0], frontColor[1], frontColor[2]);
				// Front
				GL11.glVertex3f(left, bottom, front);
				GL11.glVertex3f(left, top, front);
				GL11.glVertex3f(right, top, front);
				GL11.glVertex3f(right, bottom, front);
				
				GL11.glColor3f(backColor[0], backColor[1], backColor[2]);
				// Back
				GL11.glVertex3f(left, bottom, back);
				GL11.glVertex3f(left, top, back);
				GL11.glVertex3f(right, top, back);
				GL11.glVertex3f(right, bottom, back);		
				
				GL11.glColor3f(topColor[0], topColor[1], topColor[2]);
				// Top
				GL11.glVertex3f(left,  top, front);
				GL11.glVertex3f(left,  top,  back);
				GL11.glVertex3f(right,  top, back);
				GL11.glVertex3f(right,  top, front);		

				GL11.glColor3f(bottomColor[0], bottomColor[1], bottomColor[2]);
				// Bottom
				GL11.glVertex3f(left, bottom, back);
				GL11.glVertex3f(right, bottom, back); 
				GL11.glVertex3f(right, bottom, front);	
				GL11.glVertex3f(left, bottom, front);	

				GL11.glColor3f(leftColor[0], leftColor[1], leftColor[2]);
				// Left
				GL11.glVertex3f(left, bottom, front);
				GL11.glVertex3f(left, bottom,  back);
				GL11.glVertex3f(left, top,  back);
				GL11.glVertex3f(left, top, front);	
				
				GL11.glColor3f(rightColor[0], rightColor[1], rightColor[2]);
				// Right
				GL11.glVertex3f(right,  bottom, front);    
				GL11.glVertex3f(right,  bottom,  back);    
				GL11.glVertex3f(right,  top,  back);       
				GL11.glVertex3f(right,  top, front);	     
				
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();
		
	}

	public void rotate(float rx, float ry, float rz) {
		this.rx += rx;
		this.ry += ry;
		this.rz += rz;
	}

	public float[] getBackColor() {
		return backColor;
	}

	public void setBackColor(float[] backColor) {
		this.backColor = backColor;
	}

	public float[] getFrontColor() {
		return frontColor;
	}

	public void setFrontColor(float[] frontColor) {
		this.frontColor = frontColor;
	}

	public float[] getTopColor() {
		return topColor;
	}

	public void setTopColor(float[] topColor) {
		this.topColor = topColor;
	}

	public float[] getBottomColor() {
		return bottomColor;
	}

	public void setBottomColor(float[] bottomColor) {
		this.bottomColor = bottomColor;
	}

	public float[] getLeftColor() {
		return leftColor;
	}

	public void setLeftColor(float[] leftColor) {
		this.leftColor = leftColor;
	}

	public float[] getRightColor() {
		return rightColor;
	}

	public void setRightColor(float[] rightColor) {
		this.rightColor = rightColor;
	}
	
	public void setY(float y) {
		this.y = y;
	}
}
