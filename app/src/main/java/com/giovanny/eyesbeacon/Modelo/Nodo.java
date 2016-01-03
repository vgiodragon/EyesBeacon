package com.giovanny.eyesbeacon.Modelo;

/**
 * Created by giovanny on 03/01/16.
 */

public class Nodo {



    public Nodo[] getHjos() {
        return hijos;
    }
    public String getName() {
        return name;
    }

    boolean visitado;

    public Nodo(String name,String MAC,int piso,int cx,int cy,int lx,int ly) {
        this.name = name;
        descrip=null;
        this.ly = ly;
        this.lx = lx;
        this.cy = cy;
        this.cx = cx;
        this.piso = piso;
        this.MAC = MAC;
    }

    public Nodo(String name, String descrip, String MAC,int piso,int cx,int cy,int lx,int ly) {
        this.name = name;
        this.descrip = descrip;
        this.MAC = MAC;
        this.piso=piso;
        this.cx = cx;
        this.cy = cy;
        this.lx = lx;
        this.ly = ly;
    }

    public String getDescrip() {
        return descrip;
    }

    public String getMAC() {
        return MAC;
    }

    public int getPiso() {
        return piso;
    }

    private String name;
    private String descrip;
    private String MAC;
    private int piso;
    private int cx,cy,lx,ly;

    public int getLx() {
        return lx;
    }
    public int getLy() {
        return ly;
    }

    public int getCx() {
        return cx;
    }
    public int getCy() {
        return cy;
    }



    public void setHijos(Nodo[] hijos) {
        this.hijos = hijos;
    }

    Nodo [] hijos;
}
