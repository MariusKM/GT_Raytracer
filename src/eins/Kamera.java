package eins;

public class Kamera extends SzeneOb {
    private Vektor3 richtung;
    private BildEbene ebene;
    private Vektor3 fokus;
    //private int[] rollwinkel;

    Kamera( Vektor3 position, Vektor3 richtung){
        this.position = position; //should be 0,0,-1
        this.richtung = richtung; //should be 0,0,1
        Vektor3 u = new Vektor3(0,1,0);
        Vektor3 kreuz = Vektor3.kreuzprodukt(u,richtung);
        Vektor3 bildPosition = new Vektor3(position.getX(),position.getY(), position.getZ()+1);
        this.ebene = new BildEbene(bildPosition , new Vektor3(0,1,0), kreuz,new Vektor3(-kreuz.getX(),-kreuz.getY(),-kreuz.getZ() ));
    }


    class BildEbene{
        Vektor3 position;
        Vektor3 u ;
        Vektor3 kreuz ;
        Vektor3 r;

        public BildEbene( Vektor3 position, Vektor3 u, Vektor3 kreuz, Vektor3 r) {
            this.u = u;
            this.kreuz = kreuz;
            this.r = r;
        }

    }

    public Vektor3 getRichtung() {
        return richtung;
    }

    public BildEbene getEbene() {
        return ebene;
    }

    public Vektor3 getFokus() {
        return fokus;
    }

}
