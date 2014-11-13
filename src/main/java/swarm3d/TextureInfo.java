package swarm3d;

public class TextureInfo {

	float s0,t0, s1, t1;
	private boolean flip=false;

	public TextureInfo(float s0, float t0, float s1, float t1) {
		super();
		this.s0 = s0;
		this.t0 = t0;
		this.s1 = s1;
		this.t1 = t1;
	}

	@Override
	public String toString() {
		return "TextureInfo [s0=" + s0 + ", t0=" + t0 + ", s1=" + s1 + ", t1="
				+ t1 + "]";
	}
	
	public void setFlip(boolean flip) {
		
		if(flip != this.flip) {
			float s0 = this.s0;
			float s1 = this.s1;
			float t0 = this.t0;
			float t1 = this.t1;
			
			this.s0 = s0;
			this.t0 = t1;

			this.s0 = s0;
			this.t1 = t0;
	
			this.s1 = s1;
			this.t0 = t1;

			this.s1 = s1;
			this.t1 = t0;
			
			this.flip = flip;
		}
	}
	
//	public boolean getFlip() {
//		return flip;
//	}
}
