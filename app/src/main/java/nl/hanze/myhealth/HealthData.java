package nl.hanze.myhealth;

/**
 * Created by timar on 29-09-15.
 */
public class HealthData {
    public int[] bloodpressure;
    public int[] ecg;
    public int pulse;

    public int[] getBloodPressure() {
        return bloodpressure;
    }

    public void setBloodPressure(int[] bloodpressure) {
        this.bloodpressure = bloodpressure;
    }

    public int[] getEcg() {
        return ecg;
    }

    public void setEcg(int[] ecg) {
        this.ecg = ecg;
    }


    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }
}
