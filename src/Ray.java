public class Ray {
//TODO  SWITCH TO USEING T FOR HITPOIINT INSTEAD OF SAVING VECTOR3
    private Vector3 Origin;
    private Vector3 Direction;
    private double t;
    private SphereObject nearest;

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



    public double getT() {
        return t;
    }

    public void setT(double t) {
        this.t = t;
    }

    public SphereObject getNearest() {
        return nearest;
    }

    public void setNearest(SphereObject nearest) {
        this.nearest = nearest;
    }

    public Ray (Vector3 or, Vector3 dir){
        this.Origin = or;
        this.Direction = dir;
        t = 10000;
        nearest = null;

    }
}


