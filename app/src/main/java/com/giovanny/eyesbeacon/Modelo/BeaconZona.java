package com.giovanny.eyesbeacon.Modelo;

/**
 * Created by giovanny on 11/01/16.
 */
public class BeaconZona {
    private String MAC;

    public String getMAC() {
        return MAC;
    }

    public int getI() {
        return i;
    }

    public String getDescrip() {
        return Descrip;
    }

    private int i;
    private String Descrip;

    public BeaconZona(String MAC, int i, String descrip) {
        this.MAC = MAC;
        this.i = i;
        Descrip = descrip;
    }
}
