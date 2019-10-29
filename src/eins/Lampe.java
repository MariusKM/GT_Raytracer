package eins;

public class Lampe extends SzeneOb{
    //diffus - kein Winkel
    private int lumen;
    private int lichtFarbe;


    Lampe(Vektor3 position, int lumen){
        this.position = position;
        this.lumen = lumen;
    }
}
