package uk.templedynamic.fsskeleton;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
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

public class FSMessagesCursorAdapter extends SimpleCursorAdapter {

    private final Activity activity;
 //   private FSTheme theme;

    public FSMessagesCursorAdapter(Activity act, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(act.getApplicationContext(), layout, c, from, to, flags);
        this.activity = act;
    }

//    public void setTheme(FSTheme t) {
//        this.theme = t;
//    }

    static class ViewHolder {
        public RelativeLayout rowlayout;
        public TextView title;
        public TextView tag;
        public ImageView image;

        public void setBackgndCol(int col) {
            rowlayout.setBackgroundColor(col);
            title.setBackgroundColor(col);
            tag.setBackgroundColor(col);
        }

        public void setTextCol(int col) {
            title.setTextColor(col);
            tag.setTextColor(col);
            // note there is also public void setTextColor (ColorStateList colors) snd setTextAppearence and probabl others
            // which set colors for normal, selected, focused states.
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        if (rowView == null) {
            LayoutInflater inflater = this.activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.message_row, null);

            ViewHolder viewHolder = new ViewHolder();
            viewHolder.rowlayout = (RelativeLayout) rowView.findViewById(R.id.message_row);
            viewHolder.title = (TextView) rowView.findViewById(R.id.message_row_title);
            viewHolder.tag = (TextView) rowView.findViewById(R.id.message_row_content);
            viewHolder.image = (ImageView) rowView.findViewById(R.id.icon);

            // These rows will be transparent by default - could set them to a colour instead
            // int backgndCol = Color.parseColor(theme.getRGBColourForStyle(Theme.Style.BKGND));
            // viewHolder.setBackgndCol(backgndCol);

//            int txtCol = Color.parseColor(theme.getRGBColourForStyle(FSTheme.Style.TXT));

//            viewHolder.setTextCol(txtCol); // does both title and tag

            rowView.setTag(viewHolder);
        }

        ViewHolder holder = (ViewHolder) rowView.getTag();
        Cursor cursor = getCursor();
        cursor.moveToPosition(position);

        String title = cursor.getString(cursor.getColumnIndex(FSMessage.COL_TITLE));
        String content = cursor.getString(cursor.getColumnIndex(FSMessage.COL_CONTENT));

        holder.title.setText(title);
        holder.tag.setText(content);

        return rowView;
    }

}
