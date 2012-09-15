package com.delivery.android.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.delivery.android.database.AddressTable;
import com.delivery.android.database.DatabaseHelper;
import com.delivery.android.database.OrderTable;

public class OrderContentProvider extends ContentProvider {
	private DatabaseHelper mDatabase;

	  // Used for the UriMacher
	  private static final int ORDERS = 1;
	  private static final int ORDERS_ID = 2;

	  private static final int ADDRESSES = 3;
	  private static final int ADDRESSES_ID = 4;

	  public static final String AUTHORITY = "com.delivery.android.order";

	  private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  static {
		  //Order table
		  sURIMatcher.addURI(AUTHORITY, OrderTable.TABLE_NAME, ORDERS);
		  sURIMatcher.addURI(AUTHORITY, OrderTable.TABLE_NAME + "/#", ORDERS_ID);

		  //Address table
		  sURIMatcher.addURI(AUTHORITY, AddressTable.TABLE_NAME, ADDRESSES);
		  sURIMatcher.addURI(AUTHORITY, AddressTable.TABLE_NAME + "/#", ADDRESSES_ID);
	  }


	@Override
	public boolean onCreate() {
		mDatabase = new DatabaseHelper(getContext());
		return true;
	}

	private String getTableNameForUri(Uri uri) {
		switch(sURIMatcher.match(uri)) {
			case ORDERS:
			case ORDERS_ID:
				return OrderTable.TABLE_NAME;
			case ADDRESSES:
			case ADDRESSES_ID:
				return AddressTable.TABLE_NAME;
			default:
			   throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
	    int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = mDatabase.getWritableDatabase();
	    int rowsDeleted = 0;
	    String table = getTableNameForUri(uri);
	    switch (uriType) {
			case ORDERS_ID:
			case ADDRESSES_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					selection = BaseColumns._ID + "=" + id;
				} else {
					selection = BaseColumns._ID + "=" + id + " and " + selection;
				}
				break;
	    }
	    rowsDeleted = sqlDB.delete(table, selection, selectionArgs);

	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		switch(sURIMatcher.match(uri)) {
		case ORDERS:
			return OrderTable.CONTENT_TYPE;
		case ORDERS_ID:
			return OrderTable.CONTENT_ITEM_TYPE;
		case ADDRESSES:
			return AddressTable.CONTENT_TYPE;
		case ADDRESSES_ID:
			return AddressTable.CONTENT_ITEM_TYPE;
		default:
		   throw new IllegalArgumentException("Unknown URI: " + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
	    SQLiteDatabase sqlDB = mDatabase.getWritableDatabase();
	    long id = 0;
	    String table = getTableNameForUri(uri);

	    id = sqlDB.insert(table, null, values);
	    if (id > 0) {
	    	getContext().getContentResolver().notifyChange(uri, null);
	    	return ContentUris.withAppendedId(uri, id);
	    }

	    throw new IllegalArgumentException("Couldn't insert row into " + uri);
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
	    // Uisng SQLiteQueryBuilder instead of query() method
	    SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

	    String table = getTableNameForUri(uri);
    	queryBuilder.setTables(table);
	    int uriType = sURIMatcher.match(uri);
	    switch (uriType) {
			case ORDERS_ID:
			case ADDRESSES_ID:
				// Adding the ID to the original query
				queryBuilder.appendWhere(BaseColumns._ID + "=" + uri.getLastPathSegment());
			    break;
	    }

	    SQLiteDatabase db = mDatabase.getWritableDatabase();
	    Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
	    // Make sure that potential listeners are getting notified
	    cursor.setNotificationUri(getContext().getContentResolver(), uri);

	    return cursor;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
	    int uriType = sURIMatcher.match(uri);
	    SQLiteDatabase sqlDB = mDatabase.getWritableDatabase();
	    String table = getTableNameForUri(uri);
	    int rowsUpdated = 0;
		switch (uriType) {
			case ORDERS_ID:
			case ADDRESSES_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					selection = BaseColumns._ID + "=" + id;
				} else {
					selection = BaseColumns._ID + "=" + id + " and " + selection;
				}
				break;
		}
		rowsUpdated = sqlDB.update(table, values, selection, selectionArgs);

	    getContext().getContentResolver().notifyChange(uri, null);
	    return rowsUpdated;
	}

}