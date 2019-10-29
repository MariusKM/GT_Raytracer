package eins;

public class Vektor3 {

    private double x,y,z;

    Vektor3(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    Vektor3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    Vektor3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vektor3 kreuzprodukt(Vektor3 u, Vektor3 v){
        double tmpx = u.y*v.z - u.z*v.y,
                tmpy = u.z*v.x - u.x*v.z,
                tmpz = u.x*v.y - u.y*v.x;
        return new Vektor3(tmpx, tmpy,tmpz);
    }

    public static double skalarprodukt(Vektor3 u, Vektor3 v){
        return u.getX()*v.getX()+ u.getY()*v.getY()+u.getZ()*v.getZ();
    }

    public static Vektor3 minus(Vektor3 u, Vektor3 v){
        return new Vektor3(u.getX()-v.getX(), u.getY()-v.getY(), u.getZ()-v.getZ());
    }

    public static Vektor3 hoch2(Vektor3 u){
        return new Vektor3(Math.pow(u.getX(),2),Math.pow(u.getY(),2), Math.pow(u.getZ(),2));
    }

    public static Vektor3 multiplikation(Vektor3 u, double d){
        return new Vektor3(u.getX()*d, u.getY()*d, u.getZ()*d);
    }

    public static double betrag(Vektor3 u) {
        return Math.sqrt(skalarprodukt(u,u));
    }


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
