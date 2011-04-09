package com.android.trip;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseProvider extends SQLiteOpenHelper {

	static final String dbName = "tripDB";
	static final String logTable = "tbSavedLogs";	
	static final String colLogName = "LogName";
	static final String colTotalTime = "TotalTime";
	static final String colTotalDistance = "TotalDistance";
	static final String colSavedDate = "SavedDate";
	
	
	public DatabaseProvider(Context context){
		super (context,dbName,null,33);
	}
	
	
	
	public DatabaseProvider(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE "+logTable+" ("+colLogName+ " TEXT, "
												+colTotalTime+ " INTEGER, "
												+colTotalDistance+ " INTEGER, "
												+colSavedDate+ " TEXT");
		
	}
	
	
	public void insertLog(String logName,int totalTime, int totalDistance, String time)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(colLogName,logName);
		cv.put(colTotalTime, totalTime);
		cv.put(colTotalDistance,totalDistance);
		cv.put(colSavedDate, time);
		
		db.insert(logTable, null, cv);
		db.close();
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	public SQLiteCursor getLogs()
	{
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = "SELECT * FROM "+logTable;
		SQLiteCursor cur = (SQLiteCursor)db.rawQuery(sql,null);
		return cur;
	}
	
	
	public Boolean ifTableExists(String tableName)
	{
		SQLiteDatabase db = this.getReadableDatabase();
		String sql = 
            "SELECT name FROM sqlite_master WHERE type='table' AND name='"+tableName+"'";      
		SQLiteCursor cur=(SQLiteCursor)db.rawQuery(sql,null);
		
		if(cur.getCount()>0) return true;
		
		return false;
	}
	
	
	

}
