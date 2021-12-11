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
    private static final String COMMA_DELIMITER = ",";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ToggleButton button = (ToggleButton) findViewById(R.id.button1);
        button.setOnCheckedChangeListener((compoundButton, on) -> {
            startRecording = on;
        });
        final Button resultButton = (Button) findViewById(R.id.button2);
        resultButton.setClickable(false);
        button.setOnCheckedChangeListener(((compoundButton, off) -> {
            resultButton.setClickable(true);
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

    // http://phrogz.net/js/framerate-independent-low-pass-filter.html
    public static void smoothArray(Double[] values, int smoothing) {
        Double value = values[0]; // start with the first input
        for (int i=1; i < values.length; i++){
            Double currentValue = values[i];
            value += (currentValue - value) / smoothing;
            values[i] = value;
        }
    }

    public void showResults(View view) throws FileNotFoundException, ParseException {
        Intent intent = new Intent(this, Result.class);
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(getFilesDir().getAbsolutePath() + "/data.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        SimpleDateFormat[] timeStamps = new SimpleDateFormat[records.size()];
        Double[] xVal = new Double[records.size()];
        Double[] yVal = new Double[records.size()];
        Double[] zVal = new Double[records.size()];
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy h:mm:ss", Locale.ENGLISH);
        Date[] dateStamp = new Date[records.size()];
        for (int i = 0; i < records.size(); i++) {
            dateStamp[i] = sdf.parse(records.get(i).get(0));
            xVal[i] = Double.parseDouble(records.get(i).get(1));
            yVal[i] = Double.parseDouble(records.get(i).get(2));
            zVal[i] = Double.parseDouble(records.get(i).get(3));
        }
//        System.out.println(Arrays.toString(xVal));
        // get these from user input
        double wheelDiam = 0.1412;
        String wheel_side = "left";

        double weightLoad = 100;
        double sampFreq = 51.2;


        // filter the relevant accelerations
        smoothArray(xVal, 2);
        smoothArray(yVal, 2);
        smoothArray(zVal, 2);

        //for finding magnitude
        Double[] magnitude = new Double[xVal.length];
        for (int i = 0; i < xVal.length; i++) {
            Double mag = (xVal[i] * xVal[i]) + (yVal[i] * yVal[i]) + (zVal[i] * zVal[i]);
            magnitude[i] = Math.sqrt(mag);
        }

        smoothArray(magnitude, 2);
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