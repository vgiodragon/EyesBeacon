package com.giovanny.eyesbeacon.Modelo;

import java.util.Comparator;

/**
 * Created by giovanny on 19/01/16.
 */
public class BeaconComparador implements Comparator<Beacon> {
    @Override
    public int compare(Beacon lhs, Beacon rhs) {
        return  lhs.getRSSI() - rhs.getRSSI();
    }
}