package com.giovanny.eyesbeacon.Modelo;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by giovanny on 07/03/16.
 */
public class Tareas {
    private ArrayList<String> TareasARealizar;

    public Tareas(){
        TareasARealizar= new ArrayList<>();
    }

    public synchronized String getTareasARealizar0() {
        return TareasARealizar.get(0);
    }

    public synchronized void setTareasARealizar(ArrayList<String> tareasARealizar) {
        TareasARealizar = tareasARealizar;
    }

    public int getTareasSize(){
        return TareasARealizar.size();
    }

    public synchronized void Restart(){
        TareasARealizar.clear();
        Log.d("Restar","resetenado "+TareasARealizar.size());
    }

    public synchronized void Remove0(){
        TareasARealizar.remove(0);
    }

    public synchronized boolean isEmpty(){
        return TareasARealizar.isEmpty();
    }


}
