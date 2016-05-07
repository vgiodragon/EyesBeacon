package com.giovanny.eyesbeacon;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import com.giovanny.eyesbeacon.Modelo.NodosC;
import com.giovanny.eyesbeacon.Modelo.Tareas;
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
    Tareas tareas;
    ArrayList<String> ZonasRuta;
    double angz;
    int pasi;
    boolean llego;
    int detente;
    String estrellas;
    TextView giro;
    TextView step;
    TextView beac;
    TextView tareas1;
    TextView tTareasRea;
    TextView tamdfk;
    Beacons beacons;
    private boolean espera;
    NodosC NC;
    BeaconZona ant, actual,siguiente;
    int desti;

    private final int code_request=1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, code_request);
        }
        beac = (TextView) findViewById(R.id.beacon);
        giro = (TextView) findViewById(R.id.giro);
        step = (TextView) findViewById(R.id.podometro);
        tareas1 = (TextView) findViewById(R.id.tareas);
        tTareasRea = (TextView) findViewById(R.id.tareasRea);
        tamdfk = (TextView) findViewById(R.id.trmdfk);
        estrellas="";
        tTareasRea.setText(estrellas);
        llego=false;
        detente=0;
        actual=null;
        CI= new CargaInformacion();
        NC = new NodosC(CI.getNodos());
        step.setText("_"+0);
        tareas = new Tareas();
        espera=false;
        ZonasRuta=new ArrayList<>();
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        podometro = new Podometro(sensorManager);
        giroscopio = new Giroscopio(sensorManager);
        beacons = new Beacons(this,this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case code_request:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    Toast.makeText(MainActivity.this, "COARSE LOCATION permitido", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "COARSE LOCATION no permitido", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    private synchronized void hablo(String text, boolean wait){
        if(detente==0) {
            Log.d("detente","_1"+detente);
            if (!espera)
                if (result == TextToSpeech.LANG_NOT_SUPPORTED || result == TextToSpeech.LANG_MISSING_DATA)
                    Toast.makeText(getApplicationContext(), "NO sorportado", Toast.LENGTH_SHORT).show();
                else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        ttsGreater21(text);
                    } else {
                        ttsUnder20(text);
                    }
                }
            espera = wait;
        }
        else{
            Log.d("detente","_2"+detente);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ttsGreater21("Detente");
            } else {
                ttsUnder20("Detente");
            }
        }
    }

    private synchronized void getZonasRuta(){
        ZonasRuta=NC.getZonasRuta();
    }

    private synchronized int getZonasRutaSize(){
        return ZonasRuta.size();
    }

    private synchronized void passZonasRuta(){
        if(ZonasRuta.size()>0)
            ZonasRuta.remove(0);
    }

    private void Restart(){
        //TareasARealizar.remove(0);
        tareas.Remove0();
        //if(!TareasARealizar.isEmpty()) {
        if(!tareas.isEmpty()) {
            //hablo(TareasARealizar.get(0), true);
            hablo(tareas.getTareasARealizar0(), true);
            giroscopio.restartAngZ();
            podometro.restartPasos();
        }
    }


    private void Tareaspe(){

        //String [] tareaActual = TareasARealizar.get(0).split(" ");
        String [] tareaActual = tareas.getTareasARealizar0().split(" ");
        if(tareaActual[0].equals("Dar")){
            int cant = Integer.parseInt(tareaActual[1]);
            if ((cant-1) < podometro.getPasos()) {
                estrellas += "*";
                tTareasRea.setText(estrellas);
                Restart();
            }

        }
        else if(tareaActual[0].equals("Escalera")){
            int cant = Integer.parseInt(tareaActual[4]);
            if((cant-1)<podometro.getPasos()){
                estrellas+="*";
                tTareasRea.setText(estrellas);
                Restart();
            }
        }

        else if(tareaActual[0].equals("Gira")){

            int ang=Integer.parseInt(tareaActual[1]);

            if(tareaActual[2].equals("D")){
                ang=(ang-15)*(-1);
                if(giroscopio.getTotalAngZ()<ang){
                    //Log.d("rea",giroscopio.getTotalAngZ()+"_"+ang);
                    estrellas+="*";
                    tTareasRea.setText(estrellas);
                    Restart();
                }
            }
            else {
                ang-=15;
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
        return res;
    }

    @SuppressWarnings("deprecation")
    private void ttsUnder20(String text) {
        HashMap<String, String> map = new HashMap<>();
        map.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "MessageId");
        ttsObject.speak(text, TextToSpeech.QUEUE_ADD, map);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void ttsGreater21(String text) {
        String utteranceId=this.hashCode() + "";
        ttsObject.speak(text, TextToSpeech.QUEUE_ADD, null, utteranceId);
    }

    private void hiloHabla() {
        new Thread() {
            public void run() {
                //while (!TareasARealizar.isEmpty()) {
                while (!tareas.isEmpty()) {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                hablo(tareas.getTareasARealizar0(), false);
                            }
                        });
                        Thread.sleep(3800);
                        Log.d("hablo", "segundo");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                if(llego) {
                    hablo("Has llegado a " + CI.getNodos().get(desti).getName(), false);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    hablo("Has llegado a "+CI.getNodos().get(desti).getName(),false);
                }
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
                                if (beacons.numDetec() > 0) {
                                    detectados = beacons.getDetectados();
                                    beacons.onStop();
                                    beac.setText(showBeacon());
                                    for (int d = 0; d < detectados.size(); d++) {
                                        siguiente = CI.isZona(detectados.get(d).getMAC());
                                        if (siguiente != null) {
                                            ant = actual;
                                            setActual(siguiente);
                                            Log.d("actual", actual.getMAC() + "_" + actual.getDescrip());
                                        }
                                    }
                                    if (getZonasRutaSize() > 0 && detectados.size() > 0 && tareas.getTareasSize()>0) {
                                        Beacon beac = detectados.get(0);

                                        if(!beac.getMAC().equals("74:DA:EA:B3:3A:B8")) {

                                            if(detente>0)
                                                detente--;

                                            if (beac.getRSSI() > -85) {

                                                if (!beac.getMAC().equals(ZonasRuta.get(0))) {
                                                    passZonasRuta();
                                                    if (getZonasRutaSize() > 0) {
                                                        if (!beac.getMAC().equals(ZonasRuta.get(0))) {
                                                            llego = false;
                                                            tareas.Restart();
                                                            hablo("SALISTE DEL CAMINO", espera);
                                                            Log.d("Restar", "Reiniciado ...");
                                                            try {
                                                                Thread.sleep(2000);
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                            hablo("Regenerando nuevo camino", espera);
                                                            try {
                                                                Thread.sleep(2000);
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                            hablo("El punto de control es " + CI.getPuntoControl(actual.getMAC()), espera);
                                                            try {
                                                                Thread.sleep(3000);
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }
                                                            LanzoHilos(desti);
                                                            try {
                                                                Thread.sleep(900);
                                                            } catch (InterruptedException e) {
                                                                e.printStackTrace();
                                                            }

                                                        }
                                                    }
                                                }

                                            }
                                        }
                                        else{   detente=2;
                                                hablo("DETENTE",espera);
                                            }

                                    }
                                    beacons.onStart();
                                    beacons.detectadosReset();
                                }

                            }
                        });
                        Thread.sleep(750);
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
                //while (!TareasARealizar.isEmpty()) {
                while (!tareas.isEmpty()) {
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
                Log.d("Restar", "Fin Pasos");
            }
        }.start();
    }

    private void hiloGiroscopio() {
        new Thread() {
            public void run() {

                //while (!TareasARealizar.isEmpty()) {
                while (!tareas.isEmpty()) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            angz=giroscopio.getTotalAngZ();
                            giro.setText(String.format("%f",angz));
                        }
                    });

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Log.d("Restar", "Fin Giroscopio");
            }
        }.start();
    }

    private void hiloTareas() {
        new Thread() {
            public void run() {

                //while (!TareasARealizar.isEmpty()) {
                while (!tareas.isEmpty()) {
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            //tamdfk.setText(TareasARealizar.get(0));
                            tamdfk.setText(tareas.getTareasARealizar0());
                            Tareaspe();
                        }
                    });

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                Log.d("Restar","Fin hiloTareas");
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
        giroscopio.restartAngZ();
        podometro.restartPasos();
        NC = new NodosC(CI.getNodos());
        NC.IniciaBusqueda(NC.getNodo(getIActual()), res);
        //TareasARealizar=NC.getTarea();
        tareas.setTareasARealizar(NC.getTarea());
        tareas1.setText(NC.tareaspe());
        getZonasRuta();
        String mues="";
        for(int i=0;i<ZonasRuta.size();i++){
            mues+=ZonasRuta.get(i)+"__";
        }
        Log.d("zonas","_"+mues);
        llego=true;
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
                        //Log.d("ESCUCHE2","_"+ant.getI()+"_");
                        hablo("Estas en la zona del " + CI.getNodos().get(ant.getI()).getName(),espera);
                    }
                    else if(desti!=-1){
                        //NC.obtieneCamino(NC.getNodo(ant.getI()),desti); ACABO DE MODIFICAR
                        tareas1.setText("");
                        LanzoHilos(desti);
                    }
                    else{
                        hablo("NO RECONOZCO MENSAJE",espera);
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