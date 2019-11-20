package math;

/**
 * quaternion number class
 */
public class Quaternion {

	public double r,i,j,k; // the 4 components
	public double s; // the scalar part (r)
	public Vector3D v;// the vector part (i,j,k)
	
	/**
	 * constructor
	 * @param r
	 * @param i
	 * @param j
	 * @param k
	 */
	public Quaternion(double r, double i, double j, double k) {
		this.r = s = r;
		this.i = i;
		this.j = j;
		this.k = k;
		v = new Vector3D(i,j,k);
	}
	
	/**
	 * constructor
	 * @param s [in] scalar part (r)
	 * @param v [in] vector part (i,j,k)
	 */
	public Quaternion(double s, Vector3D v) {
		r = this.s = s;
		i = v.x;
		j = v.y;
		k = v.z;
		this.v = new Vector3D(v);
	}
	
	/**
	 * multiplies two quaternions
	 * @param a [in] quaternion #1
	 * @param b [in] quaternion #2
	 * @return a*b
	 */
	public static Quaternion mul(Quaternion a, Quaternion b) {
		return new Quaternion(
				a.r * b.r - a.i * b.i - a.j * b.j - a.k * b.k,
				a.r * b.i + a.i * b.r + a.j * b.k - a.k * b.j,
				a.r * b.j - a.i * b.k + a.j * b.r + a.k * b.i,
				a.r * b.k + a.i * b.j - a.j * b.i + a.k * b.r
		);
	}
}
