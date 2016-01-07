package com.giovanny.eyesbeacon.Modelo;

import java.util.ArrayList;

/**
 * Created by giovanny on 03/01/16.
 */
public class CargaInformacion {

    private ArrayList<Nodo> nodos;
    private ArrayList<String> frase;
    private ArrayList<String> tarea;

    public ArrayList<Nodo> getNodos() {
        return nodos;
    }

    public ArrayList<String> getFrase() {
        return frase;
    }

    public CargaInformacion(int w,int h,int lx,int ly){
        ///me conecto a la base de datos y saco la info par crear nodos

        ///
        iniciarNodos(w, h, lx, ly);
    }

    public CargaInformacion(){
        setFrase();
        setTareas();
    }

    //String name, String descrip, String MAC
    public void iniciarNodos(int w,int h,int lx,int ly) {
        nodos = new ArrayList<>();
        nodos.add(new Nodo("Punto 0", "Esta es la entrada de CTIC aqui se encuentra un encargado que derivara al nuevo visitante al lugar que desea", "F9:47:58:EB:AC:A0", 1,(w)/10 ,  7*(h)/8 ,lx,ly));//Be02
        nodos.add(new Nodo("Punto 1","En el labo 1 piso 1 es un laboratorio destinado para la capacitacion de docente en las nuevas areas teconologicas","FB:D3:B5:B9:89:F2",1,(w)/10, 5*(h)/8,lx,ly));//Be08
        nodos.add(new Nodo("Punto 2", "Laboratorio en que se da clases de Investigacion para alumnos de Pre y Post Grado por parte de Doctores y PHDs", "C8:1E:15:D5:68:FC", 1, (w)/2, 5*(h)/8, lx, ly));//Be06
        nodos.add(new Nodo("Punto 3", "Laboratorio destinado para capacitacion de externo en nuevos programas computacionales de uso empresarial", "E2:BE:2C:EC:C0:E9", 1, 7 * (w) / 10, 3 * (h) / 8, lx, ly));//Be09___
        nodos.add(new Nodo("Punto 4", "Ambiente en que se se gestiona los futuros eventos que se realizaran en CTIC y además se brinda información de la labor de CTIC", "C3:B7:4E:8D:D1:E8", 1, 9 * (w) / 10, (h) / 8, lx, ly));


        //Setear hijos!!
        nodos.get(0).setHijos(new Nodo[]{nodos.get(1)});
        nodos.get(1).setHijos(new Nodo[]{nodos.get(0), nodos.get(2)});
        nodos.get(2).setHijos(new Nodo[]{nodos.get(1), nodos.get(3)});
        nodos.get(3).setHijos(new Nodo[]{nodos.get(2), nodos.get(4)});
        nodos.get(4).setHijos(new Nodo[]{nodos.get(3)});
        frase = new ArrayList<>();
        frase.add("Sigue Adelante equis pasos");
        frase.add("Gira a la Derecha");
        frase.add("Saliste del camino");
    }

    private void setFrase(){
        frase = new ArrayList<>();
        frase.add("Avanza");// X pasos
        frase.add("Gira 90 a la Derecha");
        frase.add("Gira 90 a la Izquierda");
        frase.add("Y llegaras a la"); ///luego
        frase.add("Y estaras "); /// afuera , en la cocina
        frase.add("Saliste del camino");

    }

    public String getTareas(){
        String tareas ="";
        for(int i=0;i<tarea.size();i++)
            tareas+=tarea.get(i)+"\n";

        return tareas;
    }

    private void setTareas(){
        tarea=new ArrayList<>();
        tarea.add("Dar 1");
        tarea.add("Gira 90 D");
        tarea.add("Dar 15");
        tarea.add("Gira 90 I");
        tarea.add("Dar 12");
        tarea.add("Gira 180 D");
        tarea.add("Dar 13");
        tarea.add("Gira 90 Der");
        tarea.add("Dar 11");
        tarea.add("Gira 90 I");
    }

}

