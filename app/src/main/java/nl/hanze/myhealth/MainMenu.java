package nl.hanze.myhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainMenu extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        //extra comments.
        return true;
    }

    /**
     * Onclick from the main menu to the Activity of a camera.
     * Result is the user is redirected to a urine sample photo menu.
     * @param view
     */
    public void onClickSendUrineSample(View view){
        Intent intent = new Intent(this, PhotoIntentActivity.class);
        startActivity(intent);
    }

    /**
     * Onclick from the main menu to the Activity of a camera.
     * Result is the user is redirected to a urine sample photo menu.
     * @param view
     */
    public void onClickSendUrineSampleStrip(View view){
        Intent intent = new Intent(this, QRScanActivity.class);
        startActivity(intent);
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
}

