package de.mytfg.jufo.mfakegps;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

public class FakeGPSDatabase {
    private Context context;
    //database variables
    private DbHelper dbHelper;
    public final String DBNAME = "FakeGPSDatabase";
    public final int DBVERSION = 20;
    public SQLiteDatabase db;
    public final String COLUMN_ID = "Id";
    public final String COLUMN_LAT = "latitude";
    public final String COLUMN_LON = "longitude";
    public final String COLUMN_ALT = "altitude";
    public final String COLUMN_SPE = "speed";
    public final String COLUMN_ACC = "accuracy";
    public final String TABLENAME = "FakeGPSData";
    public final String CREATERDB = "CREATE TABLE "+TABLENAME+" ("+COLUMN_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_LAT+" REAL NOT NULL, " +
            COLUMN_LON+" REAL NOT NULL, " +
            COLUMN_ALT+" REAL, " +
            COLUMN_SPE+" REAL, " +
            COLUMN_ACC+" REAL);";

    // Number of rows and actual row ("cursor")
    public long rows = 0;
    public long row = 0;

    // Log TAG
    protected static final String TAG = "FakeGPSDatabase-class";

    //constructor
    public FakeGPSDatabase(Context context) {
        Log.i(TAG, "FakeGPSDatabase Constructor");
        this.context = context;
        dbHelper = new DbHelper(context);
        open();
    }

    //creating a DbHelper
    public class DbHelper extends SQLiteOpenHelper {
        //DbHelper constructor
        public DbHelper(Context context) {
            super(context, DBNAME, null, DBVERSION);
            Log.i(TAG, DBVERSION + "");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.i(TAG, "onCreate()");
            db.execSQL(CREATERDB);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.i(TAG, "onUpgrade()");
            if(oldVersion<newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLENAME);
                onCreate(db);
                Log.i(TAG, "Database upgraded");
            }
        }
    }

    public long insertLocation(Location loc) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_LAT, loc.getLatitude());
        value.put(COLUMN_LON, loc.getLongitude());
        value.put(COLUMN_ALT, loc.getAltitude());
        value.put(COLUMN_SPE, loc.getSpeed());
        value.put(COLUMN_ACC, loc.getAccuracy());
        return db.insert(TABLENAME, null, value);
    }

    public long insertFakeLocation(FakeLocation loc) {
        ContentValues value = new ContentValues();
        value.put(COLUMN_LAT, loc.getLatitude());
        value.put(COLUMN_LON, loc.getLongitude());
        value.put(COLUMN_ALT, loc.getAltitude());
        value.put(COLUMN_SPE, loc.getSpeed());
        value.put(COLUMN_ACC, loc.getAccuracy());
        return db.insert(TABLENAME, null, value);
    }

    public FakeLocation getNextLocation() {
        FakeLocation loc = new FakeLocation();
        Cursor cursor = db.query(TABLENAME, new String[]{COLUMN_ID, COLUMN_LAT, COLUMN_LON, COLUMN_ALT, COLUMN_SPE, COLUMN_ACC}, null, null, null, null, null);

        // TODO

        // Increment row "cursor"
        row++;
        return loc;
    }

    public int getNumRows() {
        int num;
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + TABLENAME, null);
        mCount.moveToFirst();
        num = mCount.getInt(0);
        mCount.close();
        return num;
    }

    private void open() throws SQLException {
        Log.i(TAG, "open()");
        db = dbHelper.getWritableDatabase();
    }

    private void close() {
        Log.i(TAG, "close()");
        dbHelper.close();
    }


    public void deleteDatabase() {
        //delete database
        context.deleteDatabase(DBNAME);
        Log.i(TAG, "database deleted");
    }
}