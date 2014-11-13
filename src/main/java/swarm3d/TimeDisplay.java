package swarm3d;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class TimeDisplay implements Displayable {

	private Number[] hour, minute, second;
	private List<Displayable> displayables = new ArrayList<Displayable>();

	private Position position;
	private Dimension2d dimension;
	private Color color;	
	private Rotation rotation;
	
	public TimeDisplay(Position position, Dimension2d dimension, Color color, Rotation rotation) {
		super();
		this.position = position;
		this.dimension = dimension;
		this.color = color;
		this.rotation = rotation;
		
		generateDisplayables();
	}
	
	public TimeDisplay(Position position, Dimension2d dimension, Color color) {
		this(position, dimension, color, new Rotation(0, 0, 0, 0));
	}
	
	private void generateDisplayables() {

		float space=(dimension.width*5f)/100f;
		float perNumberWidth = (dimension.width-(2f*space))/6f;
		float left=(-(dimension.width/2) + perNumberWidth/2);
		
		Dimension2d numberDimension = new Dimension2d(perNumberWidth, dimension.height);
		
		hour = new Number[] { 
				new Number(new Position(left, 0, 0), numberDimension, color, 0),
				new Number(new Position(left+(perNumberWidth*1), 0, 0), numberDimension, color, 0)
			};

		minute = new Number[] {
					new Number(new Position(left+(perNumberWidth*2)+(space*1), 0, 0), numberDimension, color, 0),
					new Number(new Position(left+(perNumberWidth*3)+(space*1), 0, 0), numberDimension, color, 0)
				};		
		
		second = new Number[] {
					new Number(new Position(left+(perNumberWidth*4)+(space*2), 0, 0), numberDimension, color, 0),
					new Number(new Position(left+(perNumberWidth*5)+(space*2), 0, 0), numberDimension, color, 0)
				};		
		
		displayables.addAll(Arrays.asList(hour));
		displayables.addAll(Arrays.asList(minute));
		displayables.addAll(Arrays.asList(second));
		
		displayables.add(new Panel(new Position(0, dimension.height/2 + space, 0),
				new Dimension2d(dimension.width+space*2, space), color));
		displayables.add(new Panel(new Position(0, -(dimension.height/2) - space, 0),
				new Dimension2d(dimension.width+space*2, space), color));

		displayables.add(new Panel(new Position(-(dimension.width/2) - space/2, 0, 0),
				new Dimension2d(space/4f, dimension.height+space*2f), color));	
		displayables.add(new Panel(new Position(dimension.width/2 + space/2, 0, 0),
				new Dimension2d(space/4f, dimension.height+space*2f), color));			
	}

	public void display() {
		
		Date currentDate = new Date();
		
		hour[0].setNumber(currentDate.getHours()/10);
		hour[1].setNumber(currentDate.getHours()%10);
		
		minute[0].setNumber(currentDate.getMinutes()/10);
		minute[1].setNumber(currentDate.getMinutes()%10);

		second[0].setNumber(currentDate.getSeconds()/10);
		second[1].setNumber(currentDate.getSeconds()%10);

		GL11.glPushMatrix(); 
		{
			position.bindPosition();
			rotation.bind();
			for(Displayable displayable : displayables) {
				displayable.display();
			}
		}
		GL11.glPopMatrix();
		
	}
	
}
