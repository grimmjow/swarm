package swarm3d;

public class Number extends Panel {

	private static float s = 10f/512f;
	private static float t = 14f/512f;
	
	private int number;
	
	public Number(Position position, Dimension2d dimension, Color color, int number) {
		super(position, dimension, new TextureInfo(s*(number-1), 0, s*number, t), color);
	}
	
	public void setNumber(int number) {
		if(number == 0) {
			number = 10;
		}
		
		if(this.number != number && getRotation().w == 0) {
			final Number thatNumber = this;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
				Rotation rotation2 = thatNumber.getRotation();

					rotation2.x = 1.0f;
					while(rotation2.w < 180) {

						if(rotation2.w > 45) {
							getTextureInfo().s0 = s*(getNumber()-1);
							getTextureInfo().s1 = s*getNumber();
							getTextureInfo().setFlip(true);
						}
						
						rotation2.w += 10f;
						try {
							Thread.sleep(50);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					getTextureInfo().setFlip(false);
					rotation2.w = 0;		
				}
			}).start();
		}
		this.number = number;	
	}
	
	public int getNumber() {
		return number;
	}
}
