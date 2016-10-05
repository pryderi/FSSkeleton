package uk.templedynamic.fsskeleton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class PhotoActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    private static final String SEND_TAG = "messages";
    private static final String DELETE_TAG = "camera";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button messages_button = (Button) findViewById(R.id.photo_delete_button);
        messages_button.setTag(DELETE_TAG);
        messages_button.setOnClickListener(this);

        Button camera_button = (Button) findViewById(R.id.photo_send_button);
        camera_button.setTag(SEND_TAG);
        camera_button.setOnClickListener(this);
    }


    public void onClick(View v) {

        int id = v.getId();
        String tag = (String) v.getTag();
        Log.i(TAG, "On click called with tag " + tag + "; = ID " + id);

        if (tag.equals(SEND_TAG)) {
            Intent i = new Intent(this, FSMessagesActivity.class);
            startActivity(i);
        } else if (tag.equals(DELETE_TAG))  {

            Log.e(TAG, "Deleting the photo ");

            String fileuri = "xxx";   // set this to the uri of the file to be deleted

            FSAttachment.delete(fileuri);   // just logs that the method was called.

            // delete the photo file from the phone at this point.

            // Noting else to do here!


        } else {
            Log.e(TAG, "Unknown button");
        }
    }

}
