package swarm3d;

import org.lwjgl.opengl.GL11;

public class Color {

	public final static Color RED = new Color(1f,0f,0f);
	public final static Color GREEN = new Color(0f,1f,0f);
	public final static Color BLUE = new Color(0f,0f,1f);
	public final static Color DARK_GRAY = new Color(0.2f,0.2f,0.2f);
	
	float r, g, b;
	
	public Color(float r, float g, float b) {
		this.r=r;
		this.g=g;
		this.b=b;
	}
	
	public float[] getRgb() {
		return new float[] {r,g,b};
	}

	public void bind() {
		GL11.glColor3f(r, g, b);	
	}
		
}
