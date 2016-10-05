package uk.templedynamic.fsskeleton;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "on Create");
        View view = inflater.inflate(R.layout.messages_table_frag, container, false);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnItemSelectedListener) {
            listener = (OnItemSelectedListener) context;
        } else {
            throw new ClassCastException(context.toString() + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void showPagesFromCursor(Cursor cursor) {
        Log.i(TAG, "Mapping messages fragment - " + cursor.getCount() + " messages to display");
        String[] uiBindFrom = new String[]{ FSMessage.COL_TITLE, FSMessage.COL_CONTENT };
        int[] uiBindTo = new int[] {R.id.message_row_title, R.id.message_row_content};
        Log.i(TAG, "Mapping page cursor from " + FSMessage.COL_TITLE + " to col " + R.id.message_row_content);
        cursorAdapter = new FSMessagesCursorAdapter( getActivity(), android.R.layout.simple_list_item_1, cursor, uiBindFrom, uiBindTo, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER );
        setListAdapter(cursorAdapter);
    }

}
