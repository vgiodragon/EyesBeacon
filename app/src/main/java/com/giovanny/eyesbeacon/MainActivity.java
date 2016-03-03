package com.giovanny.eyesbeacon;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.hardware.SensorManager;
import android.os.Build;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.giovanny.eyesbeacon.Modelo.Beacon;
import com.giovanny.eyesbeacon.Modelo.BeaconZona;
import com.giovanny.eyesbeacon.Modelo.CargaInformacion;
import com.giovanny.eyesbeacon.Modelo.Nodo;
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
    protected static final int RESULT_SPEECH = 1;
    ArrayList<Beacon> detectados;
    ArrayList<String> TareasARealizar;
    ArrayList<String> ZonasRuta;
    int tA;
    double angz;
    int pasi;
    boolean llego;
    boolean paso;
    String estrellas;
    TextView giro;
    TextView step;
    TextView beac;
    TextView tareas;
    TextView tTareasRea;
    TextView tamdfk;
    Beacons beacons;
    private boolean espera;
    NodosC NC;
    BeaconZona ant, actual,siguiente;
    int desti;
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
        llego=false;
        actual=null;
         CI= new CargaInformacion();
        NC = new NodosC(CI.getNodos());
        step.setText("_"+0);
        tA=0;
        espera=false;
        ZonasRuta=new ArrayList<>();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        podometro = new Podometro(sensorManager);
        giroscopio = new Giroscopio(sensorManager);
        beacons = new Beacons(this,this);
    }


    private synchronized void hablo(String text, boolean wait){
        if(!espera)
            if(result==TextToSpeech.LANG_NOT_SUPPORTED || result==TextToSpeech.LANG_MISSING_DATA)
                Toast.makeText(getApplicationContext(),"NO sorportado", Toast.LENGTH_SHORT).show();
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ttsGreater21(text);
                } else {
                    ttsUnder20(text);
                }
            }
        espera=wait;
    }

    private synchronized void getZonasRuta(){
        ZonasRuta=NC.getZonasRuta();
    }

    private synchronized String primerZonasRuta(){
        return ZonasRuta.get(0);
    }

    private synchronized void passZonasRuta(){
        if(ZonasRuta.size()>0)
            ZonasRuta.remove(0);
    }

    private void Restart(){
        TareasARealizar.remove(0);

        if(!TareasARealizar.isEmpty()) {
            hablo(TareasARealizar.get(0), true);
            giroscopio.restartAngZ();
            podometro.restartPasos();
        }
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
        else if(tareaActual[0].equals("Escalera")){
            int cant = Integer.parseInt(tareaActual[4]);
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

    private synchronized void setActual(BeaconZona actual){
        this.actual=actual;
    }

    private synchronized int getIActual(){
        return ant.getI();
    }

    public String showBeacon(){
        String res="";
        for(int i=0;i<detectados.size();i++){
            res+=detectados.get(i).getMAC()+"_"+detectados.get(i).getRSSI()+"\n";
        }
        beacons.detectadosReset();
        return res;
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
                while (!TareasARealizar.isEmpty()) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                hablo(TareasARealizar.get(0),false);
                            }
                        });
                        Thread.sleep(4800);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                hablo("Has llegado a "+CI.getNodos().get(desti).getName(),false);
            }
        }.start();
    }

    private void hiloBeacons() {
        beacons.onStart();

        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                detectados=beacons.getDetectados();
                                beac.setText(showBeacon());
                                for(int d=0;d<detectados.size();d++){
                                    siguiente =CI.isZona(detectados.get(d).getMAC());
                                    if(siguiente!=null) {
                                        ant = actual;
                                        setActual(siguiente);
                                        Log.d("actual",actual.getMAC()+"_"+actual.getDescrip());
                                    }
                                }
                                if(ZonasRuta.size()>0 && detectados.size()>0){
                                    Beacon beac=detectados.get(0);
                                    if(beac.getRSSI()<-87){
                                        if(llego) {
                                            llego = false;
                                            paso =true;
                                        }
                                        if(paso)
                                            passZonasRuta();
                                    }
                                    if(beac.getRSSI()>-82){
                                        if(beac.getMAC().equals(primerZonasRuta())){
                                            llego=true;
                                            paso=false;
                                        }
                                        else{
                                            hablo("SALISTE DEL CAMINO",espera);
                                            NC.obtieneCamino(NC.getNodo(ant.getI()),desti);
                                            tareas.setText("");
                                            LanzoHilos(desti);
                                        }
                                    }
                                }



                            }
                        });
                        Thread.sleep(1500);
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
                while (!TareasARealizar.isEmpty()) {
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

                while (!TareasARealizar.isEmpty()) {
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

                while (!TareasARealizar.isEmpty()) {
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

    private void EscuchoaTexto(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        try {
            startActivityForResult(intent, RESULT_SPEECH);
        } catch (ActivityNotFoundException a) {
            Toast t = Toast.makeText(getApplicationContext(),
                    "Opps! Your device doesn't support Speech to Text",
                    Toast.LENGTH_SHORT);
            t.show();
        }
    }

    private void LanzoHilos(int res){
        NC = new NodosC(CI.getNodos());
        NC.IniciaBusqueda(NC.getNodo(getIActual()), res);
        TareasARealizar=NC.getTarea();
        tareas.setText(NC.tareaspe());
        getZonasRuta();
        Log.d("MAIN", "LANZO HILOS " + TareasARealizar.size());
        hiloHabla();
        hiloPasos();
        hiloGiroscopio();
        hiloTareas();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SPEECH: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> text = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    Log.d("ESCUCHE","_"+text.get(0));
                    desti=CI.Destino(text.get(0));
                    if(text.get(0).equalsIgnoreCase("Dónde estoy")){
                        hablo("Estas en la zona del "+ CI.getNodos().get(ant.getI()).getName(),espera);
                    }
                    else if(desti!=-1){
                        //NC.obtieneCamino(NC.getNodo(ant.getI()),desti); ACABO DE MODIFICAR
                        tareas.setText("");
                        LanzoHilos(desti);
                    }
                    else{
                        hablo("NO RECONOSCO MENSAJE",espera);
                    }
                }
                break;
            }

        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_HEADSETHOOK:
                if (action == KeyEvent.ACTION_UP){
                    EscuchoaTexto();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        hiloBeacons();
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

    }


}
