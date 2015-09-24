package nl.hanze.myhealth.network;

import android.content.Context;

public interface MyHealthHandler {

    public void onResult(Object result);
    public void onError(Object error);
    public Context getContext();
}
