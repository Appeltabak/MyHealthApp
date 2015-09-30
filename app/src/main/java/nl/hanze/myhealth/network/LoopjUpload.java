package nl.hanze.myhealth.network;

import android.util.Log;

import com.loopj.android.http.*;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.conn.ConnectTimeoutException;

public class LoopjUpload {

    private MyHealthHandler handler;
    private static final String TAG = "UPLOAD";


    public LoopjUpload(MyHealthHandler handler){
        setHandler(handler);
    }

    public void setHandler(MyHealthHandler handler) {
        this.handler = handler;
    }

    public void uploadFile(File uploadImage) {
        setHandler(handler);
        final MyHealthHandler mHandler = handler;
        AsyncHttpClient myClient = new AsyncHttpClient();
        myClient.setTimeout(60 * 1000);
        myClient.setMaxRetriesAndTimeout(2, 2);

        RequestParams params = new RequestParams();
            params.put("user_id",'1');
        try {
            params.put("file", uploadImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        myClient.post("https://myhealthweb.herokuapp.com/api/upload_image", params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {

                if ( e.getCause() instanceof ConnectTimeoutException) {
                    Log.e("CONNECTION TIMEOUT", errorResponse);
                }
                    System.out.println("Connection timeout !");
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e("UPLOAD", errorResponse);
                System.out.println(errorResponse);
                mHandler.onError(errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                mHandler.onResult(responseString);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });



    }
}