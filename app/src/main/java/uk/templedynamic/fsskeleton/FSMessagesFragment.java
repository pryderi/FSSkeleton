package uk.templedynamic.fsskeleton;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

/**
 * Created by pryderi on 28/09/2016.
 */

public class FSMessagesFragment extends ListFragment {

    public interface OnItemSelectedListener {
        public void onIndexItemSelected(FSMessage message);
    }

    private final String TAG = this.getClass().getSimpleName();
    private OnItemSelectedListener listener;
    private FSMessagesCursorAdapter cursorAdapter;
    private LinearLayout llayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "on Create");
 //       ConfigurationManager configurationManager = ConfigurationManager.getTheConfigurationManager();
        View view = inflater.inflate(R.layout.activity_messages, container, false);
        llayout = (LinearLayout)view.findViewById(R.id.messages_table_fragment);
        return view;
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        Cursor cursor = (Cursor) getListAdapter().getItem(position);
        FSMessage message = FSMessage.recordFromCursor(cursor);
        Log.i(TAG, "Listview item click at position " + position + ". Message is " + message.record_id + ": " + message.title);
        listener.onIndexItemSelected(message);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    // May also be triggered from the Activity
    public void updateDetail() {
        // Create a string, just for testing
        Log.i(TAG, "Updating detail text");
        Cursor cursor = (Cursor) getListAdapter().getItem(0);
        FSMessage message = FSMessage.recordFromCursor(cursor);
        listener.onIndexItemSelected(message);
    }

//    public void applyTheme(FSTheme newTheme) {
//        theme =  newTheme;
//        if (llayout != null) {
//            theme.configureBackgroundLayout(this.getActivity(), llayout);
//        }
//    }

    public void showPagesFromCursor(Cursor cursor) {
        String[] uiBindFrom = new String[]{ FSMessage.COL_TITLE, FSMessage.COL_CONTENT };
        int[] uiBindTo = new int[] {R.id.message_row_title, R.id.message_row_content};
        Log.i(TAG, "Mapping page cursor from " + FSMessage.COL_TITLE + " to col " + android.R.id.text1);
        cursorAdapter = new FSMessagesCursorAdapter( getActivity(), android.R.layout.simple_list_item_1,
                cursor, uiBindFrom, uiBindTo, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER );
//        cursorAdapter.setTheme(this.theme);
        setListAdapter(cursorAdapter);
    }
}
