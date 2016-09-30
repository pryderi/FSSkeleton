package uk.templedynamic.fsskeleton;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by pryderi on 27/09/2016.
 */

public class FSDatabaseManager {

    public static final String DBNAME_PREFIX = "FSData_"; // all database names should start with this prefix

    // format example as provided by SQLite 2012-01-28 20:53:23
    private static SimpleDateFormat sqliteFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    private final String TAG = this.getClass().getSimpleName();
    private static final String DEFAULT_DB_NAME = "FSSkeletonDB.sqlite";
    private static final int DB_VERSION = 2;

    private String databasename;
    private SQLiteWrapper sqLiteWrapper;
    private SQLiteWrapper newSQLiteWrapper;  // used when we swap to a new database.

    HashMap<String, String> recordSet;  // used to record the ids of all records in a table, so we can identify which ones have not been changed.
    String recordSetTableName; // the table to which the record set refers. (to avoid deleting from the wrong one!!)

    // Android's default system path for application databases.
    public File database_file;
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    // This is a singleton object The constructor is private, and the public class method getTheDatabase(context)
    // returns the single instance of this class.
    //
    // Usage:
    // getTheDatabase(context) returns the database manager; uses default database path
    // loadNewDataBase(newdbname) load the database with the name shown
    //
    //
    // This here is a singleton class.  There is only ever one instance of it.

    private static FSDatabaseManager theUniqueDatabase;

    // ...So the constructor is declared private, and cannot be called form elsewhere
    // The only way to make a new one is to call getTheDatabase below
    private FSDatabaseManager(Context context) {
        this.myContext = context;
        databasename = DEFAULT_DB_NAME;
    }

    // get the One True FSDatabaseManager.  Its a class method (static) and thread safe(synchronized)
    public static synchronized FSDatabaseManager getTheDatabase(Context context) {
        if (theUniqueDatabase == null) {
            theUniqueDatabase = new FSDatabaseManager(context);
        }
        return theUniqueDatabase;
    }

    public boolean loadDefaultDatabase(Context context) {
        theUniqueDatabase.database_file = context.getDatabasePath(DEFAULT_DB_NAME);
        boolean db_ok = true;
        // We will ensure the default database exists and attempt to create it if it doesn't
        if (!theUniqueDatabase.checkDataBase(DEFAULT_DB_NAME)) {
            // doesn't exist - create it.
            if (!theUniqueDatabase.copyDefaultDatabase()) {
                // if this fails, we are truly stuffed and the app can't run.
                // returning null will kill the app.
                theUniqueDatabase = null;
                db_ok = false;
            }
        }
        return db_ok;
    }

    public SQLiteDatabase getSqlDatabase() {
        if (null == myDataBase) {
            openDataBase(); // creates the SQLwrapper if there isn't one, and sets myDataBase
        }
        return myDataBase;
    }

    // loadNewDataBase.  Check that the new database exists.  If not we will try to create it
    // We will create a new SQLiteWrapper (extends SQLiteOpenHelper) for the new database
    // the old wrapper will be dropped only if the new one works out
    public synchronized boolean loadNewDataBase(String application_id) {

        String newDbName = DBNAME_PREFIX + application_id + ".sqlite";
        boolean success = false;

        if (checkDataBase(newDbName)) {
            // Database for this new app  exists, so we're in business
            // drop the current db, and open the new one.
            closeDataBase();
            sqLiteWrapper = null;  // drop the old db wrapper.
            databasename = newDbName;
            openDataBase(); // creates a new db wrapper as we nulled the old one.
            success = true;
        } else {
            Log.i(TAG, "Can't load database " + newDbName + " reverting to default");
            // make every attempt to fall back to the default db instead
            if (!checkDataBase(DEFAULT_DB_NAME)) {
                copyDefaultDatabase();
            }
            databasename = DEFAULT_DB_NAME;
            success = false; // we'll report back that the new db couldn't be found
        }
        return success;
    }


    // Check if the database with the given name exists as a file and can be opened as an SQLite db.
    // return true if it exists, false if it doesn't.
    private boolean checkDataBase(String dbname){
        SQLiteDatabase checkDB = null;
        File new_database_file = myContext.getDatabasePath(dbname);
        try{
            String dbPath = new_database_file.getPath();
            Log.i(TAG, "Checking existence of database - path: " + dbPath);
            checkDB = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e){
            //database does't exist.
            Log.e(TAG, "Database check failed - couldn't open the database " + databasename);
            checkDB = null;
        }
        if (checkDB != null){
            database_file = new_database_file;
            checkDB.close();
        }
        return checkDB != null;
    }

