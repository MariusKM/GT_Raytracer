package Objects;

import math.Vector3;

public class Ray {

    private Vector3 Origin;
    private Vector3 Direction;
    private float t0,t1,t2Nearest;
    private SceneObject nearest;
    public Vector3 intersection1, intersection2;

    public Vector3 getOrigin() {
        return Origin;
    }

    public void setOrigin(Vector3 origin) {
        Origin = origin;
    }

    public Vector3 getDirection() {
        return Direction;
    }

    public void setDirection(Vector3 direction) {
        Direction = direction;
    }


    public float getT1() {
        return t1;
    }
    public void setT1(float t1) {
        this.t1 = t1;
    }

    public float getT0() {
        return t0;
    }


    public void setT0(float t0) {
        this.t0 = t0;
    }

    public float getT2Nearest() {
        return t2Nearest;
    }

    public void setT2Nearest(float t2Nearest) {
        this.t2Nearest = t2Nearest;
    }

    public SceneObject getNearest() {
        return nearest;
    }

    public Vector3 PointAtParameter(float t) {
      Vector3 point = new Vector3(Direction);
      point.mult(t);
      point.add(Origin);


       return point  ;
    }

    public void setNearest(SceneObject nearest) {
        this.nearest = nearest;
    }

    public Ray(Vector3 or, Vector3 dir){
        this.Origin = or;
        this.Direction = dir;
        t0 = 10000;
        nearest = null;

    }

    public Ray(Ray ray){
        this.Origin = ray.getOrigin();
        this.Direction = ray.getDirection();
        t0 = ray.getT0();
        nearest = ray.getNearest();

    }


    /**
     * returns the point start+t*direction
     * @param t [in] distance
     * @return start+t*direction
     */
    public Vector3 getPoint(float t) {
        return new Vector3(	Origin.x+t*Direction.x,
                Origin.y+t*Direction.y,
                Origin.z+t*Direction.z);
    }

}


