package swarm;

public class Orb {

	boolean mother;
	Sensor sensor;
	Action nextAction;

	public Orb(Sensor sensor) {
		this.sensor = sensor;
	}

	public void run() {

		if (!mother) {

			sensor.calcMotherDistance();

		}

		nextAction = getNextAction();

	}

	private Action getNextAction() {

		Action action = new Action();
		double motherDistance = 0;
		Orb target = null;

		for (Orb neighbor : sensor.neighbors) {

			if (neighbor.sensor.motherDistance > motherDistance) {
				target = neighbor;
				motherDistance = neighbor.sensor.motherDistance;
			}

		}

		if (motherDistance > sensor.motherDistance) {

			action.type = ActionType.MOVE_TO;
			action.target = target;

		}

		return action;

	}

	public void setMother(boolean mother) {
		this.mother = mother;
	}

}
