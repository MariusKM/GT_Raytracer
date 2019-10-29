public class Ray {
//TODO  SWITCH TO USEING T FOR HITPOIINT INSTEAD OF SAVING VECTOR3
    Vector3 Origin;
    Vector3 Direction;
    Vector3 hitPos;
    public double t;

    public Ray (Vector3 or, Vector3 dir){
        this.Origin = or;
        this.Direction = dir;
        hitPos = new Vector3(0,0,0);


    }
}


