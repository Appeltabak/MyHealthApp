package nl.hanze.myhealth.network;

import android.util.Log;

import com.loopj.android.http.*;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;

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

        RequestParams params = new RequestParams();
        try{
            params.put("user_id",'1');
            params.put("file", uploadImage);
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }

        myClient.post("http://myhealthweb.herokuapp.com/api/upload_image", new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.e("UPLOAD", errorResponse);
                System.out.println(errorResponse);
                mHandler.onError(errorResponse);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                mHandler.onResult("Good happened");
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}