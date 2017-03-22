package com.example.filipe.pacman;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;




public class Main extends Activity implements SensorEventListener {

    private MediaPlayer player;   //
    public SensorManager sensorManager;
    public Sensor accelerometer;
    public float alpha = 0.9f,
            lastX, lastY, lastZ,
            lowPassX, lowPassY, lowPassZ,
            highPassX, highPassY, highPassZ,
            calibX, calibY, calibZ;

    public float zeroX = 0f, zeroY = 0f, zeroZ = 0f;
    public static int usableX, usableY;



    public long lastUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(new GameView(this));
        player = MediaPlayer.create(this, R.raw.metal);



        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        for (Sensor sensor : sensorManager.getSensorList(Sensor.TYPE_ALL)) {
            System.out.println(sensor.getName());
        }

        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        lastUpdate = System.currentTimeMillis();
    }



    public void onStart() {
        super.onStart();
        Log.d("testing", "onStart got called");
    }

    public void onStop() {
        super.onStop();
        Log.d("testing", "onStop got called");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.d("testing", "onDestroy got called");
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL);

        if (player != null) {
            player.setLooping(true);
            player.start();
        }
        Log.d("testing", "onResume got called");
    }

    @Override
    protected void onPause() {
        sensorManager.unregisterListener(this);
        super.onPause();
        player.stop();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long actualTime = System.currentTimeMillis();
            if (actualTime - lastUpdate > 500) {
                lastUpdate = actualTime;

                float x = event.values[0], y = event.values[1], z = event.values[2];


                lowPassX = lowPass(x, lowPassX);
                lowPassY = lowPass(y, lowPassY);
                lowPassZ = lowPass(z, lowPassZ);


                highPassX = highPass(x, lastX, highPassX);
                highPassY = highPass(y, lastY, highPassY);
                highPassZ = highPass(z, lastZ, highPassZ);

                calibX = zeroX - x;
                calibY = zeroY - y;
                calibZ = zeroZ - z;


                lastX = x;
                lastY = y;
                lastZ = z;

                usableX= Math.round(highPassX);
                usableY= Math.round(highPassY);
                Global.globalX=usableX;
                Global.globalY=usableY;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private float highPass(float current, float last, float filtered) {
        return alpha * (filtered + current - last);
    }

    private float lowPass(float current, float filtered) {
        return alpha * current + (1.0f - alpha) * filtered;
    }

    public void onCalib(View view) {
        zeroX = lastX;
        zeroY = lastY;
        zeroZ = lastZ;

    }

}
