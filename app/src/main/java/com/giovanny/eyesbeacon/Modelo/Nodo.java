package com.giovanny.eyesbeacon.Modelo;

import java.util.ArrayList;

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

    public Nodo(int id,String name,String MAC){
        this.id=id;
        this.name = name;
        this.MAC = MAC;
        Giro=null;
        Escalera=null;
    }

    public String getDescrip() {
        return descrip;
    }

    public String getMAC() {
        return MAC;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getGiro() {
        return Giro;
    }

    public void setGiro(String[] giro) {
        Giro = giro;
    }

    public String[] getEscalera() {
        return Escalera;
    }

    public void setEscalera(String[] escalera) {
        Escalera = escalera;
    }

    public void setHijos(Nodo[] hijos,Integer[] pasos) {
        this.hijos = hijos;
        this.edge=pasos;
    }

    public void recalPasos(int larPaso){
        for(int i =0; i<edge.length;i++){
            edge[i]= ( (edge[i] + larPaso/4) / larPaso ) - 1;
        }
    }

    public Integer[] getPasos() {
        return edge;
    }

    Nodo [] hijos;
    private String name;
    private String descrip;
    private String MAC;
    Integer[] edge;
    private int id ;
    boolean visitado;
    private String[] Giro;
    private String[] Escalera;
}
