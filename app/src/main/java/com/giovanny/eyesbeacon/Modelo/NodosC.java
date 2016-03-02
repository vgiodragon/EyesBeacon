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
    private ArrayList<String> ZonasRuta;
    private boolean termino=false;

    private ArrayList<Arista> camino;

    private int anterior;


    private ArrayList<String> tarea;

    public ArrayList<String> getTarea() {
        return tarea;
    }


    public NodosC(ArrayList<Nodo>nodos){
        this.nodos=nodos;
        ruta=new ArrayList<>();
        tarea=new ArrayList<>();
        ZonasRuta=new ArrayList<>();
    }

    public ArrayList<String> getZonasRuta(){
        return ZonasRuta;
    }

    public Nodo getNodo(int i){
        return nodos.get(i);
    }

    public ArrayList<Arista> getCamino() {
        return camino;
    }

    public void IniciaBusqueda(Nodo origen,int id){
        tarea.clear();
        ruta.clear();
        setBanderas();
        ruta.add(origen);
        termino=false;
        anterior=origen.getId();
        obtieneCamino(origen, id);
        obtengoPasos();
    }

    public void setBanderas(){
        for(int i=0;i<nodos.size();i++)
            nodos.get(i).visitado=false;
    }


    public void obtieneCamino(Nodo origen,int id){
        if(!termino) {
            origen.visitado=true;
            if (origen.getId()==id) {
                Log.d("Caminos", "_ENCONTRE!" + origen.getName());
                termino = true;
            } else {
                Nodo[] hijos = origen.getHjos();
                for (int i = 0; i < origen.getHjos().length; i++) {

                    if (!hijos[i].visitado) {
                        ruta.add(hijos[i]);
                        obtieneCamino(hijos[i], id);
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
            text+=ruta.get(i).getId()+",";
        }
        Log.d("Caminos",text);
        return text;
    }

    public void obtengoPasos(){
        tarea = new ArrayList<>();
        Log.d("ruta",pRutaDEF());
        String []giro;

        ZonasRuta.add(ruta.get(0).getMAC());


        for(int i=0;i<ruta.size()-1;i++){
            Nodo act =ruta.get(i);
            Nodo[] hijo = act.getHjos();
            if(!act.getMAC().equals(ZonasRuta.get(ZonasRuta.size()-1)))
                ZonasRuta.add(act.getMAC());
            for (int j = 0; j < act.getHjos().length; j++) {
                if(hijo[j].getId()==ruta.get(i+1).getId()){

                    if(act.getGiro()!=null){
                        for(int k=0;k<act.getGiro().length;k++){///busco para giros
                            giro=act.getGiro()[k].split(" ");

                            if(giro[0].equals(anterior+"") && giro[2].equals(hijo[j].getId()+"")){
                                Log.d("ruta",giro[0]+"/"+anterior+"_"+giro[2]+"/"+hijo[j].getId());
                                if(giro[1].charAt(0)=='-')
                                    tarea.add("Gira " + giro[1].substring(1) +" D");
                                else
                                    tarea.add("Gira " + giro[1] +" I");
                                break;
                            }
                        }
                    }
                    tarea.add("Dar " + act.getPasos()[j] + " pasos");
                    break;
                }
            }
            anterior=act.getId();
        }
    }

    public String tareaspe(){
        String tar="";
        for(int i=0;i<tarea.size();i++){
            tar+=tarea.get(i)+"\n";
        }
        return tar;
    }
}
