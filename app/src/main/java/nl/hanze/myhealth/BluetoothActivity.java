package nl.hanze.myhealth;

import android.app.Activity;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;


public class BluetoothActivity extends AppCompatActivity implements BluetoothClientHandler {
    private Bluetooth bluetooth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        final Activity mActivity = this;
        final BluetoothClientHandler mHandler = this;

        final Bluetooth bluetooth = new Bluetooth();
        this.bluetooth = bluetooth;

        bluetooth.init(this);
        bluetooth.setName("MyHealth Monitor");

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetooth.enableDiscoverability(mActivity, 120);
                BluetoothServerThread server = new BluetoothServerThread(mHandler, bluetooth);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_bluetooth, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_CANCELED) {
            Toast.makeText(this, "An error occurred: The device is not discoverable!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onError(Exception e) {
        Toast.makeText(this, "An error occurred: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnect(BluetoothSocket client) {
        Toast.makeText(this, "Client connected!", Toast.LENGTH_LONG).show();
        try {
            bluetooth.send("Hello World!", client);
        } catch (IOException e) {
            Toast.makeText(this, "Unable to send message", Toast.LENGTH_LONG).show();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                Toast.makeText(this, "Exception: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
