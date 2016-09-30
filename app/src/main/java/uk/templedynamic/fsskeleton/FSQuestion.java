package uk.templedynamic.fsskeleton;

import android.database.Cursor;

import java.util.Date;

/**
 * Created by pryderi on 28/09/2016.
 */

public class FSQuestion extends FSDatabaseRecord {

    // Schema
    public static final String COL_ID = "_id";
    public static final String COL_QUESTION = "question";
    public static final String COL_CREATED_AT = "created_at";
    public static final String COL_UPDATED_AT = "updated_at";

    public String record_id;
    public String question;
    public Date created_at, updated_at;


    public static FSQuestion recordFromCursor(Cursor cursor) {
        FSQuestion newMsg = new FSQuestion();
        newMsg.record_id = cursor.getString(cursor.getColumnIndex(COL_ID));
        newMsg.question = cursor.getString(cursor.getColumnIndex(COL_QUESTION));
        newMsg.created_at = sqltimeFromString(cursor.getString(cursor.getColumnIndex(COL_CREATED_AT)));
        newMsg.updated_at = sqltimeFromString(cursor.getString(cursor.getColumnIndex(COL_UPDATED_AT)));
        return newMsg;
    }

    public static final String[] allcolumns = {COL_ID, COL_QUESTION, COL_CREATED_AT, COL_UPDATED_AT};

}
