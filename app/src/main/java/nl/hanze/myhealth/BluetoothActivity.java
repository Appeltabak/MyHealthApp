package nl.hanze.myhealth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.Set;


public class BluetoothActivity extends AppCompatActivity implements BluetoothHandler {
    private Bluetooth bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        bluetooth = new Bluetooth();
        bluetooth.init(this);
        ListView view = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item);
        view.setAdapter(adapter);
        Set<BluetoothDevice> devices = bluetooth.getBondedDevices();
        for(BluetoothDevice device : devices) {
            adapter.add(device.getName() + "\n" + device.getAddress());
        }
        final BluetoothHandler mHandler = this;
        view.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
                        // Get the device MAC address, which is the last 17 chars in the View
                        String info = ((TextView) v).getText().toString();
                        String address = info.substring(info.length() - 17);
                        bluetooth.connect(address, mHandler);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onError(Exception e) {
        Log.d("mah debug","An error occurred: " + e.getMessage());
    }

    @Override
    public void onConnect(BluetoothSocket client) {
        Log.d("mah debug","Client connected!");
        try {
            RunSimulator sim = new RunSimulator();
            HealthData healthData = sim.runSim();
            bluetooth.sendHealthData(healthData, client);
        } catch (IOException e) {
            Log.d("mah debug", "Unable to send object");
        }
    }
}
