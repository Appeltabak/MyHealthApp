package nl.hanze.myhealth;

import android.os.AsyncTask;

/**
 * Created by Jeroen on 24-9-2015.
 */
public class BluetoothServerThread extends Thread {
    private BluetoothClientHandler handler;
    private Bluetooth bluetooth;

    public BluetoothServerThread(BluetoothClientHandler handler, Bluetooth bluetooth){
        this.handler = handler;
        this.bluetooth = bluetooth;
    }

    @Override
    public void run() {
        if(handler != null) {
            bluetooth.listen(handler);
        }
    }
}
