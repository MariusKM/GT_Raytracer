package math;

/**
 * a ray
 */
public class Ray {
	public Point3D start; // start point of the ray
	public Vector3D direction; // direction of the ray
	
	/**
	 * standard constructor
	 */
	public Ray() {
		start = new Point3D();
		direction = new Vector3D();
	}
	
	/**
	 * copy constructor
	 * @param ray [in] the ray to copy
	 */
	public Ray(Ray ray) {
		start = new Point3D(ray.start);
		direction = new Vector3D(ray.direction);
	}
	
	/**
	 * constructor
	 * @param start [in] start point
	 * @param direction [in] vector of direction
	 */
	public Ray(Point3D start, Vector3D direction) {
		this.start = new Point3D(start);
		this.direction = new Vector3D(direction);
	}
	
	/**
	 * sets the ray
	 * @param start [in] start point
	 * @param direction [in] vector of direction
	 */
	public void set(Point3D start, Vector3D direction) {
		this.start = new Point3D(start);
		this.direction = new Vector3D(direction);
	}
	
	/**
	 * returns the point start+t*direction
	 * @param t [in] distance
	 * @return start+t*direction
	 */
	public Point3D getPoint(double t) {
		return new Point3D(	start.x+t*direction.x,
							start.y+t*direction.y,
							start.z+t*direction.z);
	}
	
	/**
	 * advances the start point of the ray by t*direction
	 * @param t [in] distance to advance the ray
	 */
	public void advance(double t) {
		start.x += t*direction.x;
		start.y += t*direction.y;
		start.z += t*direction.z;
	}
}
