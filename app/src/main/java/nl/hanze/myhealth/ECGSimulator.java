package nl.hanze.myhealth;

import java.io.ByteArrayInputStream;

/**
 * Created by Tim on 29-09-15.
 */
public class ECGSimulator {
    String[] threewaves = new String[21];

    public String[] simValue() {
        byte[] templateECG = new byte[]{0, 1, 2, 4, 6, 9, 12, 13, 14, 15, 14, 13, 12, 9, 6, 4, 2, 1, 0, 0, 0};
        ByteArrayInputStream bais = new ByteArrayInputStream(templateECG);

        int c;
        for (int i = 0; i < 2; i++) {
            int count = 0;
            while ((c = bais.read()) != -1) {
                threewaves[count] = String.valueOf(c);
                count++;
            }
        }
        return threewaves;
    }
}
