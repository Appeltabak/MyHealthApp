package nl.hanze.myhealth;

import java.util.Random;

/**
 * Created by Tim on 29-09-15.
 */
public class BloodPressureSimulator {
    int[] pressurevalues = new int[2];

    public int[] simValue() {
        //calc systolic
        int maxUpper = 140;
        int minUpper = 90;
        int systolicpressure = calcPressure(maxUpper, minUpper);

        //calc diastolic
        int maxLower = 90;
        int minLower = 60;
        int diastolicpressure = calcPressure(maxLower, minLower);

        pressurevalues[0] = systolicpressure;
        pressurevalues[1] = diastolicpressure;
        return pressurevalues;
    }

    public int calcPressure(int max, int min) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }
}
