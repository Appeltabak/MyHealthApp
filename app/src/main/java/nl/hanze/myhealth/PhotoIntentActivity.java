package nl.hanze.myhealth;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import android.view.View;

public class PhotoIntentActivity extends AppCompatActivity {

    private static final int ACTION_TAKE_PHOTO = 1;
    private static final int PICK_IMAGE = 2;

    private Button takeAPicture;
    private Button pickGallery;
    private Button sendButton;

    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_intent);

        mImageView = (ImageView) findViewById(R.id.imageView1);

        takeAPicture = (Button) findViewById(R.id.TakePicture);
        pickGallery = (Button) findViewById(R.id.load_gallery);
        sendButton = (Button) findViewById(R.id.send_button);

        pickGallery.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableShowButton();//disables button and show send button.
                dispatchTakePictureFromGallery();
            }
        });

        takeAPicture.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    disableShowButton(); //disables button and shows the send button.
                    TakePicture(); //dispatches the camera.
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void disableShowButton(){
        takeAPicture.setVisibility(View.INVISIBLE);
        pickGallery.setVisibility(View.INVISIBLE);
        sendButton.setVisibility(View.VISIBLE);
    }

    public void TakePicture() throws IOException {
        Camera.takePicture(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_qrscan, menu);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode == RESULT_CANCELED) { stop(); }

        else if(resultCode == RESULT_OK) {

            switch(requestCode)
            {
                case PICK_IMAGE:
                        mImageView.setImageBitmap(BitmapFactory.decodeFile(pickImageFromGallery(intent)));
                    break;
                case ACTION_TAKE_PHOTO:
                        showImg();
                    break;
            }
        }

    }

    private void dispatchTakePictureFromGallery() {
        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = null;
        chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    /**
     *
     */
    private String pickImageFromGallery(Intent intent){
        Uri selectedImage = intent.getData();
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    private void showImg() {
        ImageView mImageView = (ImageView) findViewById(R.id.imageView1);
        Bitmap image = Camera.getFullSizedPicture();
        mImageView.setImageBitmap(image);
    }

    private void stop() {
        Camera.destroyPicture();
        setResult(RESULT_CANCELED);
        finish();
    }

}
