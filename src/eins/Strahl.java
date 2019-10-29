package eins;

public class Strahl {
    private Vektor3 startpunkt;
    private Vektor3 richtung;

    public Strahl(Vektor3 startpunkt, Vektor3 richtung){
        this.richtung = richtung;
        this.startpunkt = startpunkt;
    }

    public Vektor3 getStartpunkt() {
        return startpunkt;
    }

    public Vektor3 getRichtung() {
        return richtung;
    }
}
