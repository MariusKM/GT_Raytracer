package eins;

import java.util.ArrayList;

public class Szene {
    public ArrayList<SzeneOb> objekte;
    private int hFarbe;
    
    public Szene(int hfarbe){
        this.hFarbe = hfarbe;
        objekte = new ArrayList<>();
    }
    public ArrayList<SzeneOb> getObjekte() {
        return objekte;
    }

    public int gethFarbe() {
        return hFarbe;
    }



}
