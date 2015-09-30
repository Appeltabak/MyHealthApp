package nl.hanze.myhealth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Jeroen on 28-9-2015.
 */
public class Bluetooth {
    public static final String SERVICE_NAME = "MyHealth";
    public static final java.util.UUID UUID = java.util.UUID.fromString("fa87c0d0-afac-11de-8a39-0800200c9a66");
    public static final int REQUEST_ENABLE_BT = 1;

    private BluetoothAdapter mBluetoothAdapter;
    private AcceptThread acceptThread;
    private ConnectThread connectThread;

    /**
     * Initiate bluetooth.
     * @param activity
     */
    public void init(Activity activity) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter != null) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }
    }

    /**
     * Get a set of paired bluetooth devices.
     * @return devices
     */
    public Set<BluetoothDevice> getBondedDevices() {
        return mBluetoothAdapter.getBondedDevices();
    }

    /**
     * Connect with the specified bluetooth device.
     * @param device
     * @param handler
     */
    public void connect(BluetoothDevice device, BluetoothHandler handler) {
        ConnectThread thread = new ConnectThread(device, handler);
        thread.start();
    }

    /**
     * Connect with a bluetooth device with the specified MAC address.
     * @param address
     * @param handler
     */
    public void connect(String address, BluetoothHandler handler) {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        connect(device, handler);
    }

    /**
     * Start listening for incomming bluetooth connections. This method only accepts one
     * connection after which the device stops listening for connections.
     * @param handler
     */
    public void listen(BluetoothHandler handler) {
        AcceptThread thread = new AcceptThread(handler);
        thread.start();
    }

    /**
     * Sends a message.
     * @param msg
     * @param socket
     */
    public void sendLine(String msg, BluetoothSocket socket) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.println(msg);
        writer.flush();
    }

    /**
     * Reads a message.
     * @param socket
     * @return
     * @throws IOException
     */
    public String readLine(BluetoothSocket socket) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        return reader.readLine();
    }

    /**
     * Sends an Object
     * @param healthdata
     * @param socket
     * @throws IOException
     */
    public void sendHealthData(HealthData healthdata, BluetoothSocket socket) throws IOException{
        ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
        out.writeObject(healthdata);
    }

    /**
     * @param socket
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public HealthData readHealthData(BluetoothSocket socket) throws IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
        return (HealthData)in.readObject();
    }

    /**
     * The thread responsible for handling a client side bluetooth connection.
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket mmSocket;
        private BluetoothHandler handler;

        public ConnectThread(BluetoothDevice device, BluetoothHandler handler) {
            connectThread = this;
            this.handler = handler;
            try {
                mmSocket = device.createRfcommSocketToServiceRecord(UUID);
            } catch (IOException e) {
                handler.onError(e);
            }
        }

        public void run() {
            mBluetoothAdapter.cancelDiscovery();

            try {
                mmSocket.connect();
                handler.onConnect(mmSocket);
            } catch (IOException connectException) {
                handler.onError(connectException);
            } finally {
                try { mmSocket.close(); }
                catch (IOException closeException) {}
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) { }
        }
    }

    /**
     * The thread responsible for handling a server side bluetooth connection. This
     * thread listens for incomming connections and invokes the onConnect() method
     * on the handler instance.
     */
    private class AcceptThread extends Thread {
        private BluetoothServerSocket mmServerSocket;
        private BluetoothHandler handler;

        public AcceptThread(BluetoothHandler handler) {
            acceptThread = this;
            this.handler = handler;
            try {
                mmServerSocket = mBluetoothAdapter.listenUsingRfcommWithServiceRecord(SERVICE_NAME, UUID);
            } catch (IOException e) {
                handler.onError(e);
            }
        }

        public void run() {
            BluetoothSocket socket = null;

            while (true) {
                try {
                    socket = mmServerSocket.accept();
                } catch (IOException e) {
                    break;
                }
                if (socket != null) {
                    handler.onConnect(socket);
                    try { mmServerSocket.close(); }
                    catch (IOException e) {}
                    break;
                }
            }
        }

        public void cancel() {
            try {
                mmServerSocket.close();
            } catch (IOException e) {}
        }
    }
}

