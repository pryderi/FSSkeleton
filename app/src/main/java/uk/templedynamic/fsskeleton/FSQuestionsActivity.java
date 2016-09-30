package uk.templedynamic.fsskeleton;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;


public class FSQuestionsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>,  FSQuestionsFragment.OnItemSelectedListener {

    private final String TAG = this.getClass().getSimpleName();
    private static final int LOADER_MESSAGES = 0x01;

    private static final int TWO_PANELS = 2;
    private static final int ONE_PANEL = 1;
    private int displayMode;

    private ArrayList<FSMessage> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        getLoaderManager().initLoader(LOADER_MESSAGES, null, this);
    }

    public void onIndexItemSelected(FSQuestion question) {

        Log.i(TAG, "Question was selected with ID " + question.record_id);
        switch (displayMode) {
            case TWO_PANELS:
                // two panels, so need to load the selected content page locally
                // got a pageID, so kick off a new loader to get it from the database
//                Log.i(TAG, "Resarting loader with ID = " + LOADER_ONE_PAGE);
//
//                selectedEvent = new Event();
//                selectedEvent.setID(eventID); // change the id of the selected event, then start loader to populate it
//                getSupportLoaderManager().restartLoader(LOADER_ONE_PAGE, null, this);
                break;
            case ONE_PANEL:
                // launchActivity(downlinkType, downlinkID);
                Log.i(TAG, "Loading into the one panel");
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

//        CursorLoader cursorLoader = null;
//        Log.i(TAG, "Loading navlinks");
//        String[] cols = { FSMessage.COL_ID, FSMessage.COL_TITLE, FSMessage.COL_CONTENT};  // the columns to be loaded. id, title and subtitle!
//        cursorLoader = new CursorLoader(this, FSDatabaseProvider.MESSAGES_URI, cols, null, null, null);
//        return cursorLoader;

        // **** WAS ****
        Log.i(TAG, "Loading All message records ");
        CursorLoader cursorLoader =  new CursorLoader(this, FSDatabaseProvider.MESSAGES_URI, FSMessage.allcolumns, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        Log.i(TAG, "Callback to on load finished. Loader id: " + loader.getId() + " Records: " + cursor.getCount());
        FSMessagesFragment fragment = (FSMessagesFragment) getFragmentManager().findFragmentById(R.id.messFragment);
        if (fragment != null && fragment.isInLayout()) {
            fragment.showPagesFromCursor(cursor);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // For whatever reason, the Loader's data is now unavailable.
        // So will need to update the fragments and tell them to dumb their data
        // eg give them a null Cursor.
        // mAdapter.swapCursor(null);
    }
}

