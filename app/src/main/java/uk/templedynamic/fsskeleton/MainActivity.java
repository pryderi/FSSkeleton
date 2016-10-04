package uk.templedynamic.fsskeleton;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = this.getClass().getSimpleName();

    public static final String ACTIVITY_MESSSAGES = "messages";
    public static final String ACTIVITY_CAMERA = "camera";
    public static final String ACTIVITY_QUIZ = "quiz";

    public static final String ACTIVITY_NUTTY = "nuts";

    private FSDatabaseManager dbm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbm = FSDatabaseManager.getTheDatabase(this);
        if (dbm.loadDefaultDatabase(this)) {
            Log.i(TAG, "Database loaded OK");
        } else {
            Log.e(TAG, "Can't load the default db.  We are STUFFED!");
            this.finish();  // kill the app.
        }

        Button messages_button = (Button) findViewById(R.id.messages_button);
        messages_button.setTag(ACTIVITY_MESSSAGES);
        messages_button.setOnClickListener(this);

        Button camera_button = (Button) findViewById(R.id.camera_button);
        camera_button.setTag(ACTIVITY_CAMERA);
        camera_button.setOnClickListener(this);

    }


    public void onClick(View v) {

        int id = v.getId();
        String tag = (String) v.getTag();
        Log.i(TAG, "On click called with tag " + tag + "; = ID " + id);

        if (tag.equals(ACTIVITY_MESSSAGES)) {
            Intent i = new Intent(this, FSMessagesActivity.class);
            startActivity(i);
        } else if (tag.equals(ACTIVITY_CAMERA))  {
            Intent i = new Intent(this, FSGalleryActivity.class);
            startActivity(i);
        } else {
            Log.e(TAG, "Unknown button");
        }
    }
}
