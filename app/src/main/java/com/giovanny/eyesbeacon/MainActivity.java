package com.giovanny.eyesbeacon;

import android.annotation.TargetApi;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.giovanny.eyesbeacon.Modelo.Detectar;
import com.giovanny.eyesbeacon.Sensores.Giroscopio;
import com.giovanny.eyesbeacon.Sensores.Podometro;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    MapaView mapaView;
    TextToSpeech ttsObject;
    Detectar detectar;
    int result;
    private SensorManager sensorManager;
    Podometro podometro;
    Giroscopio giroscopio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapaView= (MapaView)findViewById(R.id.mapa);
        detectar = new Detectar(mapaView);
        ExecutorService threadExecutor = Executors.newCachedThreadPool();
        threadExecutor.execute(detectar);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        podometro = new Podometro(sensorManager,detectar);
        giroscopio = new Giroscopio(sensorManager,detectar);

        hiloHabla();
    }



    private void hablo(){
        if(result==TextToSpeech.LANG_NOT_SUPPORTED || result==TextToSpeech.LANG_MISSING_DATA)
            Toast.makeText(getApplicationContext(),"NO sorportado", Toast.LENGTH_SHORT).show();
        else{
            if(mapaView.getCI()!=null ) {
                String text = mapaView.getCI().getFrase().get(detectar.getFrase());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21(text);
                } else {
                    ttsUnder20(text);
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

    @Override
    protected void onStop()
    {
        super.onStop();
        ttsObject.shutdown();
    }


    private void hiloHabla() {
        new Thread() {
            public void run() {

                while (true) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                hablo();
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
