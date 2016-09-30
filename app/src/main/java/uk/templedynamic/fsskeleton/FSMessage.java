package uk.templedynamic.fsskeleton;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.util.Date;
import java.util.List;

/**
 * Created by pryderi on 28/09/2016.
 */

public class FSMessage extends FSDatabaseRecord {

    // Schema
    public static final String COL_ID = "_id";
    public static final String COL_GLOBAL_ID = "global_id";
    public static final String COL_SOURCE_ID = "source_id";
    public static final String COL_DEST_ID = "dest_id";
    public static final String COL_TITLE = "title";
    public static final String COL_CONTENT = "content";
    public static final String COL_STATUS = "status";
    public static final String COL_REF_TYPE = "ref_type";
    public static final String COL_REF_ID = "ref_id";
    public static final String COL_EXCHANGE_TYPE = "exchange_type";
    public static final String COL_CREATED_AT = "created_at";
    public static final String COL_UPDATED_AT = "updated_at";

    public String record_id;
    public String title, content;
    public String global_id, source_id, dest_id;
    public String ref_type, ref_id, status;
    public String exchange_type;
    public Date created_at, updated_at;


    public static FSMessage recordFromCursor(Cursor cursor) {
        FSMessage newMsg = new FSMessage();
        newMsg.record_id = cursor.getString(cursor.getColumnIndex(COL_ID));
        newMsg.global_id = cursor.getString(cursor.getColumnIndex(COL_GLOBAL_ID));
        newMsg.source_id = cursor.getString(cursor.getColumnIndex(COL_SOURCE_ID));
        newMsg.dest_id = cursor.getString(cursor.getColumnIndex(COL_DEST_ID));
        newMsg.title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
        newMsg.content = cursor.getString(cursor.getColumnIndex(COL_CONTENT));
        newMsg.status = cursor.getString(cursor.getColumnIndex(COL_STATUS));
        newMsg.ref_type = cursor.getString(cursor.getColumnIndex(COL_REF_TYPE));
        newMsg.ref_id = cursor.getString(cursor.getColumnIndex(COL_REF_ID));
        newMsg.exchange_type = cursor.getString(cursor.getColumnIndex(COL_EXCHANGE_TYPE));
        newMsg.created_at = sqltimeFromString(cursor.getString(cursor.getColumnIndex(COL_CREATED_AT)));
        newMsg.updated_at = sqltimeFromString(cursor.getString(cursor.getColumnIndex(COL_UPDATED_AT)));
        return newMsg;
    }

    public static final String[] allcolumns = {COL_ID, COL_GLOBAL_ID, COL_SOURCE_ID, COL_DEST_ID, COL_TITLE, COL_CONTENT, COL_STATUS, COL_REF_TYPE, COL_REF_ID, COL_EXCHANGE_TYPE, COL_CREATED_AT, COL_UPDATED_AT};

}
