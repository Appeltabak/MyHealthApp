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
    private BluetoothServerSocket server;

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
        stopServer();

        if(writer != null) { writer.close(); }
        if(reader != null) {
            try { reader.close(); }
            catch (IOException e) {}
        }
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
     * Set the friendly Bluetooth name of the local Bluetooth adapter.
     * @param name
     */
    public void setName(String name) {
        mBluetoothAdapter.setName(name);
    }

    /**
     * Start listening for an incomming bluetooth connection. The provided handler should implement
     * event methods for handling bluetooth communication. This method should be invoked using a
     * separate thread!
     * @param handler
     */
    public void listen(BluetoothClientHandler handler) {
        // Make sure only one BluetoothServerSocket is running.
        stopServer();

        try {
            // UUID as seen in the Android Bluetooth Chat example.
            server = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(SERVICE_NAME, UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66"));
            BluetoothSocket client = server.accept();
            handler.onConnect(client);
        } catch (Exception e) {
            handler.onError(e);
        } finally {
            stopServer();
        }
    }

    /**
     * Stop listening for incomming connections.
     */
    public void stopServer() {
        if(server == null) { return; }

        try { server.close(); }
        catch(IOException e) {}
        server = null;
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
