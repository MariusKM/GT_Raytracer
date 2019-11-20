public class Ray3 {
//TODO  SWITCH TO USEING T FOR HITPOIINT INSTEAD OF SAVING VECTOR3
    private Vector3 Origin;
    private Vector3 Direction;
    private float t0,t1;
    private SceneObject nearest;

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
        this.t0 = t0;
    }

    public float getT0() {
        return t0;
    }


    public void setT0(float t0) {
        this.t0 = t0;
    }


    public SceneObject getNearest() {
        return nearest;
    }


    public void setNearest(SceneObject nearest) {
        this.nearest = nearest;
    }

    public Ray3(Vector3 or, Vector3 dir){
        this.Origin = or;
        this.Direction = dir;
        t0 = 10000;
        nearest = null;

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


