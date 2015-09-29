package nl.hanze.myhealth;

/**
 * Created by Tim on 29-09-15.
 */
public class RunSimulator {
    ECGSimulator ecg;
    PulseSimulator pulse;
    BloodPressureSimulator bloodpressure;


    public HealthData runSim(){
        HealthData healthdata = new HealthData();
        healthdata.setEcg(ecg.simValue());
        healthdata.setPulse(pulse.simValue());
        healthdata.setBloodPressure(bloodpressure.simValue());
        return healthdata;
    }
}
