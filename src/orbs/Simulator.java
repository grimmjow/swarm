package orbs;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Simulator {

	List<GeometricObject> objects = new ArrayList<>();
	private final static double JITTER = 0.1;

	private class Target {
		public double x;
		public double y;
		public double distance;
	}

	public static void main(String[] args) {

		Simulator sim = new Simulator();

		sim.mainLoop();
	}

	public Simulator() {
		initialize();
	}

	private void initialize() {

		Orb motherOrb = new Orb(new Sensor(100));
		motherOrb.setMother(true);

		objects.add(new GeometricObject(0, 0, 20, motherOrb));
		for(int i=0;i<10;i++) {
			objects.add(new GeometricObject(30+(i*20), 0, 10, new Orb(new Sensor((100.0 / (i+10))))));
		}

		setNeighbors();
	}

	public void nextStep() {
		runObjects();
		executeActions();

	}

	private void mainLoop() {

		for(;;){
			nextStep();
		}

	}

	private void executeActionMoveTo(GeometricObject movingObj) {

		GeometricObject staticGeo = getGeoObject(movingObj.orb.nextAction.target);

		Map<Double,Target> targets = getTargets(staticGeo, movingObj);

		int count = 0;
		for (Map.Entry<Double, Target> entry : targets.entrySet()) {
			if (count > 1) {
				break;
			}

			if (entry.getKey() > JITTER) {
				moveObject(movingObj, entry.getValue().x, entry.getValue().y, new ArrayList<GeometricObject>());
				break;
			}

			count++;
		}

	}

	private boolean isCrossing(GeometricObject obj, double x, double y) {

		for (GeometricObject geo : objects) {
			if (geo == obj) {
				continue;
			}
			if (getDistance(geo, obj) < geo.r + obj.r) {
				return true;
			}
		}

		return false;
	}

	private void moveObject(GeometricObject movingObj, double x, double y, List<GeometricObject> alreadyMoved) {

		double deltaX = movingObj.x - x;
		double deltaY = movingObj.y - y;

		movingObj.x = x;
		movingObj.y = y;
		alreadyMoved.add(movingObj);

		for (Orb orb : movingObj.orb.sensor.neighbors) {
			GeometricObject neighbor = getGeoObject(orb);
			// Wurde zurück gelassen
			if (!alreadyMoved.contains(neighbor) && getDistance(movingObj, neighbor) > movingObj.r + neighbor.r + JITTER) {
				moveNeighbors(deltaX, deltaY, movingObj, neighbor, alreadyMoved);
			}
		}
	}

	private void moveNeighbors(double deltaX, double deltaY, GeometricObject targetObj, GeometricObject movingObj, List<GeometricObject> alreadyMoved) {

		if (!isCrossing(movingObj, movingObj.x + deltaX, movingObj.y + deltaY)) {
			moveObject(movingObj,  movingObj.x + deltaX, movingObj.y + deltaY, alreadyMoved);
		} else {
			Map<Double,Target> targets = getTargets(targetObj, movingObj);

			for(Map.Entry<Double,Target> entry : targets.entrySet()) {
				if (!isCrossing(movingObj, entry.getValue().x, entry.getValue().y)) {
					moveObject(movingObj,  entry.getValue().x, entry.getValue().y, alreadyMoved);
				}
			}
		}

	}

	private Map<Double,Target> getTargets(GeometricObject staticObj, GeometricObject movingObj) {
		double range = staticObj.r + movingObj.r*2;
		Map<Double,Target> targets = new HashMap<Double, Target>();

		for (GeometricObject geoObj : objects) {
			// nicht kollidierende ignorieren
			if (geoObj == staticObj || geoObj == movingObj || getDistance(staticObj, geoObj) >= range + geoObj.r) {
				continue;
			}

			// http://www.onlinemathe.de/forum/Berechnung-Schnittpunkte-von-2-Kreisen
			double r1 = staticObj.r + movingObj.r;
			double r2 = geoObj.r + movingObj.r;
			double dx = geoObj.x - staticObj.x;
			double dy = geoObj.y - staticObj.y;
			double d = sqrt(pow(dx, 2) + pow(dy, 2));
			double a = (pow(r1, 2) - pow(r2, 2) + pow(d, 2)) / (2 * d);
			double h = sqrt(pow(r1, 2) - pow(a, 2));

			Target target = new Target();
			target.x = staticObj.x + (a / d * dx - h / d * dy);
		    target.y = staticObj.y + (a / d * dy + h / d * dx);
		    target.distance = getDistance(movingObj, new GeometricObject(target.x, target.y, 0, null));
		    targets.put(target.distance, target);

		    target = new Target();
			target.x = staticObj.x + (a / d * dx + h / d * dy);
		    target.y = staticObj.y + (a / d * dy - h / d * dx);
		    target.distance = getDistance(movingObj, new GeometricObject(target.x, target.y, 0, null));
		    targets.put(target.distance, target);
		}

		return targets;
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
				if (getDistance(obj, sub) <= (obj.r + sub.r + JITTER)) {
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
