package com.giovanny.eyesbeacon;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.giovanny.eyesbeacon.Modelo.CargaInformacion;
import com.giovanny.eyesbeacon.Modelo.Nodo;

import java.util.ArrayList;

/**
 * Created by giovanny on 03/01/16.
 *
 * know more of Views : http://www.vogella.com/tutorials/AndroidCustomViews/article.html
 *
 */
public class MapaView extends View {

    private Paint paint;
    private CargaInformacion CI;
    private float Px,Py;

    public MapaView(Context context) {
        super(context);
        iniciar();
    }
    public MapaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        iniciar();
    }

    public void iniciar(){
        paint = new Paint();
        Log.d("Mapa", "inicio");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        createCI();
    }

    public void setCI(CargaInformacion CI){
        this.CI =CI;
    }

    public CargaInformacion getCI(){
        return CI;
    }

    public void createCI(){
        Log.d("Mapa","cre:"+getWidth()+"_"+getHeight());
        CI = new CargaInformacion(getWidth(),getHeight(),getHeight()/3-50,getHeight()/3-50);
        Px = 1.f*getWidth()/10;
        Py = 7.f*(getHeight())/8;
    }

    public synchronized void setPosition(float [] Ps){
        Px=Ps[0];
        Py=Ps[1];
    }

    public synchronized float [] getPosition(){
        return new float[]{Px,Py};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xFF33B5E5);
        paint.setStrokeWidth(4);

        ArrayList<Nodo> n = CI.getNodos();
        for(int i=0;i<n.size()-1;i++){
            canvas.drawCircle(n.get(i).getCx(), n.get(i).getCy(),3.f,paint);
            canvas.drawLine(n.get(i).getCx(), n.get(i).getCy(),
                    n.get(i+1).getCx(), n.get(i+1).getCy(), paint);
        }
        paint.setColor(0xFF4433E5);
        paint.setStrokeWidth(8);
        for(int i=0;i<n.size();i++) {
            canvas.drawCircle(n.get(i).getCx(), n.get(i).getCy(), 6.f, paint);
        }

        paint.setColor(0xFFEE9911);
        paint.setStrokeWidth(10);
        //Draw Position
        float []ps =getPosition();
        canvas.drawCircle(ps[0], ps[1], 15.f, paint);

        invalidate();//Para actualizar la vista durante running
    }


}
