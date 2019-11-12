package math;

/**
 * a three dimensional vector extending the 4 vector
 * the fourth component is therefor 0
 */
public class Vector3D extends Vector{

    /**
     * standard constructor
     */
    public Vector3D()  {
        super(0.,0.,0.,0.);
    }

    /**
     * constructor, creates a vector from three doubles
     * @param x
     * @param y
     * @param z
     */
    public Vector3D(double x, double y, double z)  {
        super(x,y,z,0.0);
    }

    /**
     * copy constructor
     * @param v [in] vector
     */
    public Vector3D(Vector3D v)  {
        super(v.x,v.y,v.z,0.0);
    }

    /**
     * constructor, creates a vector from a start and an end point
     * @param start [in] start point
     * @param end [in] end point
     */
    public Vector3D(Point3D start, Point3D end) {
        super(end.x - start.x, end.y - start.y, end.z - start.z,0.0);
    }

    /**
     * normalize the vector
     * @return success-state
     */
    public boolean normalize() {
        double norm = x*x+y*y+z*z;
        if (norm==0.0) return false; // math.Vector of length zero
        if (norm!=1.0)
        {
            norm = 1.0/Math.sqrt(norm);
            x *= norm;
            y *= norm;
            z *= norm;
        }
        return true;
    }

    /**
     * returns the scalar product with a vector
     * @param v [in] vector
     * @return this dot v
     */
    public double dot(Vector3D v) {
        return x*v.x + y*v.y + z*v.z;
    }

    /**
     * scales the vector with the double s
     * @param s scale
     */
    public void scale(double s) {
        x *= s;
        y *= s;
        z *= s;
    }

    /**
     * sets the vector
     * @param v [in] vector
     */
    public void set(Vector3D v) {
        x = v.x;
        y = v.y;
        z = v.z;
        w = 0.0;
    }

    /**
     * sets the vector
     * @param x
     * @param y
     * @param z
     */
    public void set(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        w = 0.0;
    }

    /**
     * add a vector
     * @param v the vector to add
     */
    public void add(Vector3D v) {
        x += v.x;
        y += v.y;
        z += v.z;
        w = 0.0;
    }

    /**
     * subtracts a vector
     * @param v the vector to subtract
     */
    public void minus(Vector3D v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        w = 0.0;
    }
}
