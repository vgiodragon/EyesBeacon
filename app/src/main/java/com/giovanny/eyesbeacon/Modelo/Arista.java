package com.giovanny.eyesbeacon.Modelo;

/**
 * Created by giovanny on 03/01/16.
 */
public class Arista {

    public Arista(int xi, int yi, int xf, int yf) {
        this.xi = xi;
        this.yi = yi;
        this.xf = xf;
        this.yf = yf;
    }

    public int getXi() {
        return xi;
    }

    public int getYi() {
        return yi;
    }

    public int getXf() {
        return xf;
    }

    public int getYf() {
        return yf;
    }

    private int xi,yi,xf,yf;
}