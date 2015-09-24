package nl.hanze.myhealth;

import android.os.AsyncTask;

/**
 * Created by Jeroen on 24-9-2015.
 */
public class BluetoothServerTask extends AsyncTask<Bluetooth, Void, Void> {
    private BluetoothClientHandler handler;

    public void setBluetoothHandler(BluetoothClientHandler handler){
        this.handler = handler;
    }

    @Override
    protected Void doInBackground(Bluetooth... bluetooth) {
        if(handler != null) {
            bluetooth[0].listen(handler);
        }

        return null;
    }
}
