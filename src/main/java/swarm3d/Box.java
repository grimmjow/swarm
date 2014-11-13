package swarm3d;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class Box implements Displayable {

	private Position position;
	private Dimension3d dimension;
	private float rx, ry, rz=1, rw;
	private Color backColor = new Color(1f,0f,0f);  // Red
	private Color frontColor = new Color(1f,1f,0f); 
	private Color topColor = new Color(1f,0f,1f);
	private Color bottomColor = new Color(0f,1f,0f); // Green
	private Color leftColor = new Color(0f,1f,1f);  
	private Color rightColor = new Color(0f,0f,1f); // blue
	FloatBuffer transformationBuffer;

	public Box(Position position, Dimension3d dimension) {
		this.position = position;
		this.dimension = dimension;
	}
	
	public void display() {

		GL11.glPushMatrix(); 
		{
			
			if(transformationBuffer == null) {
				position.bindPosition();
//				GL11.glRotatef(rw, rx, ry, rz);    
				GL11.glRotatef(rw, rx, 0.0f, 0.0f);
			    GL11.glRotatef(rw, 0.0f, ry, 0.0f);
			    GL11.glRotatef(rw, 0.0f, 0.0f, rz);
			} else {
				GL11.glMultMatrix(transformationBuffer);
			}
			GL11.glBegin(GL11.GL_QUADS); 
			{

				float back = dimension.depth/2;
				float front = -back;

				float top = dimension.height/2;
				float bottom = -top;
				
				float right = dimension.width/2;
				float left = -right;
				
				frontColor.bind();
				// Front
				GL11.glVertex3f(left, bottom, front);
				GL11.glVertex3f(left, top, front);
				GL11.glVertex3f(right, top, front);
				GL11.glVertex3f(right, bottom, front);
				
				backColor.bind();
				// Back
				GL11.glVertex3f(left, bottom, back);
				GL11.glVertex3f(left, top, back);
				GL11.glVertex3f(right, top, back);
				GL11.glVertex3f(right, bottom, back);		
//				
				topColor.bind();
				// Top
				GL11.glVertex3f(left,  top, front);
				GL11.glVertex3f(left,  top,  back);
				GL11.glVertex3f(right,  top, back);
				GL11.glVertex3f(right,  top, front);		

				bottomColor.bind();
				// Bottom
				GL11.glVertex3f(left, bottom, back);
				GL11.glVertex3f(right, bottom, back); 
				GL11.glVertex3f(right, bottom, front);	
				GL11.glVertex3f(left, bottom, front);	

				leftColor.bind();
				// Left
				GL11.glVertex3f(left, bottom, front);
				GL11.glVertex3f(left, bottom,  back);
				GL11.glVertex3f(left, top,  back);
				GL11.glVertex3f(left, top, front);	
				
				rightColor.bind();
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

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Dimension3d getDimension() {
		return dimension;
	}

	public void setDimension(Dimension3d dimension) {
		this.dimension = dimension;
	}

	public Color getBackColor() {
		return backColor;
	}

	public void setBackColor(Color backColor) {
		this.backColor = backColor;
	}

	public Color getFrontColor() {
		return frontColor;
	}

	public void setFrontColor(Color frontColor) {
		this.frontColor = frontColor;
	}

	public Color getTopColor() {
		return topColor;
	}

	public void setTopColor(Color topColor) {
		this.topColor = topColor;
	}

	public Color getBottomColor() {
		return bottomColor;
	}

	public void setBottomColor(Color bottomColor) {
		this.bottomColor = bottomColor;
	}

	public Color getLeftColor() {
		return leftColor;
	}

	public void setLeftColor(Color leftColor) {
		this.leftColor = leftColor;
	}

	public Color getRightColor() {
		return rightColor;
	}

	public void setRightColor(Color rightColor) {
		this.rightColor = rightColor;
	}

	public void rotate(float rx, float ry, float rz) {
		this.rx += rx;
		this.ry += ry;
		this.rz += rz;
	}
	
	public void setY(float y) {
		this.position.y = y;
	}

	public float getX() {
		return position.x;
	}

	public void setX(float x) {
		this.position.x = x;
	}

	public float getZ() {
		return position.z;
	}

	public void setZ(float z) {
		this.position.z = z;
	}

	public float getY() {
		return position.y;
	}

	public float getWidth() {
		return dimension.width;
	}

	public void setWidth(float width) {
		this.dimension.width = width;
	}

	public float getHeight() {
		return dimension.height;
	}

	public void setHeight(float height) {
		this.dimension.height = height;
	}

	public float getDepth() {
		return dimension.depth;
	}

	public void setDepth(float depth) {
		this.dimension.depth = depth;
	}

	public float getRx() {
		return rx;
	}

	public void setRx(float rx) {
		this.rx = rx;
	}

	public float getRy() {
		return ry;
	}

	public void setRy(float ry) {
		this.ry = ry;
	}

	public float getRz() {
		return rz;
	}

	public void setRz(float rz) {
		this.rz = rz;
	}

	public float getRw() {
		return rw;
	}

	public void setRw(float wr) {
		this.rw = wr;
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
