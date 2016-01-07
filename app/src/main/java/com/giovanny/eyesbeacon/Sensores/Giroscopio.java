package com.giovanny.eyesbeacon.Sensores;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

/**
 * Created by giovanny on 05/01/16.
 */
public class Giroscopio implements SensorEventListener {
    static final double EPSILON = 0.00000001;
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final double[] deltaRotationVector = new double[4];
    private float timestamp;

    public double totalAngZ;

    private boolean ban;

    SensorManager sensorManager;

    public Giroscopio(SensorManager sensorManager){
        this.sensorManager=sensorManager;
        totalAngZ=0.0f;
        timestamp=0.f;
        ban=false;
    }

    public boolean Resumen(){
        Sensor giroscopio = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (giroscopio!= null) {
            sensorManager.registerListener(this, giroscopio, SensorManager.SENSOR_DELAY_UI);
            return true;
        } else {
            return false;
        }
    }

    public synchronized void restartAngZ(){
        totalAngZ=0f;
    }

    public synchronized double getTotalAngZ(){
        return totalAngZ;
    }

    private synchronized void setAngZ(double z){
        totalAngZ+=z;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float axisX,axisY,axisZ;

        if (ban) {
            final float dT = (event.timestamp - timestamp) * NS2S;
            // Axis of the rotation sample, not normalized yet.
            axisX = event.values[0];
            axisY = event.values[1];
            axisZ = event.values[2];

            // Calculate the angular speed of the sample
            double omegaMagnitude = Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

            // Normalize the rotation vector if it's big enough to get the axis
            // (that is, EPSILON should represent your maximum allowable margin of error)
            if (omegaMagnitude > EPSILON) {
                axisZ /= omegaMagnitude;
            }

            // Integrate around this axis with the angular speed by the timestep
            // in order to get a delta rotation from this sample over the timestep
            // We will convert this axis-angle representation of the delta rotation
            // into a quaternion before turning it into the rotation matrix.
            double thetaOverTwo = omegaMagnitude * dT / 2.0f;
            double sinThetaOverTwo = Math.sin(thetaOverTwo);
            deltaRotationVector[2] = 2*sinThetaOverTwo * axisZ;

            setAngZ(deltaRotationVector[2]*180/3.14159);
        }

        timestamp = event.timestamp;
        ban=true;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
