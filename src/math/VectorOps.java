package math;

/**
 * a class with vector operations
 */
public class VectorOps {

    public VectorOps() {}

    /**
     * normalize a 3d vector
     * @param v [in] vector
     * @return normalized v
     */
    public static Vector3D normalize(Vector3D v) {
        double x = v.x; double y = v.y; double z = v.z;
        double norm = x*x+y*y+z*z;
        if (norm==0.0) return new Vector3D(); // math.Vector of length zero
        if (norm!=1.0)
        {
            norm = 1.0/Math.sqrt(norm);
            x *= norm;
            y *= norm;
            z *= norm;
        }
        return new Vector3D(x,y,z);
    }

    /**
     * returns the scalar product of two vectors
     * @param v1 [in] vector #1
     * @param v2 [in] vector #2
     * @return v1 dot v2
     */
    public static double dot(Vector3D v1, Vector3D v2) {
        return v1.x*v2.x + v1.y*v2.y + v1.z*v2.z;
    }

    /**
     * returns the cross product of two vectors
     * @param v1 [in] vector #1
     * @param v2 [in] vector #2
     * @return v1 cross v2
     */
    public static Vector3D cross(Vector3D v1, Vector3D v2) {
        return new Vector3D(
                v1.y*v2.z-v1.z*v2.y,
                v1.z*v2.x-v1.x*v2.z,
                v1.x*v2.y-v1.y*v2.x);
    }

    /**
     * subtracts the vector v2 from v1
     * @param v1 [in] vector #1
     * @param v2 [in] vector #2
     * @return v1-v2
     */
    public static Vector3D minus(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.x-v2.x,v1.y-v2.y,v1.z-v2.z);
    }

    /**
     * adds two vectors
     * @param v1 [in] vector #1
     * @param v2 [in] vector #2
     * @return v1+v2
     */
    public static Vector3D plus(Vector3D v1, Vector3D v2) {
        return new Vector3D(v1.x+v2.x,v1.y+v2.y,v1.z+v2.z);
    }

    /**
     * scales the vector with the double s
     * @param s [in] s
     * @param v [in] vector
     * @return s*v
     */
    public static Vector3D scale(double s, Vector3D v) {
        return new Vector3D(s*v.x, s*v.y, s*v.z);
    }

    /**
     *normalizes a vector and returns its length
     * @param v [in] vector
     * @return length of v
     */
    public static double normalizeMag(Vector3D v) {
        double mag = Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
        if (mag==0.0) return mag; // math.Vector of length zero
        if (mag!=1.0)
        {
            double _1overmag = 1.0/mag;
            v.x *= _1overmag;
            v.y *= _1overmag;
            v.z *= _1overmag;
        }
        return mag;
    }

    /**
     * returns the length of the vector
     * @param v [in] vector
     * @return length of v
     */
    public static double magnitude(Vector3D v) {
        return Math.sqrt(v.x*v.x+v.y*v.y+v.z*v.z);
    }

    /**
     * converts a 4-vector to a 3-vector
     * @param v [in] vector
     * @return 3-vector of v
     */
    public static Vector3D toVector3D(Vector v) {
        return new Vector3D(v.x,v.y,v.z);
    }

    /**
     * converts a 4-vector to a 3d-point
     * @param v [in] vector
     * @return point of v
     */
    public static Point3D toPoint3D(Vector v) {
        return new Point3D(v.x,v.y,v.z);
    }

    /**
     * Returns index of smallest component
     * @param v [in] vector
     * @return index of min(x,y,z)
     */
    public static int indexOfMinAbsComponent( Vector3D v )
    {
        double x = Math.abs(v.x);
        double y = Math.abs(v.y);
        double z = Math.abs(v.z);
        if( x < y && x < z )
            return 0;
        else if( y < z )
            return 1;
        else
            return 2;
    }

    /**
     * Returns a vector perpendicular to this one
     * @param v [in] vector
     * @return vector perpendicular to v
     */
    public static Vector3D perpendicular( Vector3D v )
    {
        int axis = indexOfMinAbsComponent(v);
        if( axis == 0 )
            return new Vector3D(0.0, v.z, -v.y);
        else if( axis == 1 )
            return new Vector3D(v.z, 0.0, -v.x);
        else
            return new Vector3D(v.y, -v.x, 0.0);

    }






}
