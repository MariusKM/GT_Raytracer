import math.VectorOps;

public class HalfSpace extends SceneObject {

    // a*x+b*y+c*z+d<=0
    private double a,b,c,d;
    /**
     * constructor
     * @param a_
     * @param b_
     * @param c_
     * @param d_
     */
    public HalfSpace(double a_, double b_, double c_, double d_) {
        super();
        a = 0; b = 1; c = 0; d = 0;

        double length = Math.sqrt(a_*a_+b_*b_+c_*c_);
        scale(1, length, 1);
        move(new Vector3(0,-d_,0));
        rotate(VectorOps.cross(new Vector3(0,1,0), new Vector3(a_,b_,c_)), Math.acos(b_/length));
    }
    public void scale(double s) {
        TransformationMatrix4x4 m = new TransformationMatrix4x4();
        m.createScaleMatrix(s, s, s);
        transform(m);
        trans.transform(m);
    }

    @Override
    public boolean intersect(Ray3 ray3, SceneObject object) {
        Vector3D dir = ray.direction;
        double s = a*dir.x+b*dir.y+c*dir.z;
        if (Math.abs(s)<Constants.nearzero) {
            if (isInside(ray.start)) // intersection is the ray itself
                return new IntersectionInfo(-Constants.infinity, Constants.infinity, ray, this, this);
            else // never intersects
                return new IntersectionInfo();
        }
        // intersection exists
        Point3D start = ray.start;
        double t = a*start.x+b*start.y+c*start.z+d;
        if (t==0) { // startpoint is in the plane, so go a bit away from plane and recalculate
            ray.advance(Constants.nearzero);
            t = a*start.x+b*start.y+c*start.z+d;
        }
        double t1 = -t/s;
        if (isInside(start)) {
            if (t1 < 0.0)
                return new IntersectionInfo(0.0, Constants.infinity, ray, this, this);
            else
                return new IntersectionInfo(0.0, t1-Constants.nearzero, ray, this, this);
        }
        else {
                return false;
        }
        return false;
    }

    @Override
    public int shadeDiffuse(Vector3 rayDir, Vector3 sceneOrigin, Light light, float t) {
        return 0;
    }

    @Override
    public boolean shadowCheck(SceneSimple scene, Ray3 myRay3) {
        return false;
    }
}
