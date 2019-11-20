package math;

/**
 * a 2 dimensional point
 */
public class Point2D {
	public double x=0,y=0;
	
	/**
	 * constructor
	 * @param x [in] x coordinate
	 * @param y [in] y coordinate
	 */
	public Point2D(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point2D() {
	}

	/**
	 * copy constructor
	 * @param p [in] other point
	 */
	public Point2D(Point2D p) {
		x = p.x;
		y = p.y;
	}
}
