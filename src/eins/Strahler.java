package eins;

import static eins.Main.aufX;
import static eins.Main.aufY;

public class Strahler {
    private Szene szene;
    private Kamera kam;
    double [] zPuff = new double[aufX*aufY];
    private int pixelFarbe;
    private int objFarbe;

    public Strahler(Szene szene, Kamera kam){
        this.szene = szene;
        this.kam = kam;
        pixelFarbe = szene.gethFarbe();
    }

    public int[] zeichne() {
        int[] bild = new int[aufX*aufY];
        for(int i=0; i< aufX; i++ ){ //Object obj : szene.objekte
            for(int j=0; j< aufY; j++ ) {
                if(berechneSchnittpunkt(i,j)) {
                    bild[j * aufX + i] =objFarbe; //objFarbe;
                    //System.out.println("schnitt Pixel:"+(j + aufX + i));

                }else {
                    if(bild[j * aufX + i] != 0xffffff)
                    bild[j * aufX + i] = pixelFarbe;
                }
            }
        }

        return bild;
    }

    private boolean berechneSchnittpunkt(int x, int y) {
        zPuff[y * aufX + x] =Double.MAX_VALUE;
        boolean schnitt = false;
        double distanz=0;
        for(SzeneOb obj : szene.objekte){
            Vektor3 st = new Vektor3(x,y, kam.getEbene().u.getZ());
            Strahl strahl = new Strahl(Vektor3.minus(st,kam.position), kam.getRichtung());
            //gerade g = (x,y,-1) + t*kamera.richtung
            //kugel x² +y²+z²=radius
            //-1²=1
            //t=1
            // 0,0,0
            //int t;
            //(x-obj.position.getX()+1*kam.position.getX())

            if(obj instanceof Kugel) {
                Kugel ku = (Kugel) obj;
                // Ursprung des Strahls Minus position der Kugel
                Vektor3 oMc = (Vektor3.minus(strahl.getStartpunkt(), ku.position));
                Vektor3 l = kam.getRichtung();
                //abc formel
                try {
                    double a = 1;
                    double b = 2 * Vektor3.skalarprodukt(oMc, l);
                    double c = Vektor3.betrag(oMc) - ku.getRadius() * ku.getRadius();
                    double ergebnis = b * b - 4.0 * a * c;
                    double r1=0,r2=0;
                    if (ergebnis > 0.0) {
                        r1 = (-b + Math.pow(ergebnis, 0.5)) / (2.0 * a);
                        r2 = (-b - Math.pow(ergebnis, 0.5)) / (2.0 * a);
                        distanz= r1<r2? r1 :r2; //gib kleinere distanz zurück
                        schnitt = true;
                    } else if (ergebnis == 0.0) {
                         r1 = -b / (2.0 * a);
                        distanz=r1;
                        schnitt = true;
                    } else {
                        //schnitt = false;
                    }
                } catch (ArithmeticException e) {
                    //schnitt=false;
                }

                //Diskriminante
                /*double dis = Vektor3.skalarprodukt(l,oMc)*Vektor3.skalarprodukt(l,oMc)-Vektor3.betrag(oMc)*Vektor3.betrag(oMc)-ku.getRadius()*ku.getRadius();
                int d =-(Vektor3.skalarprodukt(kam.getRichtung(),oMc)+Math.sqrt( Vektor3.hoch2(Vektor3.skalarprodukt(kam.getRichtung(),oMc))+Vektor3.betrag(oMc)*Vektor3.betrag(oMc)-ku.getRadius()*ku.getRadius());
                //int d2=-(kam.getRichtung()*(Vektor3.minus(kam.position,obj.position))-
                */

                //obj.position mit schnittpunkt
                //double distanz = Vektor3.betrag(Vektor3.minus(kam.position, obj.position));
                if (schnitt && distanz < zPuff[y * aufX + x]){
                    //if (obj instanceof Kugel) {
                    zPuff[y * aufX + x] = distanz;
                    objFarbe = ku.getFarbe();
                    //System.out.println("farbe rot");
//                }

                }
            }else if(obj instanceof Lampe) {

            }
        }
        return schnitt;
    }

    /*
    public double pq(double a,double b,double c){
        double result = b * b - 4.0 * a * c;

        if (result > 0.0) {
            double r1 = (-b + Math.pow(result, 0.5)) / (2.0 * a);
            double r2 = (-b - Math.pow(result, 0.5)) / (2.0 * a);
            System.out.println("The roots are " + r1 + " and " + r2);
        } else if (result == 0.0) {
            double r1 = -b / (2.0 * a);
            System.out.println("The root is " + r1);
        } else {
            System.out.println("The equation has no real roots.");
        }
    }
    */

}
