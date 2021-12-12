package com.example.wheelchairtrials;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.toString();
    private boolean startRecording = false;
    private boolean toggle = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ToggleButton button = (ToggleButton) findViewById(R.id.button1);
        button.setOnCheckedChangeListener((compoundButton, on) -> {
            startRecording = on;
        });
        final Button resultButton = (Button) findViewById(R.id.button2);
        resultButton.setEnabled(toggle);
        button.setOnCheckedChangeListener(((compoundButton, off) -> {
            toggle = !toggle;
            resultButton.setEnabled(toggle);
        }));


        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor sensorAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
//        Sensor sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        sensorManager.registerListener(new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
//                TextView txtStatus = (TextView) findViewById(R.id.accData);
//                txtStatus.setText(event.values[0] + "\n" + event.values[1] + "\n" + event.values[2]);
//                if (startRecording) {
//                    saveAccelerationData(event);
//                }
                saveData(event);
            }
            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        }, sensorAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void showResults(View view) throws FileNotFoundException, ParseException {
        Intent intent = new Intent(this, Result.class);
        startActivity(intent);
    }
    private void saveData(SensorEvent event) {
        File dataFile = new File(getFilesDir().getAbsolutePath() + "/data.csv");
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
        long timestamp = System.currentTimeMillis() + (event.timestamp - SystemClock.elapsedRealtimeNanos()) / 1000000;
        String timeStamp = timeStampFormat.format(new Date(timestamp));
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(dataFile, true);
            OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream);
            writer.append(Arrays.toString(event.values)).append("\t").append(timeStamp).append("\n");
            writer.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}