package nl.hanze.myhealth;

import java.io.ByteArrayInputStream;

/**
 * Created by Tim on 29-09-15.
 */
public class ECGSimulator{
    int[] threewaves = new int[21];

    public int[] simValue() {
        int[] templateECG = new int[]{0, 1, 2, 4, 6, 9, 12, 13, 14, 15, 14, 13, 12, 9, 6, 4, 2, 1, 0, 0, 0};

        int c;
        for (int i = 0; i < 2; i++) {
            int count = 0;
            for (int x = 0; x < templateECG.length; x++ ){
                threewaves[x] = templateECG[x];
            }
        }
        return threewaves;
    }
}
