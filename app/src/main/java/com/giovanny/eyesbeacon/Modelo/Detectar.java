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
    public Detectar(MapaView mapaView){
        cargado=false;
        maVi=mapaView;
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
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return aris + 1;
        }
        setFrase(0);
        return aris;
    }


/// ESTE HILO SIMULARA EL MOVIMIENTO DE UNA PERSONA EN LA RUTA CORRECTA
    @Override
    public void run() {
        float dx=4.f,dy=4.f;
        int aris=0;
        float [] pps;
        while (true) {
            setCI();
            if (cargado) {
                pps=maVi.getPosition();
                if(n.get(aris+1).getCx()-pps[0]>4.5f)
                    pps[0]+=dx;
                if(pps[1]-n.get(aris+1).getCy()>4.5f)
                    pps[1]-=dy;

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
