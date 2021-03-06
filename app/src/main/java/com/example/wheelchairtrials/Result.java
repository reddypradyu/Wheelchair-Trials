package com.example.wheelchairtrials;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class Result extends AppCompatActivity {
    private static final String COMMA_DELIMITER = ",";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
//        GraphView graph = findViewById(R.id.idGraphView);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        graph.addSeries(series);
//        List<List<String>> records = new ArrayList<>();
//        try (BufferedReader br = new BufferedReader(new FileReader(getFilesDir().getAbsolutePath() + "/data.csv"))) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                String[] values = line.split(COMMA_DELIMITER);
//                records.add(Arrays.asList(values));
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        SimpleDateFormat[] timeStamps = new SimpleDateFormat[records.size()];
//        Double[] xVal = new Double[records.size()];
//        Double[] yVal = new Double[records.size()];
//        Double[] zVal = new Double[records.size()];
//        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy h:mm:ss", Locale.ENGLISH);
//        Date[] dateStamp = new Date[records.size()];
//        for (int i = 0; i < records.size(); i++) {
//            try {
//                dateStamp[i] = sdf.parse(records.get(i).get(0));
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            xVal[i] = Double.parseDouble(records.get(i).get(1));
//            yVal[i] = Double.parseDouble(records.get(i).get(2));
//            zVal[i] = Double.parseDouble(records.get(i).get(3));
//        }
////        System.out.println(Arrays.toString(xVal));
//        // get these from user input
//        double wheelDiam = 0.1412;
//        String wheel_side = "left";
//
//        double weightLoad = 100;
//        double sampFreq = 51.2;
//
//
//        // filter the relevant accelerations
//        smoothArray(xVal, 2);
//        smoothArray(yVal, 2);
//        smoothArray(zVal, 2);
//
//        //for finding magnitude
//        Double[] magnitude = new Double[xVal.length];
//        for (int i = 0; i < xVal.length; i++) {
//            Double mag = (xVal[i] * xVal[i]) + (yVal[i] * yVal[i]) + (zVal[i] * zVal[i]);
//            magnitude[i] = Math.sqrt(mag);
//        }
//
//        smoothArray(magnitude, 2);
    }
    // http://phrogz.net/js/framerate-independent-low-pass-filter.html
    public static void smoothArray(Double[] values, int smoothing) {
        Double value = values[0]; // start with the first input
        for (int i=1; i < values.length; i++) {
            Double currentValue = values[i];
            value += (currentValue - value) / smoothing;
            values[i] = value;
        }
    }
//    private double getMinScore() {
//        double minScore = Double.MAX_VALUE;
//        for (Double score : scores) {
//            minScore = Math.min(minScore, score);
//        }
//        return minScore;
//    }
//
//    private double getMaxScore() {
//        double maxScore = Double.MIN_VALUE;
//        for (Double score : scores) {
//            maxScore = Math.max(maxScore, score);
//        }
//        return maxScore;
//    }
}