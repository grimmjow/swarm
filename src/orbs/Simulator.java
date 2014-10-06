package orbs;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulator {

	List<GeometricObject> objects = new ArrayList<>();

	private class Target {
		public double x;
		public double y;
		public double distance;
	}

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

	private void executeActionMoveTo(GeometricObject obj) {

		GeometricObject staticGeo = getGeoObject(obj.orb.nextAction.target);
		double range = staticGeo.r + obj.r*2;
		Map<Double,Target> targets = new HashMap<Double, Target>();

		for (GeometricObject geoObj : objects) {
			// nicht kollidierende ignorieren
			if (geoObj == staticGeo || getDistance(staticGeo, geoObj) >= range + geoObj.r) {
				continue;
			}


		}

	}

	private void executeActions() {

		for (GeometricObject obj : objects) {

			Action nextAction = obj.orb.nextAction;
			if (nextAction.type == ActionType.NONE) {
				continue;
			}

			if (nextAction.type == ActionType.MOVE_TO) {
				executeActionMoveTo(obj);
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
				if (getDistance(obj, sub) <= (obj.r + sub.r + 0.1)) {
					obj.orb.sensor.neighbors.add(sub.orb);
				}
			}

		}

	}

	private GeometricObject getGeoObject(Orb orb) {

		for (GeometricObject geo : objects) {
			if (geo.orb == orb) {
				return geo;
			}
		}

		return null;
	}

	private double getDistance(GeometricObject geo1, GeometricObject geo2) {
		return sqrt(pow(geo1.x - geo2.x, 2) + pow(geo1.y - geo2.y, 2));
	}
}