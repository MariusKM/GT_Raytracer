package Objects;// ellipsoid
import math.*;
public class Ellipsoid extends Quadrik {

    /**
     * constructor
     * @param rx radius in x-direction
     * @param ry radius in y-direction
     * @param rz radius in z-direction
     */
    public Ellipsoid(double rx, double ry, double rz, TransformationMatrix4x4 trans ) {
        super(1.0/(rx*rx),1.0/(ry*ry),1.0/(rz*rz),0.,0.,0.,0.,0.,0.,-1.);
        transform(trans);

    }

}