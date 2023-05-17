package com.my.mw_vjux_sensors;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager;
    Sensor sensorLinAccel;
    SensorEventListener listener;
    TextView textMem;
    String[] jokes;

    StringBuilder stringBuilder = new StringBuilder();
    Random random;
    Runnable runnable;

    float[] sensorValues = new float[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        textMem = findViewById(R.id.text_mem);
        sensorLinAccel = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        jokes = new String[] {getString(R.string.vjux_you_became_a_programmer),
                            getString(R.string.vjux_a_dollar_is_worth_1_yuan),
                            getString(R.string.vjux_kolobok_hanged_himself),
                            getString(R.string.vjux_you_ordered_pizza),
                            getString(R.string.vjux_phone_is_formatted)};

        random = new Random();

        listener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION){
                    System.arraycopy(event.values, 0, sensorValues, 0, 3);
                    shake();
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        runnable = () -> isBlocked = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(listener, sensorLinAccel, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    boolean isUp = false;
    boolean isDown = false;
    long actionTime;
    boolean isBlocked = false;
    Handler handler = new Handler();

    void shake(){
        if (isBlocked){
            return;
        }
        if (sensorValues[1] < -3){
            isUp = true;
            if (isDown){
                if (System.currentTimeMillis() - actionTime < 300){
                    textMem.setText(String.valueOf(jokes[random.nextInt(jokes.length)]));
                    isUp = false;
                    isBlocked = true;
                    handler.postDelayed(runnable, 1500);
                }
            }
            isDown = false;
        } else if(sensorValues[1] > 3){
            isDown = true;
            if (isUp){
                if (System.currentTimeMillis() - actionTime < 300){
                    textMem.setText(String.valueOf(jokes[random.nextInt(jokes.length)]));
                    isDown = false;
                    isBlocked = true;
                    handler.postDelayed(runnable, 1500);
                }
            }
            isUp = false;
        }
        actionTime = System.currentTimeMillis();
//        sensorValues[1]
    }
}