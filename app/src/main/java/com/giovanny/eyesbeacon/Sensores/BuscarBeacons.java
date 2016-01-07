package com.giovanny.eyesbeacon.Sensores;

import android.content.Context;
import android.os.RemoteException;
import android.util.Log;

import com.jaalee.sdk.BLEDevice;
import com.jaalee.sdk.Beacon;
import com.jaalee.sdk.BeaconManager;
import com.jaalee.sdk.DeviceDiscoverListener;
import com.jaalee.sdk.RangingListener;
import com.jaalee.sdk.Region;
import com.jaalee.sdk.ServiceReadyCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by giovanny on 05/12/15.
 */
public class BuscarBeacons {
    private BeaconManager beaconManager;
    private static final Region ALL_BEACONS_REGION = new Region("rid", null, null, null);

    private ArrayList<ArrayList<String>> detectados;

    public BuscarBeacons(Context context){
        detectados = new ArrayList<ArrayList<String>>();
        beaconManager = new BeaconManager(context);
        beaconManager.setDeviceDiscoverListener(new DeviceDiscoverListener() {

            @Override
            public void onBLEDeviceDiscovered(BLEDevice device) {
                // TODO Auto-generated method stub
                //Log.i("JAALEE", "On ble device  discovery: " + device.getMacAddress());
            }
        });
    }

    public void bBBuscarBeacons(){
        detectados = new ArrayList<>();

        beaconManager.setRangingListener(new RangingListener() {

            @Override
            public void onBeaconsDiscovered(Region region, final List beacons) {
                filterBeacons(beacons);
            }

        });
        //return detectados;
    }

    public synchronized ArrayList<ArrayList<String>> getDetectados(){
        return detectados;
    }

    public synchronized void filterBeacons(List<Beacon> beacons) {
        detectados.clear();
        Log.d("BuscarBeacons2", "Detecto! :" + beacons.size());
        for (Beacon beacon : beacons)
        {
            detectados.add(new ArrayList<String>(
                    Arrays.asList(beacon.getMacAddress(), beacon.getName(),""+beacon.getRssi())));
           // Log.d("BuscarBeacons2", "DETECTO JAALEE del arraylist:" +
             //       detectados.get(detectados.size()-1).get(0)+"__"+detectados.get(detectados.size()-1).get(1));
        }
    }


    public int bBOnStart(){
        // Check if device supports Bluetooth Low Energy.
        if (!beaconManager.hasBluetooth()) {
            //Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
            //Log.d("BuscarBeacons3","onStart: Device does not have Bluetooth Low Energy");
            return 0;
        }

        // If Bluetooth is not enabled, let user enable it.
        if (!beaconManager.isBluetoothEnabled()) {
            /* las siguientes lineas se tienen que hacer en la clase q utiliza esto
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            */
            return 1;
        } else {
            connectToService();
            return 2;
        }
    }

    public void bBonStop(){
        try {
            beaconManager.stopRanging(ALL_BEACONS_REGION);
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void bBOnDestroy(){
        beaconManager.disconnect();
    }

    private void connectToService() {
        //getActionBar().setSubtitle("Scanning...");
        beaconManager.connect(new ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                try {
                    beaconManager.startRangingAndDiscoverDevice(ALL_BEACONS_REGION);
                } catch (RemoteException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }

}
