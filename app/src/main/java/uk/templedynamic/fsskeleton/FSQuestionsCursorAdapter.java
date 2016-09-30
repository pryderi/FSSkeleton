package uk.templedynamic.fsskeleton;

import android.app.Activity;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

/**
 * Created by pryderi on 28/09/2016.
 */

public class FSQuestionsCursorAdapter extends SimpleCursorAdapter {

    private final String TAG = this.getClass().getSimpleName();
    private final Activity activity;

    public FSQuestionsCursorAdapter(Activity act, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(act, layout, c, from, to, flags);
        Log.i(TAG, "Questions cursor adapter - " + c.getCount() + " Questions to display");
        this.activity = act;
    }

    static class ViewHolder {
        public RelativeLayout rowlayout;
        public TextView question;


        public void setBackgndCol(int col) {
            rowlayout.setBackgroundColor(col);
            question.setBackgroundColor(col);
        }

        public void setTextCol(int col) {
            question.setTextColor(col);
        }
    }

    public int getCount() {
        int z = super.getCount();
        Log.i(TAG, "Get count called - " + z + " records");
        return z;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = this.activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.fragment_fsquestion, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.rowlayout = (RelativeLayout) rowView.findViewById(R.id.list);
            viewHolder.question = (TextView) rowView.findViewById(R.id.question);

            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);

        String question = cursor.getString(cursor.getColumnIndex(FSQuestion.COL_QUESTION));

        holder.question.setText(question);


        return rowView;
    }

}
