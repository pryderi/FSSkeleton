package uk.templedynamic.fsskeleton;

import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;

import uk.templedynamic.fsskeleton.dummy.DummyContent;
import uk.templedynamic.fsskeleton.dummy.DummyContent.DummyItem;

import java.util.List;



public class FSQuestionsFragment extends ListFragment {

    public interface OnItemSelectedListener {
        public void onIndexItemSelected(FSQuestion question);
    }

    private final String TAG = this.getClass().getSimpleName();
    private uk.templedynamic.fsskeleton.FSQuestionsFragment.OnItemSelectedListener listener;
    private FSQuestionsCursorAdapter cursorAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, "on Create");
        View view = inflater.inflate(R.layout.messages_table_frag, container, false);
        return view;
    }

    @Override
    public void onListItemClick (ListView l, View v, int position, long id) {
        Cursor cursor = (Cursor) getListAdapter().getItem(position);
        FSQuestion question = FSQuestion.recordFromCursor(cursor);
        Log.i(TAG, "Listview item click at position " + position + ". Message is " + question.record_id + ": " + question.question);
        listener.onIndexItemSelected(question);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void showPagesFromCursor(Cursor cursor) {
        Log.i(TAG, "Mapping questions fragment - " + cursor.getCount() + " questions to display");
        String[] uiBindFrom = new String[]{ FSQuestion.COL_QUESTION };
        int[] uiBindTo = new int[] {R.id.question };
        Log.i(TAG, "Mapping page cursor from " + FSQuestion.COL_QUESTION + " to col " + R.id.question);
        cursorAdapter = new FSQuestionsCursorAdapter( getActivity(), android.R.layout.simple_list_item_1, cursor, uiBindFrom, uiBindTo, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER );
        setListAdapter(cursorAdapter);
    }

}