    /**
     * Copies a database from your local assets-folder to the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     **/
    private boolean copyDefaultDatabase() {

        this.sqLiteWrapper = new SQLiteWrapper(myContext, DEFAULT_DB_NAME);
        this.sqLiteWrapper.getReadableDatabase();

        boolean success = false;
        try {

            Log.i(TAG, "Copying database from assets");

            //Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(DEFAULT_DB_NAME);

            // Path to the just created empty db
            Log.i(TAG, "Copying " + DEFAULT_DB_NAME + " to " + database_file.getPath());
            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(database_file);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer))>0){
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
            success = true;
        } catch (IOException e) {
            Log.e(TAG, "Can't copy the default database, we're stuffed! " + e.getLocalizedMessage());  // success will return false!
        }

        return success;
    }

    // These methods are used by the JasonClient to write data to the database directly
    // not using a provider. This allows a bit more control over the transactions
    public void openDataBase() {
        //Open the database for reading and writing (not used by provider)
        File database_file = myContext.getDatabasePath(databasename);
        Log.i(TAG, "Opening database: " + database_file.getPath());
        if (null == sqLiteWrapper) {
            sqLiteWrapper = new SQLiteWrapper(myContext, databasename);
        }
        myDataBase = sqLiteWrapper.getSQLDatabase();
    }

    public void closeDataBase() {
        if (null != sqLiteWrapper) {  // not created by default at startup, so it may not exist.  If it doesn't there's nothing to close.
            sqLiteWrapper.close();
            Log.i(TAG, "Closing database at path ");
        }
    }

    // These methods are for downloads from the server.  This is transactional, so that we can
    // roll back if it all goes wrong.

    public void startTransaction() {
        myDataBase.beginTransaction();
    }

    public void endTransaction(boolean successful) {
        if (successful) {
            myDataBase.setTransactionSuccessful();  // can commit the changes.
        }
        myDataBase.endTransaction(); // commits the changes if was set as successful above
    }

    public void createRecordSet(String tablename) {

        // Log.i(TAG, "Creating record set for table " +  tablename);
        recordSetTableName = tablename; // must remember which tablename the recordSet is for!

        String[] cols = {"_id", "updated_at"};
        String where = null;
        String[] args = null;
        Cursor c = myDataBase.query(tablename, cols, where, args, null, null, null, null);
        recordSet = new HashMap<String, String>();
        if (c.getCount() > 0) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String updated_at = c.getString(1);
                String rec_id = c.getString(0);
                if (null == updated_at) {
                    updated_at = "1961-10-02 00:00:00";  // a null updated_at shouldn't happen, but if it does, force an early value!
                }
                recordSet.put(rec_id, updated_at); // key = id, value = updated_at
                // Log.i(TAG, "Added record " +  rec_id + " to record set; value = " + updated_at);
                c.moveToNext();
            }
        }
    }

    public void addUpdateItems(ContentValues values, String tablename) {

        // This processes updates, so these go in the new database, not the current open one.
        // RecordSet is a hash of each records updated_t field, keyed by its id.  Must be created before this is executed.
        // If the record does not exist, (i.e. not in recordSet) we will create a new one
        // if it DOES already exist, check if the new object has a later updated_at, in which case we update the existing object

        String rec_id = (String)values.get("_id");
        String where = "_ID = ?"; //used in the update command below

        // Log.i(TAG, "Adding new record to " + tablename + "; ID = " + rec_id);

        Date localdt, remotedt;  // local and remote dt

        String dtstring = recordSet.get(rec_id);  // null if no such record
        if (null != dtstring) {
            // This is an existing record.  Check the updated_at

            // Log.i(TAG, "Record exists. Checking if already updated. ID = " +  rec_id);
            try {
                localdt = sqliteFormat.parse(dtstring);
                remotedt = sqliteFormat.parse((String) values.get("updated_at"));
                // Log.i(TAG, "Comparing dates: local is  =  " +  localdt.toString() + " Remote is " + remotedt.toString());
                if (remotedt.getTime() > localdt.getTime()) {
                    // Log.i(TAG, "Local record is older. Will update. ID =  " +  rec_id);
                    values.remove("_id"); // this is in the 'where' clause
                    String[] args = {rec_id};
                    myDataBase.update(tablename, values, where, args);
                } else {
                    // Log.i(TAG, "Local record is newer.  Will not update. ID =  " +  rec_id);
                }
            } catch (ParseException e) {
                // Can't parse...abandon the record, as we can't make head or tail of the date..
                Log.e(TAG, "Couldn't parse the updated_at date! Abandoning: " + e.getLocalizedMessage());
            }
            recordSet.remove(rec_id); // remove it.  Any remaining records eed to be deleted
        } else {
            // Log.i(TAG, "No such record.  Will insert new one. ID =  " +  rec_id);
            myDataBase.insert (tablename, null, values);
        }
    }

    public void clearUncheckedRecords() {
        // This will delete any records that are in recordSet.
        // These should only be records that have not been ticked by the recent download.
        Log.i(TAG, "Clearing out any unchecked records...");
        if (null != recordSet) {
            if (!recordSet.isEmpty()) {
                StringBuilder strb = new StringBuilder("DELETE FROM " + recordSetTableName + " WHERE _id IN (");
                boolean isFirst = true;
                for (String rec_id : recordSet.keySet()) {
                    if (isFirst) {
                        strb.append(rec_id);
                        isFirst = false;
                    } else {
                        strb.append(", ").append(rec_id);
                    }
                }
                strb.append(")");
                Log.i(TAG, "Command is: " +  strb.toString());
                myDataBase.execSQL(strb.toString());
            } else {
                Log.i(TAG, "Nothing to delete.");
            }
        }
        // Finished!. Set to null so we don't go stomping over some poor table by mistake!
        recordSet = null;
        recordSetTableName = null;
    }

    private class SQLiteWrapper extends SQLiteOpenHelper {

        SQLiteDatabase sqlDatabase;
        String dbname;

        SQLiteWrapper(Context context, String dbname) {
            super(context, dbname, null, DB_VERSION);
            this.dbname = dbname;
        }

        SQLiteDatabase getSQLDatabase() {
            sqlDatabase = getWritableDatabase();  // returns the cached db if its already open, or makes it writable if it was only readable before
            Log.i(TAG, "Writable database at " + sqlDatabase.getPath());
            return sqlDatabase;
        }

        @Override
        public synchronized void close() {
            if(sqlDatabase != null) {
                sqlDatabase.close();
                sqlDatabase = null;
            }
            super.close();
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            // Shouldn't create the database, it must already exist or we're doomed!
            // If it doesn't exist, copy it from the package
            Log.i(TAG, "onCreate called for Database Manager SQLite wrapper.");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldv, int newv)
        {
            Log.i(TAG, "onUpgrade called for Database Manager.  Old version" + oldv + "; new version " + newv);
        }
    }
}
