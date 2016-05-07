package com.giovanny.eyesbeacon.Sensores;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.giovanny.eyesbeacon.Modelo.Beacon;
import com.giovanny.eyesbeacon.Modelo.BeaconComparador;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by giovanny on 07/01/16.
 */public class Beacons {
    private int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter btAdapter;


    private ArrayList<Beacon> detectados;

    public Beacons(Context context,Activity activity){
        BluetoothManager btManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);

        btAdapter = btManager.getAdapter();
        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        detectados= new ArrayList<>();
    }

    private synchronized void anadir(String mac, int RSSI){
        for(int i=0;i<detectados.size();i++){
            if(mac.equals(detectados.get(i).getMAC())){
                detectados.get(i).setRSSI(RSSI);
                return ;
            }
        }
        detectados.add(new Beacon(mac, RSSI));
    }

    public synchronized void detectadosReset(){
        detectados.clear();
    }

    public int numDetec(){
        return detectados.size();
    }

    public ArrayList<Beacon> getDetectados() {
        ArrayList<Beacon> detectadosCpy=detectados;
        Collections.sort(detectadosCpy, new BeaconComparador());
        return detectadosCpy;
    }


    public void onStart(){
        btAdapter.startLeScan(leScanCallback);
    }

    public void onStop() {
        btAdapter.stopLeScan(leScanCallback);
    }

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            anadir(device.getAddress(),rssi);
        }
    };
}