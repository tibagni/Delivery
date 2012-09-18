package com.delivery.android.database;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.delivery.android.provider.OrderContentProvider;

public class AddressTable implements BaseColumns {
	/**
	 * Database table name
	 */
	public static final String TABLE_NAME = "address";

	/**
	 * Local id of address
	 */
	public static final String COLUMN_ID = _ID;

	public static final String COLUMN_ZIP_CODE = "zip_code";
	public static final String COLUMN_STREET = "street";
	public static final String COLUMN_NUMBER = "house_number";
	public static final String COLUMN_COMPLEMENT = "complement";
	public static final String COLUMN_DISTRICT = "district";
	public static final String COLUMN_CITY = "city";

	  // Database creation SQL statement
	  private static final String DATABASE_CREATE = "create table "
	      + TABLE_NAME
	      + " ("
	      + COLUMN_ID + " integer primary key,"
	      + COLUMN_ZIP_CODE + " text not null,"
	      + COLUMN_STREET + " text not null,"
	      + COLUMN_NUMBER + " text not null,"
	      + COLUMN_COMPLEMENT + " text,"
	      + COLUMN_DISTRICT + " text not null,"
	      + COLUMN_CITY + " text not null"
	      + ");";


	  public static final Uri CONTENT_URI = Uri.parse("content://" + OrderContentProvider.AUTHORITY
			  + "/" + TABLE_NAME);

	  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/addresses";
	  public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/address";

	  public static void onCreate(SQLiteDatabase database) {
	    database.execSQL(DATABASE_CREATE);
	  }

	  public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
	    Log.w(OrderTable.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");

	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    onCreate(database);
	  }
}
