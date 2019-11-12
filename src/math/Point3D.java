package math;

/**
 * a 3 dimensional point
 */
public class Point3D extends Vector{
    // since it extends a 4 vector the fourth component is one (point)

    /**
     * constructor
     * @param x [in] x coordinate
     * @param y [in] y coordinate
     * @param z [in] z coordinate
     */
    public Point3D(double x, double y, double z){
        super(x,y,z,1.0);
    }

    /**
     * copy constructor
     * @param p [in] other point
     */
    public Point3D(Point3D p){
        super(p.x,p.y,p.z,1.0);
    }

    /**
     * new point is point + vector
     * @param p [in] point
     * @param v [in] vector
     */
    public Point3D(Point3D p, Vector3D v){
        super(p.x+v.x,p.y+v.y,p.z+v.z,1.0);
    }

    /**
     * standard constructor
     */
    public Point3D(){
        super(0.,0.,0.,1.0);
    }

    public void set(double x_, double y_, double z_) {
        x = x_; y = y_; z = z_;
    }

    public void set(Point3D p) {
        x = p.x; y = p.y; z = p.z;
    }
}

