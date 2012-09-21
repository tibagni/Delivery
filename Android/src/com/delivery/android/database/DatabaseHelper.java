package com.delivery.android.database;

import com.delivery.android.provider.Address;
import com.delivery.android.provider.Order;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	  private static final String DATABASE_NAME = "order_db.db";
	  private static final int DATABASE_VERSION = 1;

	  public DatabaseHelper(Context context) {
	    super(context, DATABASE_NAME, null, DATABASE_VERSION);
	  }

	  // Method is called during creation of the database
	  @Override
	  public void onCreate(SQLiteDatabase database) {
		Address.onCreate(database);
	    Order.onCreate(database);
	  }

	  // Method is called during an upgrade of the database,
	  // e.g. if you increase the database version
	  @Override
	  public void onUpgrade(SQLiteDatabase database, int oldVersion,
	      int newVersion) {
		  // We still don't have any update so this is not needed.
	  }
}
