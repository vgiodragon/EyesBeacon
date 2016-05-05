package com.giovanny.eyesbeacon.Modelo;

import android.util.Log;

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
        setTamPaso(62);
        setTareas();
        iniciarNodos();
        iniciaZonas();
    }

    //String name, String descrip, String MAC
    private void iniciarNodos() {
        nodos = new ArrayList<>();
        /*Seteo mis nodos!!*/
        nodos.add(new Nodo(0,"Inicio Prueba","F6:91:19:70:6A:4E"));      // 0  Be01
        nodos.add(new Nodo(1,"Punto Corte",  "F6:91:19:70:6A:4E"));      // 0  Be01
        nodos.add(new Nodo(2,"entrada 2",      "F9:47:58:EB:AC:A0"));      // 0  Be02
        nodos.add(new Nodo(3,"Zona 2",     "F9:47:58:EB:AC:A0"));      // 0  Be02
        nodos.add(new Nodo(4,"entrada 3",   "CC:1E:66:4C:E6:93"));      // 0  Be03
        nodos.add(new Nodo(5,"Zona 3",    "CC:1E:66:4C:E6:93"));      // 0  Be03
/*
        nodos.add(new Nodo(0,"Entrada trasera de Cetic","F6:91:19:70:6A:4E"));      // 0  F9:47:58:EB:AC:A0
        nodos.add(new Nodo(1,"Pasillo 1 piso 1","C3:B7:4E:8D:D1:E8"));      // 0  F9:47:58:EB:AC:A0
        nodos.add(new Nodo(2,"Lobby de Cetic","C3:B7:4E:8D:D1:E8"));    // 1
        nodos.add(new Nodo(3,"Pasillo 2 primer piso","C3:B7:4E:8D:D1:E8"));      // 3
        nodos.add(new Nodo(4,"escalera primer piso", "C3:B7:4E:8D:D1:E8"));   // 6
        nodos.add(new Nodo(5,"mitad escalera 1","CC:1E:66:4C:E6:93"));   // 7
        nodos.add(new Nodo(6,"mitad escalera","CC:1E:66:4C:E6:93"));   // 7
        nodos.add(new Nodo(7,"mitad escalera","CC:1E:66:4C:E6:93"));   // 7
        nodos.add(new Nodo(8,"mitad escalera 2","CC:1E:66:4C:E6:93"));   // 7
        nodos.add(new Nodo(9,"escalera segundo piso", "CC:1E:66:4C:E6:93" ));  // 8
        nodos.add(new Nodo(10,"Pasillo 4 segundo piso", "E2:BE:2C:EC:C0:E2" ));  // 8
        nodos.add(new Nodo(11,"Pasillo 3 segundo piso", "E2:BE:2C:EC:C0:E2" ));  // 8
        nodos.add(new Nodo(12,"Estudiantes Zona", "E2:BE:2C:EC:C0:E2" ));  // 8

        nodos.add(new Nodo(13, "escalera mitad segundo piso", "E2:BE:2C:EC:C0:E2"));//"FB:D3:B5:B9:89:F2" ));         // 9
        nodos.add(new Nodo(14, "Pasillo 2 segundo piso", "EA:C6:29:5F:32:4C"));//"FB:D3:B5:B9:89:F2" ));         // 9
        nodos.add(new Nodo(15, "Entre Labo 1 y 5", "EA:C6:29:5F:32:4C"));//"FB:D3:B5:B9:89:F2" ));         // 9
        nodos.add(new Nodo(16, "Puerta Labo 1", "EA:C6:29:5F:32:4C"));//"FB:D3:B5:B9:89:F2" ));         // 9
        nodos.add(new Nodo(17, "Puerta Labo 5", "EA:C6:29:5F:32:4C"));//"FB:D3:B5:B9:89:F2" ));         // 9
        nodos.add(new Nodo(18, "Entre Labo 2 y 4", "C8:1E:15:D5:68:FC"));//C3:B7:4E:8D:D1:E8         // 9
        nodos.add(new Nodo(19, "Puerta Labo 2", "C8:1E:15:D5:68:FC"));//C3:B7:4E:8D:D1:E8         // 9
        nodos.add(new Nodo(20, "Puerta Labo 4", "C8:1E:15:D5:68:FC"));//C3:B7:4E:8D:D1:E8       // 9
*/
        /*  Creo las aristas!! según la distancia en cm entre los nodos*/
        nodos.get(0).setHijos(new Nodo[]{nodos.get(1)}, new Integer[]{260});

        nodos.get(1).setHijos(new Nodo[]{nodos.get(0), nodos.get(2), nodos.get(4)}, new Integer[]{260, 270, 290});
            nodos.get(1).setGiro(new String[]{"0 90 2", "2 -90 0","0 -90 4","4 90 0"});

        nodos.get(2).setHijos(new Nodo[]{nodos.get(1), nodos.get(3)}, new Integer[]{270, 265});
            nodos.get(2).setGiro(new String[]{"1 -90 3", "3 90 1"});

        nodos.get(3).setHijos(new Nodo[]{nodos.get(2)}, new Integer[]{265});


        nodos.get(4).setHijos(new Nodo[]{nodos.get(1), nodos.get(5)}, new Integer[]{290, 210});
            nodos.get(4).setGiro(new String[]{"1 90 5", "5 -90 1"});

        nodos.get(5).setHijos(new Nodo[]{nodos.get(4)}, new Integer[]{210});
        /*
        nodos.get(0).setHijos(new Nodo[]{nodos.get(1)}, new Integer[]{590});

        nodos.get(1).setHijos(new Nodo[]{nodos.get(0), nodos.get(2)}, new Integer[]{590, 526});
            nodos.get(1).setGiro(new String[]{"0 90 2", "2 -90 0"});

        nodos.get(2).setHijos(new Nodo[]{nodos.get(1), nodos.get(3)}, new Integer[]{526, 604});

        nodos.get(3).setHijos(new Nodo[]{nodos.get(2), nodos.get(4)}, new Integer[]{604, 199});
            nodos.get(3).setGiro(new String[]{"2 -90 4", "4 90 2"});


        nodos.get(4).setHijos(new Nodo[]{nodos.get(3), nodos.get(5)}, new Integer[]{199, 700});
            nodos.get(4).setEscalera(new String[]{"5 subir 7"});


        nodos.get(5).setHijos(new Nodo[]{nodos.get(4), nodos.get(6)}, new Integer[]{700, 158});
            nodos.get(5).setEscalera(new String[]{"4 bajar 7"});

        nodos.get(6).setHijos(new Nodo[]{nodos.get(5), nodos.get(7)}, new Integer[]{158, 286});
            nodos.get(6).setGiro(new String[]{"5 90 7", "7 -90 5"});

        nodos.get(7).setHijos(new Nodo[]{nodos.get(6), nodos.get(8)}, new Integer[]{286, 158});
            nodos.get(7).setGiro(new String[]{"6 90 8", "8 -90 6"});



        nodos.get(8).setHijos(new Nodo[]{nodos.get(7), nodos.get(9)}, new Integer[]{158, 130});
            nodos.get(8).setEscalera(new String[]{"9 subir 13"});

        nodos.get(9).setHijos(new Nodo[]{nodos.get(8), nodos.get(10)}, new Integer[]{130, 273});
            nodos.get(9).setEscalera(new String[]{"8 bajar 13"});

        nodos.get(10).setHijos(new Nodo[]{nodos.get(9), nodos.get(11)}, new Integer[]{273, 822});
            nodos.get(10).setGiro(new String[]{"9 90 11", "11 -90 9"});

        nodos.get(11).setHijos(new Nodo[]{nodos.get(10), nodos.get(12), nodos.get(13)}, new Integer[]{822, 156, 752});
            nodos.get(11).setGiro(new String[]{"10 -90 12", "12 90 10", "12 -90 13", "13 90 13"});


        nodos.get(12).setHijos(new Nodo[]{nodos.get(11)}, new Integer[]{156});

        nodos.get(13).setHijos(new Nodo[]{nodos.get(11), nodos.get(12)}, new Integer[]{752, 296});
            nodos.get(13).setGiro(new String[]{"10 90 13", "13 -90 10"});

        nodos.get(14).setHijos(new Nodo[]{nodos.get(13), nodos.get(15)}, new Integer[]{296, 527});
            nodos.get(14).setGiro(new String[]{"13 -90 15", "15 90 13"});

        nodos.get(15).setHijos(new Nodo[]{nodos.get(14), nodos.get(16), nodos.get(17), nodos.get(18)}, new Integer[]{527, 165, 165, 1030});
            nodos.get(15).setGiro(new String[]{"14 -90 16", "16 90 14", "16 -90 18", "18 90 16",
                    "18 -90 17", "17 90 18", "17 -90 14", "14 90 17"});/// DEBE SER 90 Y 120 PERO YA QUE CHU :D

        nodos.get(16).setHijos(new Nodo[]{nodos.get(15)}, new Integer[]{165});

        nodos.get(17).setHijos(new Nodo[]{nodos.get(15)}, new Integer[]{165});

        nodos.get(18).setHijos(new Nodo[]{nodos.get(15), nodos.get(19), nodos.get(20)}, new Integer[]{1030, 165, 165});
            nodos.get(18).setGiro(new String[]{"15 -90 19", "19 90 15", "15 90 20", "20 -90 15"});

        nodos.get(19).setHijos(new Nodo[]{nodos.get(18)}, new Integer[]{165});

        nodos.get(20).setHijos(new Nodo[]{nodos.get(19)}, new Integer[]{165});
*/
        for(int i=0;i<nodos.size();i++){
            nodos.get(i).recalPasos(getTamPaso());
        }
    }

    private void iniciaZonas(){
        zonas = new ArrayList<>();//jarwer Hardware harware Harbor
        //zonas.add(new BeaconZona("F9:47:58:EB:AC:A0",1,"Muro de CTIC"));
        zonas.add(new BeaconZona("F6:91:19:70:6A:4E",0,"Zona Inicial")); /// Be01
        zonas.add(new BeaconZona("F9:47:58:EB:AC:A0",3,"Zona 2")); ///Be02
        zonas.add(new BeaconZona("CC:1E:66:4C:E6:93",5,"Zona 3"));//Be03
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

    public String getPuntoControl(String MAC){
        for(int i=0;i<zonas.size();i++){
            if(MAC.equals(zonas.get(i).getMAC()))
                return zonas.get(i).getDescrip();
        }
        return null;
    }

    public BeaconZona isZona(String zona){
        Log.d("detec","_"+zona);
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
        }

        return -1;
    }
}

