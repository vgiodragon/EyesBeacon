package com.giovanny.eyesbeacon.Sensores;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;


/**
 * Created by giovanny on 05/01/16.
 */
public class Podometro implements SensorEventListener{

    SensorManager sensorManager;
    int pasos;

    public Podometro (SensorManager sensorManager){
        this.sensorManager=sensorManager;
        pasos=1;
    }

    public boolean Resumen(){
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if (countSensor != null) {
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
            return true;
        } else {
            return false;
        }
    }

    private synchronized void diopaso(){
        pasos++;
    }
    public synchronized int getPasos(){
        return pasos;
    }
    public synchronized void restartPasos(){
        pasos=1;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        diopaso();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
