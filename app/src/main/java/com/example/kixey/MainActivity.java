package com.example.kixey;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.os.Handler;
import java.lang.Math;
import android.media.MediaPlayer;

public class MainActivity extends AppCompatActivity {

    private double a_abs = 0.0;
    private double a_max = 0.0;
    private final double a_border = 3.0;
    private boolean evaluateFlag = false;
    private TextView textView;
    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener eventListener;

    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.viewAcceleration);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mPlayer = MediaPlayer.create(this, R.raw.music);
        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlayer.stop();
            }
        });

        if(sensorManager!=null)
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        eventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(evaluateFlag) {
                    double a_x = event.values[0];
                    double a_y = event.values[1];
                    double a_z = event.values[2];
                    a_abs = Math.sqrt(Math.pow(a_x,2)+Math.pow(a_y,2)+Math.pow(a_z,2));
                    if(a_abs>a_border)
                    {
                        /** Запустить музыкальное сопровождение на 15 сек **/
                        mPlayer.start();
                    }
                    a_max = (a_max > a_abs) ? a_max : a_abs;
                    String acceleration = String.format("%.2f", a_max);
                    textView.setText(acceleration);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(eventListener, sensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void onClickOnOff(View view) {

        if(evaluateFlag) mPlayer.pause();
        evaluateFlag=!evaluateFlag;
        a_abs = 0.0;
        a_max = 0.0;
        textView = (TextView)findViewById(R.id.viewAcceleration);
        String acceleration = String.format("%.2f", a_max);
        textView.setText(acceleration);


        /** Менять цвет кнопки **/
    }

   /* public void stopPlay(){
        mPlayer.stop();
    }*/

}