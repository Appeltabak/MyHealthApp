package nl.hanze.myhealth;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import nl.hanze.myhealth.network.SingletonNetworkAdapter;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        final EditText usernameBox = (EditText) findViewById(R.id.usernameBox);
        final EditText passwordBox = (EditText) findViewById(R.id.passwordBox);
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(LoginActivity.this, "Username: " + usernameBox.getText().toString() + "\n Password: " + passwordBox.getText().toString(), Toast.LENGTH_LONG).show();
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

    public boolean verifyLogin(String username, String password) {
        buildJsonString(username, password);
        return true;
    }

    public String buildJsonString(String username, String password) {

        //String url = "http://myhealthweb.herokuapp.com/";
        final TextView mTxtDisplay = (TextView) findViewById(R.id.jsonresponsetest);

        String url = "http://89.188.21.190/sensors/getSensorBySensorId/2"; //API URL ingevuld en wel de andere zijde op sturen.

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        //DO AWESOME SHIT.
                        if(response.has("entityId")){
                            try {
                                mTxtDisplay.setText("ID: " + response.get("entityId")); //verwacht nummer 1
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTxtDisplay.setText("Something went wrong, please try again later."); //verwacht nummer 1
                        error.printStackTrace();
                        // TODO Auto-generated method stub

                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Access the RequestQueue through your singleton class.
        SingletonNetworkAdapter.getInstance(this).addToRequestQueue(jsObjRequest);
        return "";
    }
}
