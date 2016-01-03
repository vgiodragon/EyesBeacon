package com.giovanny.eyesbeacon.Modelo;

/**
 * Created by giovanny on 03/01/16.
 */

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by giovanny on 26/11/15.
 */
public class NodosC {

    private ArrayList<Nodo>nodos;

    private ArrayList<Nodo> ruta;
    private boolean termino=false;

    private ArrayList<Arista> camino;

    public NodosC(ArrayList<Nodo>nodos){
        this.nodos=nodos;
        ruta=new ArrayList<Nodo>();
    }

    public Nodo getNodo(int i){
        return nodos.get(i);
    }

    public int getNodosCount(){
        return nodos.size();
    }


    public ArrayList<Arista> getCamino() {
        return camino;
    }

    public synchronized void IniciaBusqueda(Nodo origen,String MAC){
        ruta.clear();
        setBanderas();
        ruta.add(origen);
        termino=false;

        obtieneCamino(origen, MAC);
        obtengoAristas();
    }

    public void setBanderas(){
        for(int i=0;i<nodos.size();i++)
            nodos.get(i).visitado=false;
    }


    public void obtieneCamino(Nodo origen,String MAC){
        if(!termino) {
            origen.visitado=true;
            if (origen.getMAC().equals(MAC)) {
                Log.d("Caminos", "_ENCONTRE!" + origen.getName());
                termino = true;
            } else {
                Nodo[] hijos = origen.getHjos();
                for (int i = 0; i < origen.getHjos().length; i++) {

                    if (!hijos[i].visitado) {
                        ruta.add(hijos[i]);
                        obtieneCamino(hijos[i], MAC);
                        if(termino)
                            break;
                        ruta.remove(ruta.size() - 1);
                    }
                }
            }
        }
        return;
    }


    public String pRutaDEF(){
        String text="";
        for(int i=0;i<ruta.size();i++){
            text+=ruta.get(i).getName()+",";
        }
        Log.d("Caminos",text);
        return text;
    }

    public void obtengoAristas(){
        camino = new ArrayList<>();
        Log.d("CAAMINO",pRutaDEF());
        for(int i=0;i<ruta.size()-1;i++){
            Nodo act =ruta.get(i);
            Nodo[] hijo = act.getHjos();
            for (int j = 0; j < act.getHjos().length; j++) {
                if(hijo[j].getMAC().equals(ruta.get(i+1).getMAC())){
                    camino.add(new Arista(act.getCx(),act.getCy(),hijo[j].getCx(),hijo[j].getCy()));
                    break;
                }
            }
        }
    }

}
