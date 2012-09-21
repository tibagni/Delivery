package com.delivery.android.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

public class Address implements BaseColumns {
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
	public static final String COLUMN_UF = "uf";

	// Database creation SQL statement
	private static final String DATABASE_CREATE = "create table " + TABLE_NAME
			+ " ("
			+ COLUMN_ID + " integer primary key,"
			+ COLUMN_ZIP_CODE
			+ " text not null,"
			+ COLUMN_STREET + " text not null,"
			+ COLUMN_NUMBER + " text not null,"
			+ COLUMN_COMPLEMENT + " text,"
			+ COLUMN_DISTRICT + " text not null,"
			+ COLUMN_CITY + " text not null,"
			+ COLUMN_UF + " text not null" + ");";

	public static final Uri CONTENT_URI = Uri.parse("content://"
			+ OrderContentProvider.AUTHORITY + "/" + TABLE_NAME);

	public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
			+ "/addresses";
	public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
			+ "/address";

	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion,
			int newVersion) {
		Log.w(Order.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");

		database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(database);
	}

	private long mId;
	private String mStreet;
	private int mNumber;
	private String mComplement;
	private String mDistrict;
	private String mCity;
	private String mUf;
	private String mZip;


	public long getId() {
		return mId;
	}

	public void setId(long mId) {
		this.mId = mId;
	}

	public String getStreet() {
		return mStreet;
	}

	public void setStreet(String street) {
		this.mStreet = street;
	}

	public int getNumber() {
		return mNumber;
	}

	public void setNumber(int number) {
		this.mNumber = number;
	}

	public String getComplement() {
		return mComplement;
	}

	public void setComplement(String complement) {
		this.mComplement = complement;
	}

	public String getDistrict() {
		return mDistrict;
	}

	public void setDistrict(String district) {
		this.mDistrict = district;
	}

	public String getCity() {
		return mCity;
	}

	public void setCity(String city) {
		this.mCity = city;
	}

	public String getUf() {
		return mUf;
	}

	public void setUf(String uf) {
		this.mUf = uf;
	}

	public String getZip() {
		return mZip;
	}

	public void setZip(String zip) {
		this.mZip = zip;
	}

	public ContentValues toContentValues() {
		ContentValues values = new ContentValues();

		values.put(COLUMN_STREET, mStreet);
		values.put(COLUMN_NUMBER, mNumber);
		if (!TextUtils.isEmpty(mComplement)) {
			values.put(COLUMN_COMPLEMENT, mComplement);
		}
		values.put(COLUMN_DISTRICT, mDistrict);
		values.put(COLUMN_CITY, mCity);
		values.put(COLUMN_UF, mUf);
		values.put(COLUMN_ZIP_CODE, mZip);
		return values;
	}


	public static Address restore(Cursor c) {
		Address a = new Address();
		a.mId = c.getLong(c.getColumnIndex(COLUMN_ID));
		a.mStreet = c.getString(c.getColumnIndex(COLUMN_STREET));
		a.mNumber = c.getInt(c.getColumnIndex(COLUMN_NUMBER));
		a.mComplement = c.getString(c.getColumnIndex(COLUMN_COMPLEMENT));
		a.mDistrict = c.getString(c.getColumnIndex(COLUMN_DISTRICT));
		a.mCity = c.getString(c.getColumnIndex(COLUMN_CITY));
		a.mUf = c.getString(c.getColumnIndex(COLUMN_UF));
		a.mZip = c.getString(c.getColumnIndex(COLUMN_ZIP_CODE));
		return a;
	}

	@Override
	public String toString() {
		return "RemoteAddress [mStreet=" + mStreet + ", mNumber=" + mNumber
				+ ", mComplement=" + mComplement + ", mDistrict=" + mDistrict
				+ ", mCity=" + mCity + ", mUf=" + mUf + ", mZip=" + mZip + "]";
	}

}
