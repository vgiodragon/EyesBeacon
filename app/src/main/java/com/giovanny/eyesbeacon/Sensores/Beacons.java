package com.giovanny.eyesbeacon.Sensores;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by giovanny on 07/01/16.
 */
public class Beacons {
    private int REQUEST_ENABLE_BT = 1;
    BluetoothAdapter btAdapter;
    ArrayList<String> detectados;
    public Beacons(Context context,Activity activity){
        BluetoothManager btManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);

        btAdapter = btManager.getAdapter();
        if (btAdapter != null && !btAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        detectados= new ArrayList<>();
    }

    private synchronized void anadir(String mac){
        for(int i=0;i<detectados.size();i++){
            if(mac.equals(detectados.get(i)))
                return ;
        }
        detectados.add(mac);
    }

    public synchronized String leoDetectados(){
        String tex="";
        for(int i=0;i<detectados.size();i++){
            tex+=detectados.get(i)+"\n";
        }
        Log.d("bluet", tex);
        detectados.clear();
        return tex;
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
                anadir(device.getAddress());
        }
    };

}
