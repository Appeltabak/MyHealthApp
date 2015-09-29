package nl.hanze.myhealth.network;

public interface MyHealthHandler {
    public void onResult(Object result);
    public void onError(Object error);
}
