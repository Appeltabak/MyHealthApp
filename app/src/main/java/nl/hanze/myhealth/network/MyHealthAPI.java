package nl.hanze.myhealth.network;

import android.app.Activity;
import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

public class MyHealthAPI {



    final int LOGIN = 1;
    final static String HOST = "http://www.omdbapi.com/?t=test&y=&plot=short&r=json"; //i-share database API HOST-URL.
    static Context context;

    /**
     * @param username het gebruikersnaam dat ingevuld wordt.
     * @param password  het wachtwoord dat toegestuurd.
     * @param handler de activity waar het model naar toe wordt gestuurd.
     */
    public static void login(String username, String password, MyHealthHandler handler) {

        final MyHealthHandler mHandler = handler;
        //String url = MyHealthAPI.HOST+ "/getSensorBySensorId/2"; //API URL ingevuld en wel de andere zijde op sturen.
        //String url = "http://www.omdbapi.com/?t=test&y=&plot=short&r=json";
        String url = "http://89.188.21.190/entities/getEntityByEntityId/5";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, (String)null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        //Toffe classe om te decouplen.
                        //LinkedHashMap returnObject = new LinkedHashMap<>();
                        //System.out.println(response.toString());
                        mHandler.onResult( response); //Stuur het door naar de resultaat van de activiteit
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mHandler.onError(error.fillInStackTrace()); //stuur error door naar de activiteit <handler>.
                        error.printStackTrace();
                        error.getCause();
                        error.getClass();
                        // TODO Auto-generated method stub
                    }
                });

        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Access the RequestQueue through your singleton class.
        MyHealthClient.getInstance(handler.getContext()).addToRequestQueue(jsObjRequest);
    }
}
