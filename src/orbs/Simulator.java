package orbs;

import java.util.ArrayList;
import java.util.List;
import static java.lang.Math.*;

public class Simulator {
	
	List<GeometricObject> objects = new ArrayList<>();

	public static void main(String[] args) {
		
		Simulator sim = new Simulator();
		
		sim.mainLoop();
	}
	
	private Simulator() {
		initialize();
	}
	
	private void initialize() {
		
		Orb motherOrb = new Orb(new Sensor(100));
		motherOrb.setMother(true);
		
		objects.add(new GeometricObject(0, 0, 10, motherOrb));
		for(int i=0;i<10;i++) {
			objects.add(new GeometricObject(11+(i*2), 0, 1, new Orb(new Sensor((100.0 / (i+1))))));
		}
		
	}

	private void mainLoop() {

		setNeighbors();
		
		for(;;){			
			runObjects();
			
			executeActions();
		}
		
	}

	private void executeActions() {
	
		for (GeometricObject obj : objects) {
			
			Action nextAction = obj.orb.nextAction;
			if (nextAction.type == ActionType.NONE) {
				continue;
			}
			
			if (nextAction.type == ActionType.MOVE_TO) {
				
				
				
			}

			setNeighbors();
			
		}
		
		
	}

	private void runObjects() {
		
		for (GeometricObject obj : objects) {
			
			obj.orb.run();
			
		}
		
	}

	private void setNeighbors() {
		
		for(GeometricObject obj : objects) {
			
			obj.orb.sensor.neighbors.clear();
			
			for(GeometricObject sub : objects) {
				if (obj == sub) { 
					continue;
				}
				
				// Entfernung der Objekte <= der Summe der Radien + Rundungsfehler 
				if (sqrt(pow(obj.x - sub.x, 2) + pow(obj.y - sub.y, 2)) <= (obj.r + sub.r + 0.1)) {
					obj.orb.sensor.neighbors.add(sub.orb);
				}					
			}
			
		}
		
	}
}
