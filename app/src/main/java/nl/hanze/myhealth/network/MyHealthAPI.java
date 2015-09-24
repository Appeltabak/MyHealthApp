package nl.hanze.myhealth.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MyHealthAPI {
    final int LOGIN = 1;
    final static String HOST = "https://myhealthweb.herokuapp.com/";

    private MyHealthHandler handler;
    private RequestQueue queue;

    public MyHealthAPI(Context applicationContext, MyHealthHandler handler) {
        queue = Volley.newRequestQueue(applicationContext);
        setHandler(handler);
    }

    public void setHandler(MyHealthHandler handler) {
        this.handler = handler;
    }

    /**
     * @param username het gebruikersnaam dat ingevuld wordt.
     * @param password  het wachtwoord dat toegestuurd wordt.
     */
    public void login(String username, String password) {
        String url = HOST + "api/users_verification/"+username+"/"+password;

        final MyHealthHandler mHandler = handler;
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse that Json.
                        mHandler.onResult(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mHandler.onError(error);
                    }
                });

        queue.add(jsObjRequest);
    }
}
