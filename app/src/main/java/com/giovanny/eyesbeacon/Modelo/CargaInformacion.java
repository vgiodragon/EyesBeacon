package com.giovanny.eyesbeacon.Modelo;

import com.giovanny.eyesbeacon.Sensores.Beacons;

import java.util.ArrayList;

/**
 * Created by giovanny on 03/01/16.
 */
public class CargaInformacion {

    private ArrayList<Nodo> nodos;
    private ArrayList<BeaconZona> zonas;
    private ArrayList<String> tarea;

    private int tamPaso;

    public ArrayList<Nodo> getNodos() {
        return nodos;
    }


    public int getTamPaso() {
        return tamPaso;
    }

    public void setTamPaso(int tamPaso) {
        this.tamPaso = tamPaso;
    }

    public CargaInformacion(){
        setTamPaso(54);
        setTareas();
        iniciarNodos();
        iniciaZonas();
    }

    //String name, String descrip, String MAC
    private void iniciarNodos() {
        nodos = new ArrayList<>();

        nodos.add(new Nodo(0,"Frente al baño","F6:91:19:70:6A:4E"));      // 0
        nodos.add(new Nodo(1,"Fuera Escalera 2","F6:91:19:70:6A:4E"));    // 1
        //nodos.add(new Nodo("Escalera 2 Piso","F9:47:58:EB:AC:A0"));     // 2
        nodos.add(new Nodo(2,"Mitad de Escalera","F9:47:58:EB:AC:A0"));      // 3

        nodos.add(new Nodo(3,"Fuera Escalera 3", "CC:1E:66:4C:E6:93"));   // 6

        nodos.add(new Nodo(4,"Pasadizo puerta 3","CC:1E:66:4C:E6:93"));   // 7
        nodos.add(new Nodo(5,"Afuera Nano Laboratorio", "CC:1E:66:4C:E6:93" ));  // 8

        nodos.add(new Nodo(6, "Nano Laboratorio", "C8:1E:15:D5:68:FC"));//"FB:D3:B5:B9:89:F2" ));         // 9


        nodos.get(0).setHijos(new Nodo[]{nodos.get(1)}, new Integer[]{486});

        nodos.get(1).setHijos(new Nodo[]{nodos.get(0), nodos.get(2)}, new Integer[]{486, 594});
            nodos.get(1).setGiro(new String[]{"0 90 2", "2 -90 0"});

        nodos.get(2).setHijos(new Nodo[]{nodos.get(1), nodos.get(3)}, new Integer[]{594, 594});
            nodos.get(2).setGiro(new String[]{"1 180 3", "3 -180 1"});

        nodos.get(3).setHijos(new Nodo[]{nodos.get(2), nodos.get(4)}, new Integer[]{594,594});
            nodos.get(3).setGiro(new String[]{"2 -90 4", "4 90 2"});

        nodos.get(4).setHijos(new Nodo[]{nodos.get(3), nodos.get(5)}, new Integer[]{594, 72});

        nodos.get(5).setHijos(new Nodo[]{nodos.get(4), nodos.get(6)}, new Integer[]{72,272});
            nodos.get(5).setGiro(new String[]{"4 90 6", "6 -90 4"});

        nodos.get(6).setHijos(new Nodo[]{nodos.get(5)}, new Integer[]{272});


        for(int i=0;i<nodos.size();i++){
            nodos.get(i).recalPasos(getTamPaso());
        }
    }

    private void iniciaZonas(){
        zonas = new ArrayList<>();
        zonas.add(new BeaconZona("F6:91:19:70:6A:4E",0,"Columna cerca al baño de varones"));
        zonas.add(new BeaconZona("F9:47:58:EB:AC:A0",2,"En la mitad de la escalera"));
        zonas.add(new BeaconZona("CC:1E:66:4C:E6:93",4,"A mitad de camino hay una baranda"));
        zonas.add(new BeaconZona("FB:D3:B5:B9:89:F2",6,"En la puerta del laboratorio"));
    }

    private void setTareas(){
        tarea=new ArrayList<>();
        tarea.add("Dar 1");
        tarea.add("Gira 90 D");
        tarea.add("Dar 20");//22
        tarea.add("Gira 90 I");
        tarea.add("Dar 10");//12
        tarea.add("Gira 180 D");
        tarea.add("Dar 11");//13
        tarea.add("Gira 90 D");
        tarea.add("Dar 9");//11
        tarea.add("Gira 90 I");
    }

    public BeaconZona isZona(String zona){

        for(int i=0;i<zonas.size();i++){
            if(zona.equals(zonas.get(i).getMAC()))
                return zonas.get(i);
        }

        return null;
    }

    public int Destino(String text){
        String []posibles=text.split(" ");
        for(int i=0;i<posibles.length;i++){

            for(int j=0;j<nodos.size();j++){
                String []dname=nodos.get(j).getName().split(" ");

                if(dname[0].equalsIgnoreCase(posibles[i])){
                    boolean igual=true;
                    if(dname.length>1 ){
                        for(int k=1;k<dname.length;k++){
                            if(!dname[k].equalsIgnoreCase(posibles[i+k]))
                                igual=false;
                        }
                    }
                    else igual=false;

                    if(igual)
                        return nodos.get(j).getId();
                }
            }
            /*if(posibles){

            }*/
        }

        return -1;
    }
}

