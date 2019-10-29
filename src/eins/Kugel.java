package eins;

public class Kugel extends SzeneOb{
    private double radius;
    private int farbe;

    Kugel(Vektor3 position, double radius, int farbe){
        this.position = position;
        this.radius = radius;
        this.farbe = farbe;
    }

    public double getRadius() {
        return radius;
    }

    public int getFarbe() {
        return farbe;
    }
}
