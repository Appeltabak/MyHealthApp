package nl.hanze.myhealth;

import android.app.Activity;

import com.google.zxing.integration.android.IntentIntegrator;

public class QRScan {
    static final int REQUEST_IMAGE_CAPTURE = -1;

    public static void scan(Activity activity, boolean beep, String prompt) {
        IntentIntegrator integrator = new IntentIntegrator(activity);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setBeepEnabled(beep);

        if(prompt != null) { integrator.setPrompt(prompt); }

        integrator.initiateScan();
    }

    public static void scan(Activity activity, boolean beep) {
        QRScan.scan(activity, beep, null);
    }
}
