package nl.hanze.myhealth;

import android.app.Activity;

import com.google.zxing.integration.android.IntentIntegrator;

public class QRScan {
    public static void scan(Activity context, boolean beep, String prompt) {
        IntentIntegrator integrator = new IntentIntegrator(context);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setBeepEnabled(beep);

        if(prompt != null) { integrator.setPrompt(prompt); }

        integrator.initiateScan();
    }

    public static void scan(Activity context, boolean beep) {
        QRScan.scan(context, beep, null);
    }
}
