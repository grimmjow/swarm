package orbs;

public class GeometricObject {

	double x;
	double y;
	int r;
	Orb orb;
	int id;

	public GeometricObject(int id, double x, double y, int r, Orb orb) {
		super();
		this.id = id;
		this.x = x;
		this.y = y;
		this.r = r;
		this.orb = orb;
	}

}
