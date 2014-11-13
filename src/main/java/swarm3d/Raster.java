package swarm3d;

import java.util.ArrayList;
import java.util.List;

public class Raster implements Displayable {

	private List<Line> lines = new ArrayList<>();

	public Raster(float width, float stepping) {
		
		float begin=-(width/2);
		lines.add(new Line(new Position(begin, 0f, 0f), new Position(width, 0f, 0f), Color.RED));
		for(float i=begin; i<width; i+=stepping) {
			lines.add(new Line(new Position(begin, 0f, i), new Position(width, 0f, i), Color.DARK_GRAY));				
		}
		
		lines.add(new Line(new Position(0f, begin, 0f), new Position(0f, width, 0f), Color.GREEN));
		for(float i=0; i<width; i+=stepping) {
			lines.add(new Line(new Position(begin, i, 0f), new Position(width, i, 0f), Color.DARK_GRAY));		
		}
		
		lines.add(new Line(new Position(0f, 0, begin), new Position(0f, 0, width), Color.BLUE));
		for(float i=0; i<width; i+=stepping) {
			lines.add(new Line(new Position(0, i, begin), new Position(0, i, width), Color.DARK_GRAY));		
		}	
		
	}
	
	public void display() {
		for(Line line : lines) {
			line.display();
		}
	}
	
}
