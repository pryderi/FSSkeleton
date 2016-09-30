package uk.templedynamic.fsskeleton;

import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pryderi on 27/09/2016.
 */

public abstract class FSDatabaseRecord {

    protected static SimpleDateFormat sqliteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public String record_id;

    // default constructor
    // this is an abstract class, so the constructor will not be called directly.
    public FSDatabaseRecord() {

    }

    public static Cursor recordWithID(Context ctx, Uri uri, String[] columns, String id) {
        String[] argArray = new String[]{ id };
        Resources res = ctx.getResources();
        Cursor cursor = ctx.getContentResolver().query(uri, columns, "_id = ?", argArray, null);
        return cursor;
    }

    public static Cursor allRecords(Context ctx, Uri uri, String[] columns, String id) {
        Resources res = ctx.getResources();
        return ctx.getContentResolver().query(uri, columns, null, null, null);
    }


    //  **** Writing or Updating a record in the database

    protected SQLiteDatabase getDatabase() {
        FSDatabaseManager dbm = FSDatabaseManager.getTheDatabase(null);  // null context - the dbm will already exist and will have one.
        return dbm.getSqlDatabase();
    }

    // This one needs to be overridden in subclasses
    public void updateRecord(ContentValues values) {
        SQLiteDatabase sqldb = getDatabase();
        sqldb.update("the tablename", values, "_id=" + record_id, null);
    }

    public void saveRecord(ContentValues values) {
        SQLiteDatabase sqldb = getDatabase();
        sqldb.update("the tablename", values, "_id=" + record_id, null);
    }

    //
    // These ones create cursor loaders for the data in the table in question
    // Cursorloaders allow the data to be loaded in the background
    //

    // creates a cursor loader to load all columns of the record with the given id
    public static CursorLoader cursorLoaderForRecordWithID(Context context, Uri uri, String[] columns, String pageID) {
        String where = FSDatabaseTableDefs._ID + " = ?";
        String[] argArray = new String[]{ pageID };
        return cursorLoaderForSelectedRecords(context, uri, columns, where, argArray);
    }

    // creates a cursor loader to load all columns for all records
    public static CursorLoader cursorLoaderForAllRecords(Context context, Uri uri, String[] columns) {
        return cursorLoaderForSelectedRecords(context, uri, columns, null, null);
    }

    // creates a cursor loader to load specified columns for records selected by the where and args parameters
    public static CursorLoader cursorLoaderForSelectedRecords(Context context, Uri uri, String[] columns,  String where, String[] argArray) {
        return new CursorLoader(context, uri, columns, where, argArray, null);
    }


    // Convenience method for time fields
    // datestring in format yyyy-[m]m-[d]d hh:mm:ss[.f...]. The fractional seconds may be omitted. The leading zero for mm and dd may also be omitted.
    protected static Date sqltimeFromString(String datestring) {

        SimpleDateFormat iso8601Format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss");
        Date date;
        try {
            date = iso8601Format.parse(datestring);
        } catch (ParseException e) {
            date = null;
        }
        return date;
    }

}
