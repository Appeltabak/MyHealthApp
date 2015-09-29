package nl.hanze.myhealth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import nl.hanze.myhealth.network.MyHealthAPI;
import nl.hanze.myhealth.network.MyHealthHandler;

import static nl.hanze.myhealth.utils.sha512.generateHash;

public class LoginActivity extends Activity implements MyHealthHandler {
    private MyHealthAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        api = new MyHealthAPI(getApplicationContext(), this);

        final EditText usernameBox = (EditText) findViewById(R.id.usernameBox);
        final EditText passwordBox = (EditText) findViewById(R.id.passwordBox);
        final Button loginButton = (Button) findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyLogin(usernameBox.getText().toString(), passwordBox.getText().toString());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void verifyLogin(String username, String password) {
        if(!password.isEmpty()) {
            //password = generateHash(password);
            //Toast.makeText(this,password, Toast.LENGTH_LONG).show();
            api.login(username, password);
        }
    }

    @Override
    public void onResult(Object result) {
        //Go to mainmenu. (new intent).
        Toast.makeText(this, "Success!", Toast.LENGTH_LONG).show();

        Intent i = new Intent(LoginActivity.this, MainMenu.class);
        //Intent i = new Intent(Splashscreen.this, MainMenu.class);
        startActivity(i);
    }

    @Override
    public void onError(Object error) {
        Toast.makeText(this, "Failure: " + error.toString(), Toast.LENGTH_LONG).show();
    }
}
