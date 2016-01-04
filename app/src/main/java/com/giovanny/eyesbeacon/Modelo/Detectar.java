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
    int fra;

    public synchronized int getMovimientos() {
        return movimientos;
    }

    public synchronized void ceroMovimientos() {
        movimientos = 0;
    }

    public synchronized void seHizoMovimiento() {
        movimientos -=1;
    }

    public synchronized void dioPaso() {
        movimientos += 4;
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
        while (true) {
            setCI();
            if (cargado && getMovimientos()>0) {
                pps=maVi.getPosition();
                xt=n.get(aris+1).getCx();
                yt=n.get(aris+1).getCy();
                Vu=diferenciales(xt,yt,pps,aris);
                pps[0]+=Vu[0];
                pps[1]+=Vu[1];
                /*
                if(n.get(aris+1).getCx()-pps[0]>4.5f)
                    pps[0]+=dx;
                if(pps[1]-n.get(aris+1).getCy()>4.5f)
                    pps[1]-=dy;
                */

                seHizoMovimiento();
                aris=CambioSentido(n.get(aris+1).getCx()-pps[0],pps[1]-n.get(aris+1).getCy(),aris);
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
