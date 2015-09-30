package nl.hanze.myhealth;

import android.bluetooth.BluetoothSocket;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;


public class BluetoothActivity extends AppCompatActivity implements BluetoothHandler {
    private Bluetooth bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        this.bluetooth = new Bluetooth();
        bluetooth.init(this);

        final BluetoothHandler mHandler = this;
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.listen(mHandler);
            }
        });
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
        Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnect(BluetoothSocket client) {
        Toast.makeText(this, "Client connected!", Toast.LENGTH_LONG).show();
        try {
            RunSimulator sim = new RunSimulator();
            bluetooth.sendHealthData(sim.runSim(), client);
        } catch (IOException e) {
            Toast.makeText(this, "Unable to send message", Toast.LENGTH_LONG).show();
        }
    }
}
