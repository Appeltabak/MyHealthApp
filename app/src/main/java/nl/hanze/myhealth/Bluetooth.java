package nl.hanze.myhealth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ArrayAdapter;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Jeroen on 23-9-2015.
 */
public class Bluetooth {
    public static final String SERVICE_NAME = "MyHealth";
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
     * Scan for available bluetooth devices. Scan results are added to the provided ArrayAdapter
     * instance.
     * @param activity
     * @param adapter
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
     * @param activity
     * @param duration
     */
    public void enableDiscoverability(Activity activity, int duration) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, duration);
        activity.startActivity(discoverableIntent);
    }

    /**
     * Start listening for an incomming bluetooth connection. The provided handler should implement
     * event methods for handling bluetooth communication. This method should be invoked using a
     * separate thread!
     * @param handler
     */
    public void listen(BluetoothClientHandler handler) {
        BluetoothServerSocket server = null;

        try {
            // 0x0003 = RFCOMM UUID (http://www.bluecove.org/bluecove/apidocs/javax/bluetooth/UUID.html)
            server = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(SERVICE_NAME, UUID.fromString("0x0003"));
            BluetoothSocket client = server.accept();
            handler.onConnect(client);
        } catch (IOException e) {
            handler.onError(e);
        } finally {
            if(server != null) {
                try { server.close(); }
                catch(IOException e) {}
            }
        }
    }

    /**
     * Connect to the bluetooth device. This method should be invoked using a separate thread!
     * @param device
     */
    public void connect(BluetoothHandler handler, BluetoothDevice device) {
        mBluetoothAdapter.cancelDiscovery();
        BluetoothSocket socket = null;
        try {
            // 0x0003 = RFCOMM UUID (http://www.bluecove.org/bluecove/apidocs/javax/bluetooth/UUID.html)
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("0x0003"));
            handler.onConnect(socket);
        } catch (IOException e) {
            try { socket.close(); }
            catch (IOException e1) {}
            handler.onError(e);
        }
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
