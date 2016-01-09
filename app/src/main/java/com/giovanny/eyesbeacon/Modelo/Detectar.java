package com.giovanny.eyesbeacon.Modelo;

import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by giovanny on 03/01/16.
 *
 */
public class Detectar implements Runnable{

    boolean cargado;
    private CargaInformacion CI;
    ArrayList<Nodo> n;
    private double ang;
    int fra;

    public synchronized int getPasos() {
        return pasos;
    }

    int pasos;

    TextView g;
    TextView pa;

    public Detectar(CargaInformacion CI){
        this.CI=CI;
        n = CI.getNodos();
        cargado=false;
        fra=0;
        pasos=0;
    }

    public void setTextViews(TextView g,TextView p){
        this.g=g;
        pa=p;
        pa.setText(String.format("%d",pasos));
    }

    public void dioPaso() {
        pasos++;
        pa.setText(""+pasos);
    }

    public synchronized void restartPaso(){
        pasos=0;
    }

    public synchronized void setAng(double ang){
        this.ang=ang;
        g.setText(String.format("%f",this.ang));
    }

    public synchronized double getAng(){
        return ang;
    }

    public void setFrase(int f){
        fra=f;
    }

    public int getFrase(){
        return fra;
    }

    @Override
    public void run() {
        float xt,yt;
        int aris=0;
        float [] pps;
        float [] Vu;
        double ang;
        while (true) {
            /*
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
            */
        }
    }


}
