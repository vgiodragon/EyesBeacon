package com.giovanny.eyesbeacon;

import android.annotation.TargetApi;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.giovanny.eyesbeacon.Modelo.CargaInformacion;
import com.giovanny.eyesbeacon.Modelo.Detectar;
import com.giovanny.eyesbeacon.Sensores.BuscarBeacons;
import com.giovanny.eyesbeacon.Sensores.Giroscopio;
import com.giovanny.eyesbeacon.Sensores.Podometro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1234;
    private TextToSpeech ttsObject;
    private int result;
    private SensorManager sensorManager;
    private Podometro podometro;
    private Giroscopio giroscopio;
    private CargaInformacion CI;
    private BuscarBeacons BB;
    ArrayList<ArrayList<String>> detectados;

    double angz;
    int pasi;
    TextView giro;
    TextView step;
    TextView beacons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beacons = (TextView) findViewById(R.id.beacon);
        giro = (TextView) findViewById(R.id.giro);
        step = (TextView) findViewById(R.id.podometro);
        step.setText("_"+0);
        CI= new CargaInformacion();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        podometro = new Podometro(sensorManager);
        giroscopio = new Giroscopio(sensorManager);


        BB=new BuscarBeacons(this);
        BB.bBBuscarBeacons();

        hiloHabla();
        hiloPasos();
        hiloGiroscopio();
        hiloBeacons();
    }

    private void detectoBeacons(){
        String texto="";
        detectados = BB.getDetectados();
        String detec="";
        if(detectados.size()>0) {
            for(int i=0;i<detectados.size();i++){
                detec+=detectados.get(i).get(1)+"\n";
            }
            beacons.setText(detec);
        }
    }

    private void hablo(){
        if(result==TextToSpeech.LANG_NOT_SUPPORTED || result==TextToSpeech.LANG_MISSING_DATA)
            Toast.makeText(getApplicationContext(),"NO sorportado", Toast.LENGTH_SHORT).show();
        else{
//            String text = CI.getFrase().get(detectar.getFrase()); ACA HABIA ALGO CON detectar
            String text = CI.getFrase().get(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ttsGreater21(text);
            } else {
                ttsUnder20(text);
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        ttsObject.speak(text, TextToSpeech.QUEUE_FLUSH, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        ttsObject.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId);
    }

    private void hiloHabla() {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                //hablo();
                            }
                        });
                        Thread.sleep(5500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }

    private void hiloBeacons() {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                detectoBeacons();
                            }
                        });
                        //Thread.sleep(5500);
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }
    private void hiloPasos() {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                pasi=podometro.getPasos();
                                /*if(pasi%7==0){
                                    giroscopio.restartAngZ();
                                }*/
                                step.setText(String.format("%d",pasi));
                            }
                        });
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        }.start();
    }

    private void hiloGiroscopio() {
        new Thread() {
            public void run() {

                while (true) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                angz=giroscopio.getTotalAngZ();
                                /*if(angz>90f){
                                    podometro.restartPasos();
                                }*/
                                giro.setText(String.format("%f",angz));
                            }
                        });

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }


    @Override
    protected void onStart() {
        super.onStart();
        int op=BB.bBOnStart();
        switch (op) {
            case 0:
                Toast.makeText(this, "Device does not have Bluetooth Low Energy", Toast.LENGTH_LONG).show();
                break;
            case 1:
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                break;
        }

    }
    @Override
    protected void onDestroy() {
        BB.bBOnDestroy();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        BB.bBonStop();
        super.onStop();
        ttsObject.shutdown();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!podometro.Resumen()) {
            Toast.makeText(this, "Count sensor not available!", Toast.LENGTH_LONG).show();
        }
        if (!giroscopio.Resumen()) {
            Toast.makeText(this, "Giroscopio sensor not available!", Toast.LENGTH_LONG).show();
        }
        ttsObject = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Locale loc = new Locale("spa","ESP");
                if(status== TextToSpeech.SUCCESS) result= ttsObject.setLanguage(loc);
                else{
                    Toast.makeText(getApplicationContext(), "NO sorportado", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
