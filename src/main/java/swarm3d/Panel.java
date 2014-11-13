package swarm3d;

import org.lwjgl.opengl.GL11;

public class Panel implements Displayable {

	private Position position;
	private Dimension2d dimension;
	private TextureInfo textureInfo;
	private Color color;
	private Rotation rotation;
	
	public Panel(Position position, Dimension2d dimension, 
			TextureInfo textureInfo, Color color) {
		super();
		this.position = position;
		this.dimension = dimension;
		this.textureInfo = textureInfo;
		this.color = color;
		this.rotation = new Rotation(0, 0, 0, 0);
	}
	
	public Panel(Position position, Dimension2d dimension, 
			Color color) {
		this(position, dimension, null, color);
	}
	
	public Rotation getRotation() {
		return rotation;
	}
	
	@Override
	public void display() {

		GL11.glPushMatrix(); 
		{
			position.bindPosition();
			rotation.bind();
			color.bind();
			GL11.glBegin(GL11.GL_QUADS); 
			{

				float top = dimension.height/2;
				float bottom = -top;
				
				float right = dimension.width/2;
				float left = -right;
				
				if(textureInfo != null) {
					GL11.glTexCoord2f(textureInfo.s0, textureInfo.t1);
				}
				GL11.glVertex2f(left, bottom);
				if(textureInfo != null) {
					GL11.glTexCoord2f(textureInfo.s0, textureInfo.t0);
				}
				GL11.glVertex2f(left, top);
				if(textureInfo != null) {
					GL11.glTexCoord2f(textureInfo.s1, textureInfo.t0);
				}
				GL11.glVertex2f(right, top);
				if(textureInfo != null) {
					GL11.glTexCoord2f(textureInfo.s1, textureInfo.t1);
				}
				GL11.glVertex2f(right, bottom);
				
			}
			GL11.glEnd();
		}
		GL11.glPopMatrix();
		
	}
	
	public TextureInfo getTextureInfo() {
		return textureInfo;
	}
	
}
