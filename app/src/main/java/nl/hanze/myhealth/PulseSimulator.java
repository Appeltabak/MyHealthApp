package nl.hanze.myhealth;

import java.util.Random;

/**
 * Created by Tim on 29-09-15.
 */
public class PulseSimulator{
    public int simValue() {
        int max = 100;
        int min = 60;
        Random rand = new Random();
        return rand.nextInt(max - min + 1) + min;
    }
}
