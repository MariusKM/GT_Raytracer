import Objects.Object;
import math.*;
import Objects.*;

public class Quadrik extends Object {



    // a*x^2+b*y^2+c*z^2+2*d*x*y+2*e*x*z+2*f*y*z+2*g*x+2*h*y+2*j*z+k<=0
    private double a,b,c,d,e,f,g,h,j,k;
    private Matrix4x4 matrix; // constants as matrix

    /**
     * constructor (a*x^2+b*y^2+c*z^2+2*d*x*y+2*e*x*z+2*f*y*z+2*g*x+2*h*y+2*j*z+k<=0)
     * @param a a*x^2
     * @param b b*y^2
     * @param c c*z^2
     * @param d 2*d*x*y
     * @param e 2*e*x*z
     * @param f 2*f*y*z
     * @param g 2*g*x
     * @param h 2*h*y
     * @param j 2*j*z
     * @param k
     */
    public Quadrik(	double a, double b, double c,
                       double d, double e, double f,
                       double g, double h, double j, double k ) {
        super();
        this.a = a;		this.b = b;		this.c = c;		this.d = d;		this.e = e;
        this.f = f;		this.g = g;		this.h = h;		this.j = j;		this.k = k;
        setMatrixFromConstants();
    }

    /**
     * sets the parameter matrix
     */
    private void setMatrixFromConstants() {
        matrix = new Matrix4x4(	a,d,e,g,
                d,b,f,h,
                e,f,c,j,
                g,h,j,k);
    }

    /**
     * sets parameter from the parameter matrix
     */
    private void setConstantsFromMatrix() {
        a = matrix.m00;
        b = matrix.m11;
        c = matrix.m22;
        d = matrix.m01;
        e = matrix.m02;
        f = matrix.m12;
        g = matrix.m03;
        h = matrix.m13;
        j = matrix.m23;
        k = matrix.m33;
    }

    /* (non-Javadoc)
     * @see objects.IObject#Q(math.Point3D)
     */
    public double q(Point3D p) {
        return a*p.x*p.x+b*p.y*p.y+c*p.z*p.z+2.0*(d*p.x*p.y+e*p.x*p.z+f*p.y*p.z+g*p.x+h*p.y+j*p.z)+k;
    }

    /* (non-Javadoc)
     * @see objects.IObject#isInside(math.Point3D)
     */
    public boolean isInside(Point3D p) {
        return a*p.x*p.x+b*p.y*p.y+c*p.z*p.z+2.0*(d*p.x*p.y+e*p.x*p.z+f*p.y*p.z+g*p.x+h*p.y+j*p.z)+k<=0.0;
    }

    /* (non-Javadoc)
     * @see objects.IObject#normal(math.Point3D)
     */
    public Vector3D normal(Point3D p) {
        return VectorOps.normalize( new Vector3D(	g+a*p.x+d*p.y+e*p.z,
                h+d*p.x+b*p.y+f*p.z,
                j+e*p.x+f*p.y+c*p.z	));
    }

    /* (non-Javadoc)
     * @see objects.IObject#calcIntersection(math.Ray)
     */
    public IntersectionInfo calcIntersection(Ray ray) {
        double 	p1,p2,p3,r1,r2,r3;
        p1 = ray.start.x; p2 = ray.start.y; p3 = ray.start.z;
        r1 = ray.direction.x; r2 = ray.direction.y; r3 = ray.direction.z;
        double s = r1*r1*a+r2*r2*b+r3*r3*c+2.0*(r1*r2*d+r1*r3*e+r2*r3*f);
        double t = p1*r1*a+p2*r2*b+p3*r3*c+(p1*r2+p2*r1)*d+(p1*r3+p3*r1)*e+(p2*r3+p3*r2)*f+r1*g+r2*h+r3*j;
        double u = p1*p1*a+p2*p2*b+p3*p3*c+2.0*(p1*p2*d+p1*p3*e+p2*p3*f+p1*g+p2*h+p3*j)+k;
        double D = t*t-s*u;
        if ((D<0.0) || (Math.abs(s)<Constants.nearzero))
            return new IntersectionInfo();
        double sqrtD = Math.sqrt(D);
        double t1 = (-t-sqrtD)/s;
        double t2 = (-t+sqrtD)/s;
        if (t2<t1) {
            double temp = t1;
            t1 = t2;
            t2 = temp;
        }
        if (t2<0.0)
            return new IntersectionInfo();
        if (isInside(ray.getPoint(0.5*(t1+t2))))
            return new IntersectionInfo( t1-Constants.nearzero, t2+Constants.nearzero, ray, this, this);
        else {
            if (isInside(ray.start))
                return new IntersectionInfo( 0, t1+Constants.nearzero, ray, this, this);
            else
                return new IntersectionInfo( t1+Constants.nearzero, t2-Constants.nearzero, ray, this, this);
        }

        //return new IntersectionInfo( t2-Constants.nearzero, Constants.infinity, ray, this, this);
    }

    /* (non-Javadoc)
     * @see objects.IObject#transform(math.TransformationMatrix4x4)
     */
    public void transform(TransformationMatrix4x4 m) {
        Matrix4x4 im = m.getInverseMatrix();
        matrix = MatrixOps.multiply(MatrixOps.multiply(MatrixOps.transpose(im), matrix),im);
        setConstantsFromMatrix();
    }

    /* (non-Javadoc)
     * @see objects.IObject#calculateTextureCoordinates(math.IntersectionInfo)
     */
    public void calculateTextureCoordinates(IntersectionInfo ri) {
        if (!ri.hit) return;
        ri.nextTextureCoords = new Point2D(0.0,0.0);
        double fPhi = Math.acos( ri.nextNormal.y );
        ri.nextTextureCoords.y = fPhi * Constants.inv_pi;

        double fTemp = -ri.nextNormal.x / Math.sin( fPhi ) ;
        double fTheta = Math.acos( fTemp ) * Constants.inv_2pi;

        if(  ri.nextNormal.z < 0.0 )
            ri.nextTextureCoords.x = 1.0 - fTheta;
        else
            ri.nextTextureCoords.x = fTheta;
    }

    /* (non-Javadoc)
     * @see objects.IObject#getCopy()
     */
    public IObject getCopy() {
        Quadrik ret = new Quadrik(a,b,c,d,e,f,g,h,j,k);

        ret.setTransformationMatrix(new TransformationMatrix4x4(trans));
        return ret;
    }

}
