package nl.hanze.myhealth.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import nl.hanze.myhealth.R;
import nl.hanze.myhealth.utils.Image_Util;

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
        String url = HOST + "api/user_verification/"+username+"/"+password;

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

    /**
     * UNTESTED METHOD. Sleeping time for now.
     * @param image
     * @throws IOException
     */
    public void upload_picture(File image) throws IOException {
        final MyHealthHandler mHandler = handler;
        final String bytes = Image_Util.imageToBytes(image);
        final String url = HOST + "api/upload_image"; //TODO Correct url.

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, new Response.Listener<JSONObject>() {
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
                }
                ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                // the POST parameters:
                params.put("files", bytes);
                params.put("user_id", "1");
                return params;
            }
        };

        queue.add(jsObjRequest);
    }
}
