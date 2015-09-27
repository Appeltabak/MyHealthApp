package nl.hanze.myhealth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

/**
 * Created by Jeroen on 23-9-2015.
 */
public interface BluetoothHandler {
    public void onError(Exception e); // Handle error event
    public void onConnect(BluetoothSocket client); // Handle connect event
    public void onDeviceFound(BluetoothDevice device); // Handle device found event
}
