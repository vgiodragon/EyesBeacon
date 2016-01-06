package com.giovanny.eyesbeacon.Modelo;

import com.giovanny.eyesbeacon.MapaView;

import java.util.ArrayList;

/**
 * Created by giovanny on 03/01/16.
 */
public class Detectar implements Runnable{

    boolean cargado;
    private CargaInformacion CI;
    ArrayList<Nodo> n;
    MapaView maVi;
    private double ang;
    int fra;

    public synchronized int getMovimientos() {
        return movimientos;
    }

    public synchronized void ceroMovimientos() {
        movimientos = 0;
    }

    public synchronized double seHizoMovimiento() {
        movimientos -=1;
        return ang;
    }

    public synchronized void dioPaso() {
        movimientos += 4;
    }

    public synchronized void setAng(double ang){
        this.ang=ang;
    }

    private int movimientos;

    public Detectar(MapaView mapaView){
        cargado=false;
        maVi=mapaView;
        movimientos = 0;
        fra=0;
    }

    public void setFrase(int f){
        fra=f;
    }

    public int getFrase(){
        return fra;
    }

    public void setCI(){
        if(!cargado){
            CI = maVi.getCI();
            if(CI!=null) {
                n = CI.getNodos();
                cargado = true;
            }
        }
    }

    public int CambioSentido(float rx,float ry,int aris){
        if(rx*rx+ry*ry < 20.2f) {
            setFrase(1);
            ceroMovimientos();
            return aris + 1;
        }
        setFrase(0);
        return aris;
    }

    private float [] diferenciales(float xt,float yt,float []pps,int aris){
        float [] Vu=new float [2];

        double d=  Math.sqrt( Math.pow(xt-pps[0],2.f) + Math.pow(yt-pps[1],2.f) );

        Vu [0]= (float) (4.f*(xt-pps[0])/d);
        Vu [1]= (float) (4.f*(yt-pps[1])/d);

        return Vu;
    }


    @Override
    public void run() {
        float xt,yt;
        int aris=0;
        float [] pps;
        float [] Vu;
        double ang;
        while (true) {
            setCI();
            if (cargado && getMovimientos()>0) {
                ang=seHizoMovimiento();
                pps=maVi.getPosition();
                xt= n.get(aris+1).getCx();
                yt= n.get(aris+1).getCy();
                Vu=diferenciales(xt,yt,pps,aris);
                pps[0]-=4.f*Math.sin(ang);
                pps[1]-=4.f*Math.cos(ang);
                maVi.setPosition(pps);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
