package nl.hanze.myhealth;

import android.bluetooth.BluetoothSocket;

/**
 * Created by Jeroen on 23-9-2015.
 */
public interface BluetoothHandler {
    public void onError(Exception e); // Handle error event
    public void onConnect(BluetoothSocket client); // Handle connect event
}
