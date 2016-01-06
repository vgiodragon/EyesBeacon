package com.giovanny.eyesbeacon.Sensores;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.giovanny.eyesbeacon.Modelo.Detectar;

/**
 * Created by giovanny on 05/01/16.
 */
public class Podometro implements SensorEventListener{

    SensorManager sensorManager;
    Detectar detectar;

    public Podometro (SensorManager sensorManager,Detectar detectar){
        this.sensorManager=sensorManager;
        this.detectar=detectar;
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        detectar.dioPaso();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
