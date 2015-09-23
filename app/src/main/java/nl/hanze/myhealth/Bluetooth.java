package nl.hanze.myhealth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;

import java.util.Set;

/**
 * Created by Jeroen on 23-9-2015.
 */
public class Bluetooth {
    public static final int REQUEST_ENABLE_BT = 255;

    private static BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mReceiver;

    /**
     * Sets up the Bluetooth object. Invocation of this method is mandatory before using
     * other methods provided by the Bluetooth class.
     * @param activity
     */
    public void init(Activity activity) {
        if(!isSupported()) { return; }
        enable(activity);
    }

    /**
     * It's important to execute the stop method before switching to a different Activity!
     * @param activity
     */
    public void stop(Activity activity) {
        activity.unregisterReceiver(mReceiver);
        mBluetoothAdapter.cancelDiscovery();
    }

    /**
     * Scan for available bluetooth devices.
     */
    public void scan(Activity activity, ArrayAdapter adapter) {
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        for(BluetoothDevice device : devices) {
            adapter.add(device);
        }
        startReceiver(activity, adapter);
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * Enable other Bluetooth devices to discover this device. Set the duration to 0 to make
     * the device always discoverable or -1 to use the default 120 seconds.
     */
    public void enableDiscoverability(Activity activity, int duration) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
        activity.startActivity(discoverableIntent);
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

    /**
     * Checks Bluetooth support.
     * @return isSupported
     */
    private static boolean isSupported() {
        if (mBluetoothAdapter == null) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (mBluetoothAdapter == null) return false;
        }
        return true;
    }

    /**
     * Attempts to enable Bluetooth on behalf of the provided activity if necessary.
     */
    private static void enable(Activity activity) {
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }
    }

    /**
     * Starts scanning for new Bluetooth devices.
     * @param activity
     * @param adapter
     */
    private void startReceiver(Activity activity, ArrayAdapter adapter) {
        final ArrayAdapter mAdapter = adapter;

        // Create a BroadcastReceiver for ACTION_FOUND
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Add the name and address to an array adapter to show in a ListView
                    mAdapter.add(device.getName() + "\n" + device.getAddress());
                }
            }
        };

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }
}
