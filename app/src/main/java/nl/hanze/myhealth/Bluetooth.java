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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
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
    private PrintWriter writer;
    private BufferedReader reader;

    /**
     * Sets up the Bluetooth object. Invocation of this method is mandatory before using
     * other methods provided by the Bluetooth class. The result of enabling bluetooth
     * can be handled by the activities onActivityResult() method using
     * requestCode == Bluetooth.REQUEST_ENABLE_BT .
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

        if(writer != null) { writer.close(); }
        if(reader != null) {
            try { reader.close(); }
            catch (IOException e) {}
        }
    }

    /**
     * Scan for available bluetooth devices. The handler's onDeviceFound method is invoked
     * each time a new device is found during the scan operation.
     * instance.
     * @param activity
     * @param handler
     */
    public void scan(Activity activity, BluetoothHandler handler) {
        startReceiver(activity, handler);
        mBluetoothAdapter.startDiscovery();
    }

    /**
     * Cancels the scan operation if any.
     * @return success
     */
    public boolean cancelScan() {
        return mBluetoothAdapter.cancelDiscovery();
    }

    /**
     * Get a set of known/paired bluetooth devices.
     * @return devices
     */
    public Set<BluetoothDevice> getPairedDevices() {
        return mBluetoothAdapter.getBondedDevices();
    }

    /**
     * Enable other Bluetooth devices to discover this device. Set the duration to 0 to make
     * the device always discoverable or -1 to use the default 120 seconds. Whether the user
     * choose "Yes" or "No" in the dialog can be handled in the activities onActivityResult() method.
     * The result code is equal to the duration that the device is discoverable. If the user
     * responded "No" or if an error occurred, the result code will be RESULT_CANCELED.
     *
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
            // UUID as seen in the Android Bluetooth Chat example.
            server = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(SERVICE_NAME, UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66"));
            BluetoothSocket client = server.accept();
            handler.onConnect(client);
        } catch (Exception e) {
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
            // UUID as seen in the Android Bluetooth Chat example.
            socket = device.createRfcommSocketToServiceRecord(UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66"));
            handler.onConnect(socket);
        } catch (Exception e) {
            try { socket.close(); }
            catch (IOException e1) {}
            handler.onError(e);
        }
    }

    /**
     * Send a message to the connected bluetooth device.
     * @param msg
     * @param socket
     */
    public void send(String msg, BluetoothSocket socket) throws IOException {
        if(writer == null) {
            writer = new PrintWriter(socket.getOutputStream());
        }
        writer.println(msg);
        writer.flush();
    }

    /**
     * Reads one line of data received from the connected bluetooth device.
     */
    public String readLine(BluetoothSocket socket) throws IOException {
        if(reader == null) {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        return reader.readLine();
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
     * @param handler
     */
    private void startReceiver(Activity activity, BluetoothHandler handler) {
        final BluetoothHandler mHandler = handler;

        // Create a BroadcastReceiver for ACTION_FOUND
        mReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                // When discovery finds a device
                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    // Get the BluetoothDevice object from the Intent
                    BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                    // Notify the handler of the new device
                    mHandler.onDeviceFound(device);
                }
            }
        };

        // Register the BroadcastReceiver
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        activity.registerReceiver(mReceiver, filter); // Don't forget to unregister during onDestroy
    }
}
