package swarm3d;

import org.lwjgl.opengl.GL11;

public class Box implements Displayable {

	private Position position;
	private Dimension3d dimension;
	private float rx, ry, rz, rw;
	private Color backColor = new Color(1f,0f,0f);  // Red
	private Color frontColor = new Color(1f,1f,0f); 
	private Color topColor = new Color(1f,0f,1f);
	private Color bottomColor = new Color(0f,1f,0f); // Green
	private Color leftColor = new Color(0f,1f,1f);  
	private Color rightColor = new Color(0f,0f,1f); // blue

	public Box(Position position, Dimension3d dimension) {
		this.position = position;
		this.dimension = dimension;
	}
	
	public void display() {

		GL11.glPushMatrix(); 
		{
			
			position.bindPosition();
			GL11.glRotatef(rw, rx, ry, rz);
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
	
}
