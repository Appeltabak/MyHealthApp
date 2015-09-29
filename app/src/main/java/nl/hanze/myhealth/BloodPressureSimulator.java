package nl.hanze.myhealth;

import java.util.Random;

/**
 * Created by Tim on 29-09-15.
 */
public class BloodPressureSimulator {
    String[] pressurevalues = new String[2];

    public String[] simValue() {
        //calc systolic
        int maxUpper = 140;
        int minUpper = 90;
        String systolicpressure = calcPressure(maxUpper, minUpper);

        //calc diastolic
        int maxLower = 90;
        int minLower = 60;
        String diastolicpressure = calcPressure(maxLower, minLower);
        pressurevalues[0] = systolicpressure;
        pressurevalues[1] = diastolicpressure;
        return pressurevalues;
    }

    public String calcPressure(int max, int min) {
        Random rand = new Random();
        return String.valueOf(rand.nextInt((max - min) + 1) + min);
    }
}
