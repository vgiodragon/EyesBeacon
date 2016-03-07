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
        setTamPaso(54);
        setTareas();
        iniciarNodos();
        iniciaZonas();
    }

    //String name, String descrip, String MAC
    private void iniciarNodos() {
        nodos = new ArrayList<>();

        nodos.add(new Nodo(0,"Entrada Cetic","F9:47:58:EB:AC:A0"));      // 0
        nodos.add(new Nodo(1,"Lobby de Cetic","F9:47:58:EB:AC:A0"));    // 1
        nodos.add(new Nodo(2,"Pasillo 2 primer piso","F9:47:58:EB:AC:A0"));      // 3
        nodos.add(new Nodo(3,"escalera primer piso", "CC:1E:66:4C:E6:93"));   // 6
        nodos.add(new Nodo(4,"mitad escalera 1","CC:1E:66:4C:E6:93"));   // 7
        nodos.add(new Nodo(5,"mitad escalera","CC:1E:66:4C:E6:93"));   // 7
        nodos.add(new Nodo(6,"mitad escalera","CC:1E:66:4C:E6:93"));   // 7
        nodos.add(new Nodo(7,"mitad escalera 2","CC:1E:66:4C:E6:93"));   // 7
        nodos.add(new Nodo(8,"escalera segundo piso", "CC:1E:66:4C:E6:93" ));  // 8
        nodos.add(new Nodo(9,"Pasillo 4 segundo piso", "E2:BE:2C:EC:C0:E2" ));  // 8
        nodos.add(new Nodo(10,"Pasillo 3 segundo piso", "E2:BE:2C:EC:C0:E2" ));  // 8
        nodos.add(new Nodo(11,"Estudiantes Zona", "E2:BE:2C:EC:C0:E2" ));  // 8

        nodos.add(new Nodo(12, "escalera mitad segundo piso", "E2:BE:2C:EC:C0:E2"));//"FB:D3:B5:B9:89:F2" ));         // 9
        nodos.add(new Nodo(13, "Pasillo 2 segundo piso", "C8:1E:15:D5:68:FC"));//"FB:D3:B5:B9:89:F2" ));         // 9
        nodos.add(new Nodo(14, "Entre Labo 1 y 5", "C8:1E:15:D5:68:FC"));//"FB:D3:B5:B9:89:F2" ));         // 9
        nodos.add(new Nodo(15, "Puerta Labo 1", "C8:1E:15:D5:68:FC"));//"FB:D3:B5:B9:89:F2" ));         // 9
        nodos.add(new Nodo(16, "Puerta Labo 5", "C8:1E:15:D5:68:FC"));//"FB:D3:B5:B9:89:F2" ));         // 9
        nodos.add(new Nodo(17, "Entre Labo 2 y 4", "C3:B7:4E:8D:D1:E8"));//"FB:D3:B5:B9:89:F2" ));         // 9
        nodos.add(new Nodo(18, "Puerta Labo 2", "C3:B7:4E:8D:D1:E8"));//"FB:D3:B5:B9:89:F2" ));         // 9
        nodos.add(new Nodo(19, "Puerta Labo 4", "C3:B7:4E:8D:D1:E8"));//"FB:D3:B5:B9:89:F2" ));         // 9

        nodos.get(0).setHijos(new Nodo[]{nodos.get(1)}, new Integer[]{523});

        nodos.get(1).setHijos(new Nodo[]{nodos.get(0), nodos.get(2)}, new Integer[]{523, 433});
            nodos.get(1).setGiro(new String[]{"0 90 2", "2 -90 0"});

        nodos.get(2).setHijos(new Nodo[]{nodos.get(1), nodos.get(3)}, new Integer[]{433, 194});
            nodos.get(2).setGiro(new String[]{"1 -90 3", "3 90 1"});

        nodos.get(3).setHijos(new Nodo[]{nodos.get(2), nodos.get(4)}, new Integer[]{194, 700});
            nodos.get(3).setEscalera(new String[]{"4 subir 7"});

        nodos.get(4).setHijos(new Nodo[]{nodos.get(3), nodos.get(5)}, new Integer[]{700, 158});
            nodos.get(4).setEscalera(new String[]{"3 bajar 7"});

        nodos.get(5).setHijos(new Nodo[]{nodos.get(4), nodos.get(6)}, new Integer[]{158, 286});
            nodos.get(5).setGiro(new String[]{"4 90 6", "6 -90 4"});

        nodos.get(6).setHijos(new Nodo[]{nodos.get(5), nodos.get(7)}, new Integer[]{286, 147});
            nodos.get(6).setGiro(new String[]{"5 90 7", "7 -90 5"});

        nodos.get(7).setHijos(new Nodo[]{nodos.get(6), nodos.get(8)}, new Integer[]{147, 130});
            nodos.get(7).setEscalera(new String[]{"8 subir 13"});

        nodos.get(8).setHijos(new Nodo[]{nodos.get(7), nodos.get(9)}, new Integer[]{130, 273});
            nodos.get(8).setEscalera(new String[]{"7 bajar 13"});

        nodos.get(9).setHijos(new Nodo[]{nodos.get(8), nodos.get(10)}, new Integer[]{273, 822});
            nodos.get(9).setGiro(new String[]{"8 90 10", "10 -90 8"});

        nodos.get(10).setHijos(new Nodo[]{nodos.get(9), nodos.get(11), nodos.get(12)}, new Integer[]{822, 156, 752});
            nodos.get(10).setGiro(new String[]{"9 -90 11", "11 90 9", "11 -90 12", "12 90 11"});

        nodos.get(11).setHijos(new Nodo[]{nodos.get(10)}, new Integer[]{156});

        nodos.get(12).setHijos(new Nodo[]{nodos.get(10), nodos.get(13)}, new Integer[]{752, 296});
            nodos.get(12).setGiro(new String[]{"10 90 13", "13 -90 10"});

        nodos.get(13).setHijos(new Nodo[]{nodos.get(12), nodos.get(14)}, new Integer[]{296, 527});
            nodos.get(13).setGiro(new String[]{"12 -90 14", "14 90 12"});

        nodos.get(14).setHijos(new Nodo[]{nodos.get(13), nodos.get(15), nodos.get(16), nodos.get(17)}, new Integer[]{527, 165, 165, 1030});
            nodos.get(14).setGiro(new String[]{"13 -90 15", "15 90 13", "15 -90 17", "17 90 15",
                    "17 -90 16", "16 90 17", "16 -90 13", "13 90 16"});/// DEBE SER 90 Y 120 PERO YA QUE CHU :D

        nodos.get(15).setHijos(new Nodo[]{nodos.get(14)}, new Integer[]{165});

        nodos.get(16).setHijos(new Nodo[]{nodos.get(14)}, new Integer[]{165});

        nodos.get(17).setHijos(new Nodo[]{nodos.get(14), nodos.get(18), nodos.get(19)}, new Integer[]{1030, 165, 165});
            nodos.get(17).setGiro(new String[]{"14 -90 18", "18 90 14", "14 90 19", "19 -90 14"});

        nodos.get(18).setHijos(new Nodo[]{nodos.get(17)}, new Integer[]{165});

        nodos.get(19).setHijos(new Nodo[]{nodos.get(17)}, new Integer[]{165});

        for(int i=0;i<nodos.size();i++){
            nodos.get(i).recalPasos(getTamPaso());
        }
    }

    private void iniciaZonas(){
        zonas = new ArrayList<>();//jarwer Hardware harware Harbor
        zonas.add(new BeaconZona("F9:47:58:EB:AC:A0",1,"Muro de CTIC"));
        zonas.add(new BeaconZona("CC:1E:66:4C:E6:93",5,"En la mitad de la escalera"));
        zonas.add(new BeaconZona("C8:1E:15:D5:68:FC",10,"puerta del labo Hardware"));
        zonas.add(new BeaconZona("FB:D3:B5:B9:89:F2",14,"En la puerta del laboratorio"));
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

