package nl.hanze.myhealth;

/**
 * Created by Tim on 29-09-15.
 */
public class RunSimulator {
    ECGSimulator ecg;
    PulseSimulator pulse;
    BloodPressureSimulator bloodpressure;


    public String[][] getData(){
        runSim();
        String [][] data = {ecg.getValues(), pulse.getValues(), bloodpressure.getValues()};
        return data;
    }
    public void runSim(){
        ecg.simValue();
        pulse.simValue();
        bloodpressure.simValue();
    }
}
