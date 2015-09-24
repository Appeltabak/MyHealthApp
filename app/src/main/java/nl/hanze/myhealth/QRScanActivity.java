package nl.hanze.myhealth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRScanActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscan);

        if(Camera.mCurrentPhotoPath == null) {
            QRScan.scan(this, true, getString(R.string.qr_scan_prompt));
        } else { showImg(); }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qrscan, menu);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode == RESULT_CANCELED) { stop(); }

        else if(resultCode == RESULT_OK) {
            switch(requestCode) {
                case Camera.REQUEST_IMAGE_CAPTURE:
                    showImg();
                    break;
                default :
                    handleQRResult(requestCode, resultCode, intent);
                    break;
            }
        }
    }

    private void handleQRResult(int requestCode, int resultCode, Intent intent) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (result != null) {
            String contents = result.getContents();
            // TODO: Do something with the result!
            Camera.takePicture(this);
        } else {
            Toast.makeText(this, R.string.qr_no_result, Toast.LENGTH_LONG);
            stop();
        }
}

    private void showImg() {
        ImageView mImageView = (ImageView) findViewById(R.id.imageView);
        Bitmap image = Camera.getFullSizedPicture();
        mImageView.setImageBitmap(image);
    }

    private void stop() {
        Camera.destroyPicture();
        setResult(RESULT_CANCELED);
        finish();
    }
}
