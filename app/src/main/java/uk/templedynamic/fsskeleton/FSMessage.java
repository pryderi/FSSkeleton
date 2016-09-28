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


    public static Uri getTableURI() {
        return FSDatabaseProvider.MESSAGES_URI;
    }

    private static String[] allcolumns = {COL_ID, COL_GLOBAL_ID, COL_SOURCE_ID, COL_DEST_ID, COL_TITLE, COL_CONTENT, COL_STATUS, COL_REF_TYPE, COL_REF_ID, COL_EXCHANGE_TYPE, COL_CREATED_AT, COL_UPDATED_AT};

    public static FSMessage recordFromCursor(Cursor cursor) {
        FSMessage newMsg = new FSMessage();
        newMsg.record_id = cursor.getString(0);
        newMsg.global_id = cursor.getString(1);
        newMsg.source_id = cursor.getString(2);
        newMsg.dest_id = cursor.getString(3);
        newMsg.title = cursor.getString(4);
        newMsg.content = cursor.getString(5);
        newMsg.status = cursor.getString(6);
        newMsg.ref_type = cursor.getString(7);
        newMsg.ref_id = cursor.getString(8);
        newMsg.exchange_type = cursor.getString(9);
        newMsg.created_at = sqltimeFromString(cursor.getString(10));
        newMsg.updated_at = sqltimeFromString(cursor.getString(11));
        return newMsg;
    }

}
