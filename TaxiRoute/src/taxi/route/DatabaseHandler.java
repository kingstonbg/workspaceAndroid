package taxi.route;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {
	 
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "TaxiRouteDatabase";
    private static final String TABLE_NAME = "Data";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_FB_ID = "facebookID";
 
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
 
    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DATA_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTO INCREMENT," + KEY_NAME + " TEXT,"
                + KEY_FB_ID + " TEXT" + ")";
        db.execSQL(CREATE_DATA_TABLE);
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    
    public void addNewUser() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	if(isRegistered()) {
	        ContentValues values = new ContentValues();
	        values.put(KEY_NAME, FacebookConnector.getUserName());
	        values.put(KEY_FB_ID, FacebookConnector.getId());
	        
	        db.insert(TABLE_NAME, null, values);
	        db.close();
    	}
    }
    
    public boolean isRegistered() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	if(db.query(TABLE_NAME, null, KEY_FB_ID,null, null, null, null).toString() == FacebookConnector.getId()) {
    		return true;
    	}
    	else {
    		return false;
    	}
    }
    
    public Cursor showDatabase() {
    	SQLiteDatabase db = this.getWritableDatabase();
    	return  db.query(TABLE_NAME, new String[] {KEY_ID, KEY_FB_ID, KEY_NAME}, null, null, null, null, KEY_NAME);
    }
    
}