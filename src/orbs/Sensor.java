package orbs;

import java.util.ArrayList;
import java.util.List;

public class Sensor {

	double motherDistance;
	List<Orb> neighbors = new ArrayList<>();

	public Sensor(double motherDistance) {

		this.motherDistance = motherDistance;

	}

	public void calcMotherDistance() {

		double md = 0;

		for (Orb orb : neighbors) {

			md = Math.max(md, orb.sensor.motherDistance);

		}

		motherDistance = md * 0.75;

	}

}
