package math;

import Util.MathUtil;

public class Vector3{
    public float x,y,z;


    public Vector3(){

        x = 0;
        y = 0;
        z = 0;

    }

    public Vector3(float x,float y,float z){

        this.x = x;
        this.y = y;
        this.z = z;

    }


    public Vector3(Vector3 v){

        this.x = v.x;
        this.y = v.y;
        this.z = v.z;

    }

    public void normalize() {
        float length;

        length = (float) Math.sqrt(x*x + y*y + z*z);

        try {
            x = x / length;
            y = y / length;
            z = z / length;
        } catch (ArithmeticException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public float dotProduct(Vector3 v) {
        return((x*v.x + y*v.y + z*v.z));
    }


    public void crossProduct(Vector3 v) {
        float tmpx = y*v.z - z*v.y,
                tmpy = z*v.x - x*v.z,
                tmpz = x*v.y - y*v.x;
        x = tmpx; y = tmpy; z = tmpz;
    }

    public void mult(float factor) {
        x=x*factor; y=y*factor; z=z*factor;
    }

    public void add(Vector3 v) {
        x=x+v.x; y=y+v.y; z=z+v.z;
    }


    public void sub(Vector3 v, Vector3 z) {
        this.x = v.x - z.x;
        this.y = v.y - z.y;
        this.z = v.z - z.z;
    }

    public Vector3 sub(Vector3 v) {
        return  new Vector3(x - v.x,y - v.y,z - v.z);


    }
    // TODO : ADD dist squared
    public float distance (Vector3 v){
        float d = (float)(Math.sqrt(Math.pow(v.x- this.x,2) + Math.pow(v.y- this.y,2) + Math.pow(v.z- this.z,2)));
        return d;

    }
    static public Vector3 lerp(Vector3 a, Vector3 b, float t){

        Vector3 lerpVec = new Vector3(MathUtil.lerp(a.x, b.x,t),MathUtil.lerp(a.y, b.y,t),MathUtil.lerp(a.z, b.z,t));
        return lerpVec;

    }



    public String toString() {
        String res = new String("["+ x + ", " + y + ", " + z + "]");
        return res;
    }
}


