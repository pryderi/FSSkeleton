package uk.templedynamic.fsskeleton;

import android.util.Log;

/**
 * Created by pryderi on 05/10/2016.
 */


//  This is just a stub class - the attachment handling is complicated, and does not need to be


public class FSAttachment {

    private final String TAG = this.getClass().getSimpleName();

    public String file_uri;


    public void save() {

        Log.i(TAG, "A new attachment was saved!   " + file_uri);

    }

    public static void delete(String deleted_file_uri) {

        Log.i("FSAttachment", "Attachment for file deleted: " + deleted_file_uri);
    }

}
