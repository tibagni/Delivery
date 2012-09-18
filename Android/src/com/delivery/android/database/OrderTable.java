package com.delivery.android.database;

import android.content.ContentResolver;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.delivery.android.provider.OrderContentProvider;

public class OrderTable implements BaseColumns {
	/**
	 * Database table name
	 */
	public static final String TABLE_NAME = "orders";

	/**
	 * Local id of order
	 */
	public static final String COLUMN_ID = _ID;

	/**
	 * Remote Id (coming from server)
	 */
	public static final String COLUMN_REMOTE_ID = "remote_id";

	/**
	 * Whether the order was already paid (PagSeguro) or the delivery guy should
	 * receive the value (money)
	 *
	 * 0 - The order is already paid for so the delivery guy should just deliver it
	 * (without charging for it)
	 * 1 - The payment was NOT done so the delivery guy should receive it
	 */
	public static final String COLUMN_RECEIVE_PAYMENT = "receive_payment";

	/**
	 * If the payment is to be done in the moment of the delivery, this field will
	 * hold the value that the customer will pay
	 */
	public static final String COLUMN_PAYMENT_VALUE = "payment_value";

	/**
	 * If the payment is to be done in the moment of the delivery, this field will
	 * hold the value that will have to be returned to the customer (as change)
	 */
	public static final String COLUMN_PAYMENT_CHANGE = "payment_change";

	/**
	 * Description of the order
	 */
	public static final String COLUMN_DESCRIPTION = "description";

	/**
	 * Reference to the address table. This field will hold the key to the
	 * address in which the order should be delivered
	 */
	public static final String COLUMN_ADDRESS_KEY = "address_key";

	  // Database creation SQL statement
	  private static final String DATABASE_CREATE = "CREATE TABLE "
	      + TABLE_NAME
	      + " ("
	      + COLUMN_ID + " INTEGER PRIMARY KEY,"
	      + COLUMN_REMOTE_ID + " INTEGER NOT NULL,"
	      + COLUMN_RECEIVE_PAYMENT + " INTEGER NOT NULL,"
	      + COLUMN_PAYMENT_VALUE + " REAL,"
	      + COLUMN_PAYMENT_CHANGE + " REAL,"
	      + COLUMN_DESCRIPTION + " TEXT NOT NULL,"
	      + COLUMN_ADDRESS_KEY + " INTEGER NOT NULL"
	      + ");";


	  public static final Uri CONTENT_URI = Uri.parse("content://" + OrderContentProvider.AUTHORITY
			  + "/" + TABLE_NAME);

	  public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/orders";
	  public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/order";

	  public static final String[] REMOTE_ID_PROJECTION = new String[] { COLUMN_REMOTE_ID };

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
