package com.giovanny.eyesbeacon.Modelo;

import java.util.Comparator;

/**
 * Created by giovanny on 19/01/16.
 */
public class Beacon {

    public Beacon(String MAC, int RSSI) {
        this.MAC = MAC;
        this.RSSI = RSSI;
    }

    private String MAC;
    private int RSSI;

    public String getMAC() {
        return MAC;
    }

    public int getRSSI() {
        return RSSI;
    }

    public void setRSSI(int RSSI) {
        this.RSSI = RSSI;
    }

}


