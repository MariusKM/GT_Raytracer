package objects;

import math.TransformationMatrix4x4;

public class EllipticalCylinder extends Quadrik {
    /**
     * constructor
     * @param rx radius in x-direction
     * @param ry radius in y-direction
     * @param rz radius in z-direction
     * @param trans Transformation matrix to adjust ellipsoid
     */
    public EllipticalCylinder(double rx, double ry, double rz, TransformationMatrix4x4 trans ) {
        super(1.0/(rx*rx),1.0/(ry*ry),0.,0.,0.,0.,0.,0.,0.,-1.);
        transform(trans);

    }
}
