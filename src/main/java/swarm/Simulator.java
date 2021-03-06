package swarm;

import static java.lang.Math.atan2;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Simulator {

	List<GeometricObject> objects = new ArrayList<>();
	private final static double JITTER = 0.001;
	public int executingObject = 0;
	public List<Target> lastTargets;

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

		objects.add(new GeometricObject(0, 0, 0, 50, motherOrb));
		for(int i=0;i<10;i++) {
			objects.add(new GeometricObject(i+1, 75+(i*50), 0, 25, new Orb(new Sensor((100.0 / (i+25))))));
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
		double range = staticGeo.r + movingObj.r*2;

		List<Target> targets = getTargets(staticGeo, movingObj);
		lastTargets = targets;

		for (Target target : targets) {

			if (target.distance < JITTER || isCrossing(movingObj, target.x, target.y)) {
				continue;
			}

			if (!isColliding(movingObj, staticGeo, target)) {
				moveObject(movingObj, target.x, target.y, new ArrayList<GeometricObject>());
				break;
			}
		}

	}

	private double fixAngle(double a, double offset) {
		return (Math.toDegrees(a - offset) + 360) % 360;
	}

	/**
	 *
	 * @param movingObj = soll zu target bewegt werden
	 * @param staticGeo = um dieses soll sich bewegt werden
	 * @param target = wissn schon
	 * @return
	 */
	private boolean isColliding(GeometricObject movingObj, GeometricObject staticGeo, Target target) {
		boolean collidingPositive = false;
		boolean collidingNegative = false;

		double targetOffset = atan2(target.x - staticGeo.x, target.y - staticGeo.y);
		double movingAngle = fixAngle(atan2(movingObj.x - staticGeo.x, movingObj.y - staticGeo.y), targetOffset);

		for (GeometricObject geo : objects) {
			if (getDistance(staticGeo, geo) + JITTER > staticGeo.r + movingObj.r*2 + geo.r
				|| geo == movingObj
				|| geo == staticGeo) {
				continue;
			}

			double angle = fixAngle(atan2(geo.x - staticGeo.x, geo.y - staticGeo.y), targetOffset);
			if (angle < movingAngle) {
				collidingNegative = true;
			} else {
				collidingPositive = true;
			}
		}

		return collidingPositive && collidingNegative;
	}

	/**
	 * Schaut nach, ob sich obj an der Zielkoordinate mit anderen Objekten schneidet
	 * @param obj wird ignoriert
	 * @param x absolute X Zielkoordinate
	 * @param y absolute Y Zielkoordinate
	 * @return
	 */
	private boolean isCrossing(GeometricObject obj, double x, double y) {

		for (GeometricObject geo : objects) {
			if (geo == obj) {
				continue;
			}
			if (getDistance(geo.x, geo.y, x, y) + JITTER < geo.r + obj.r) {
				return true;
			}
		}

		return false;
	}

	/**
	 *
	 * @param movingObj
	 * @param x Absolute Zielkoordinate
	 * @param y
	 * @param alreadyMoved
	 */
	private void moveObject(GeometricObject movingObj, double x, double y, List<GeometricObject> alreadyMoved) {

		if (movingObj.orb.mother) {
			return;
		}

		double deltaX = x - movingObj.x;
		double deltaY = y - movingObj.y;

		System.out.println(movingObj.id + " " + deltaX + ":" + deltaY);

		movingObj.x = x;
		movingObj.y = y;
		alreadyMoved.add(movingObj);

		for (Orb orb : movingObj.orb.sensor.neighbors) {
			GeometricObject neighbor = getGeoObject(orb);
			// Wurde zurück gelassen
			if (!alreadyMoved.contains(neighbor) && orb.sensor.motherDistance < movingObj.orb.sensor.motherDistance) {
				moveNeighbor(deltaX, deltaY, movingObj, neighbor, alreadyMoved);
			}
		}
	}

	/**
	 *
	 * @param deltaX
	 * @param deltaY
	 * @param targetObj hat sich gerade bewegt
	 * @param movingObj soll sich mit bewegen
	 * @param alreadyMoved
	 */
	private void moveNeighbor(double deltaX, double deltaY, GeometricObject targetObj, GeometricObject movingObj, List<GeometricObject> alreadyMoved) {

		System.out.println("moveNeighbour " + movingObj.id);
		if (!isCrossing(movingObj, movingObj.x + deltaX, movingObj.y + deltaY)) {
			moveObject(movingObj,  movingObj.x + deltaX, movingObj.y + deltaY, alreadyMoved);
			System.out.println(movingObj.id + " not crossing");
		} else {
			System.out.println(movingObj.id + " crossing");
			List<Target> targets = getTargets(targetObj, movingObj);

			for(Target entry : targets) {
				if (!isCrossing(movingObj, entry.x, entry.y)) {
					moveObject(movingObj,  entry.x, entry.y, alreadyMoved);
					break;
				}
			}
		}

	}

	/**
	 *
	 * @param staticObj
	 * @param movingObj
	 * @return
	 */
	private List<Target> getTargets(GeometricObject staticObj, GeometricObject movingObj) {
		double range = staticObj.r + movingObj.r*2;
		List<Target> targets = new ArrayList<Target>();

		for (GeometricObject geoObj : objects) {
			// nicht kollidierende ignorieren
			if (geoObj == staticObj || geoObj == movingObj || getDistance(staticObj, geoObj) > range + geoObj.r) {
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
		    target.distance = getDistance(movingObj.x, movingObj.y, target.x, target.y);
		    target.obj = geoObj;
		    targets.add(target);

		    target = new Target();
			target.x = staticObj.x + (a / d * dx + h / d * dy);
		    target.y = staticObj.y + (a / d * dy - h / d * dx);
		    target.distance = getDistance(movingObj.x, movingObj.y, target.x, target.y);
		    target.obj = geoObj;
		    targets.add(target);
		}

		Collections.sort(targets, new Comparator<Target>() {
			@Override
			public int compare(Target o1, Target o2) {
				return (int)((o1.distance - o2.distance)*1000);
			}
		});

		return targets;
	}

	private void executeActions() {

		executingObject++;

		if (executingObject >= objects.size()) {
			executingObject = 0;
		}

		GeometricObject obj = objects.get(executingObject);

		Action nextAction = obj.orb.nextAction;

		System.out.println("execute:" + executingObject);

		if (nextAction.type == ActionType.NONE) {
			return;
		}

		if (nextAction.type == ActionType.MOVE_TO) {
			executeActionMoveTo(obj);
		}

		setNeighbors();

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
		return getDistance(geo1.x, geo1.y, geo2.x, geo2.y);
	}

	private double getDistance(double x1, double y1, double x2, double y2) {
		return sqrt(pow(x1 - x2, 2) + pow(y1 - y2, 2));
	}
}
