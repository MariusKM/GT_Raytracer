package math;

/**
 * a 4 vector for homogene coordinates
 */
public class Vector {
    // homogene coordinates
    public double x,y,z,w;

    /**
     * constructor
     * @param x
     * @param y
     * @param z
     * @param w 4th component
     */
    public Vector(double x, double y, double z, double w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * normalize depending on context (vector or point)
     * @return if normalizing was successfull
     */
    public boolean normalize() {
        // if point always rescale to w=1
        if ((w!=1.0) && (Math.abs(w)>1E-10)) {
            w = 1.0/w;
            x *= w;
            y *= w;
            z *= w;
            w = 1.0;
            return true;
        }
        double norm = x*x+y*y+z*z;
        if (norm==0.0) return false; // math.Vector of length zero
        if (norm!=1.0)
        {
            norm = 1.0/Math.sqrt(norm);
            x *= norm;
            y *= norm;
            z *= norm;
        }
        w = 0.0;
        return true;
    }

    /**
     * calculates the scalar product
     * @param v [in] vector
     * @return this dot v
     */
    public double dot(Vector v) {
        return x*v.x + y*v.y + z*v.z + w*v.w;
    }
}
