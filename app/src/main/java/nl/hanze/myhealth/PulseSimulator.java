package nl.hanze.myhealth;

import java.util.Random;

/**
 * Created by Tim on 29-09-15.
 */
public class PulseSimulator {

    public String simValue() {
        return calcPulse(100, 60);
    }

    private String calcPulse(int max, int min) {
        Random rand = new Random();
        return String.valueOf(rand.nextInt((max - min) + 1) + min);
    }
}
