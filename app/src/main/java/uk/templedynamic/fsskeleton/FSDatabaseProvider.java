package uk.templedynamic.fsskeleton;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by pryderi on 28/09/2016.
 */

public class FSDatabaseProvider extends ContentProvider {

    private final String TAG = this.getClass().getSimpleName();

    private static final String AUTHORITY = "uk.templedynamic.flyingsquirrel.DatabaseProvider";
    private static final String BASE_PATH = "content://" + AUTHORITY + "/";

    // Arbitrary values used by the URI matcher below
    public static final int ATTACHMENTS = 190;
    public static final int MESSAGES = 200;

    private static final String MESSAGES_PATH = "properties";
    private static final String ATTACHMENTS_PATH = "attachments";

    public static final Uri MESSAGES_URI = Uri.parse(BASE_PATH + MESSAGES_PATH);
    public static final Uri ATTACHMENTS_URI = Uri.parse(BASE_PATH + ATTACHMENTS_PATH);

    // Not using URI
    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, MESSAGES_PATH, MESSAGES);
        sURIMatcher.addURI(AUTHORITY, ATTACHMENTS_PATH, ATTACHMENTS);
     }

    private FSDatabaseManager dbm;

    @Override
    public boolean onCreate() {
        Log.i(TAG, "Creating database provider (no database manager yet)");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Log.i(TAG, "Going for a cursor query:" + uri.toString() );

        dbm = FSDatabaseManager.getTheDatabase(getContext());
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        /* This is not really being used used here, but will become important when I'm using different tables.
         * Match the URI requested against the URI matcher defined above, and we can for example set different tables
         * into the query.
         */
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case MESSAGES:
                Log.i(TAG, "Navtables query " + uri.toString());
                queryBuilder.setTables(FSDatabaseSchema.TABLE_MESSAGES);
                break;
            case ATTACHMENTS:
                Log.i(TAG, "Navlinks query " + uri.toString());
                queryBuilder.setTables(FSDatabaseSchema.TABLE_ATTACHMENTS);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

//
//  SOME DEBUGGING STUFF
//
//        for(int i = 0; i < projection.length; i++) {
//            Log.d(TAG, "SQL - Projection - " + projection[i]);
//        }
//        Log.d(TAG, "SQL - selection - " + selection);
//        if (selectionArgs == null) {
//            Log.d(TAG, "SQL - Args are null");
//        } else {
//            for(int i = 0; i < selectionArgs.length; i++) {
//                Log.d(TAG, "SQL - Args - " + selectionArgs[i]);
//            }
//        }

        Cursor cursor = queryBuilder.query(dbm.getSqlDatabase(), projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // should uri for the newly created row

        Uri retUri;
        if (null == dbm) {
            dbm = FSDatabaseManager.getTheDatabase(getContext());
        }
        SQLiteDatabase sqlDB = dbm.getSqlDatabase();

        Log.i(TAG, "Inserting row into table: " + uri.toString() );
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case ATTACHMENTS:
                long id = sqlDB.insert(FSDatabaseSchema.TABLE_ATTACHMENTS, null, values);
                getContext().getContentResolver().notifyChange(uri, null);
                retUri = Uri.parse(ATTACHMENTS_PATH + "/" + id);
                Log.i(TAG, "Inserted into attachments. Returning: " + retUri.toString() );
                break;
            default:
                retUri = null;
        }
        return retUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;  // should return number of rows affected
    }

    @Override
    public int delete (Uri uri, String selection, String[] selectionArgs) {
        return 0; // should return number of rows deleted
    }

    @Override
    public String getType (Uri uri) {
        // should return the MIME type of the data
        return null;
    }
}
