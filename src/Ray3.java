public class Ray3 {
//TODO  SWITCH TO USEING T FOR HITPOIINT INSTEAD OF SAVING VECTOR3
    private Vector3 Origin;
    private Vector3 Direction;
    private float t;
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



    public float getT() {
        return t;
    }

    public void setT(float t) {
        this.t = t;
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
        t = 10000;
        nearest = null;

    }
}


