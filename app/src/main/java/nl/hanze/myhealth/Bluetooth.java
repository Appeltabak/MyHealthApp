package nl.hanze.myhealth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;

/**
 * Created by Jeroen on 23-9-2015.
 */
public class Bluetooth {
    public static final int REQUEST_ENABLE_BT = 255;

    public static void init(Activity activity) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) return;

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     * Scan for available bluetooth devices.
     */
    public void scan() {

    }

    /**
     * Start listening for an incomming bluetooth connection.
     */
    public void listen() {

    }

    /**
     * Connect to the bluetooth device.
     */
    public void connect() {

    }

    /**
     * Terminate the bluetooth connection.
     */
    public void disconnect() {

    }

    /**
     * Send a message to the connected bluetooth device.
     * @param msg
     */
    public void send(String msg) {

    }

    /**
     * Reads one line of data received from the connected bluetooth device.
     */
    public String readLine() {
        return null;
    }

}
