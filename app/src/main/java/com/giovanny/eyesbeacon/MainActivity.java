package com.giovanny.eyesbeacon;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.giovanny.eyesbeacon.Modelo.CargaInformacion;
import com.giovanny.eyesbeacon.Modelo.NodosC;
import com.giovanny.eyesbeacon.Sensores.Beacons;
import com.giovanny.eyesbeacon.Sensores.Giroscopio;
import com.giovanny.eyesbeacon.Sensores.Podometro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextToSpeech ttsObject;
    private int result;
    private SensorManager sensorManager;
    private Podometro podometro;
    private Giroscopio giroscopio;
    private CargaInformacion CI;
    ArrayList<ArrayList<String>> detectados;
    ArrayList<String> TareasARealizar;
    int tA;
    double angz;
    int pasi;
    String estrellas;
    TextView giro;
    TextView step;
    TextView beac;
    TextView tareas;
    TextView tTareasRea;
    TextView tamdfk;
    Beacons beacons;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        beac = (TextView) findViewById(R.id.beacon);
        giro = (TextView) findViewById(R.id.giro);
        step = (TextView) findViewById(R.id.podometro);
        tareas = (TextView) findViewById(R.id.tareas);
        tTareasRea = (TextView) findViewById(R.id.tareasRea);
        tamdfk = (TextView) findViewById(R.id.trmdfk);

        estrellas="";

        CI= new CargaInformacion();
        TareasARealizar=CI.getTarea();
        tareas.setText(CI.getTareas());
        step.setText("_"+0);
        tA=0;

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        podometro = new Podometro(sensorManager);
        giroscopio = new Giroscopio(sensorManager);
        beacons = new Beacons(this,this);


        NodosC NC = new NodosC(CI.getNodos());
        NC.IniciaBusqueda(NC.getNodo(6),0);
        Log.d("ruta","__"+NC.tareaspe());
    }


    private void hablo(String text){
        if(result==TextToSpeech.LANG_NOT_SUPPORTED || result==TextToSpeech.LANG_MISSING_DATA)
            Toast.makeText(getApplicationContext(),"NO sorportado", Toast.LENGTH_SHORT).show();
        else{
//            String text = CI.getFrase().get(detectar.getFrase()); ACA HABIA ALGO CON detectar
//            String text = CI.getFrase().get(0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ttsGreater21(text);
            } else {
                ttsUnder20(text);
            }
        }
    }

    private void Restart(){
        TareasARealizar.remove(0);
        giroscopio.restartAngZ();
        podometro.restartPasos();
    }


    private void Tareaspe(){
        String [] tareaActual = TareasARealizar.get(0).split(" ");

        if(tareaActual[0].equals("Dar")){
            int cant = Integer.parseInt(tareaActual[1]);
            if(cant<podometro.getPasos()){
                estrellas+="*";
                tTareasRea.setText(estrellas);
                Restart();
            }
        }

        else if(tareaActual[0].equals("Gira")){

            int ang=Integer.parseInt(tareaActual[1]);

            if(tareaActual[2].equals("D")){
                ang=(ang-10)*(-1);
                if(giroscopio.getTotalAngZ()<ang){
                    Log.d("rea",giroscopio.getTotalAngZ()+"_"+ang);
                    estrellas+="*";
                    tTareasRea.setText(estrellas);
                    Restart();
                }
            }
            else {
                ang-=10;
                if(giroscopio.getTotalAngZ()>ang){
                    estrellas+="*";
                    tTareasRea.setText(estrellas);
                    Restart();
                }
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
                                hablo(TareasARealizar.get(0));
                            }
                        });
                        Thread.sleep(4800);
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
                                beac.setText(beacons.leoDetectados());
                            }
                        });
                        Thread.sleep(650);
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
                                step.setText(String.format("%d", pasi));
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

    private void hiloTareas() {
        new Thread() {
            public void run() {

                while (true) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            tamdfk.setText(TareasARealizar.get(0));
                            Tareaspe();
                        }
                    });

                    try {
                        Thread.sleep(100);
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
        beacons.onStart();
        hiloHabla();
        hiloPasos();
        hiloGiroscopio();
        hiloBeacons();
        hiloTareas();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ttsObject.shutdown();
        beacons.onStop();
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
