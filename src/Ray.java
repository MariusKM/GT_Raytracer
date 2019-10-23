public class Ray {

    Vector3 Origin;
    Vector3 Direction;
    float t;

    public Ray (Vector3 or, Vector3 dir){
        this.Origin = or;
        this.Direction = dir;
        t = 0;

    }
}


