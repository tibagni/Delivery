package com.delivery.android.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;


public class Order implements BaseColumns {
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

	/**
	 * The name of the client who ordered
	 */
	public static final String COLUMN_CLIENT_NAME = "client_name";

	/**
	 * String representation of date and time the order was made (format: dd/mm/yyyy - hh:mm:ss)
	 */
	public static final String COLUMN_DATE_TIME = "date_time";

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
	      + COLUMN_ADDRESS_KEY + " INTEGER NOT NULL,"
	      + COLUMN_CLIENT_NAME + " TEXT NOT NULL,"
	      + COLUMN_DATE_TIME + " TEXT NOT NULL"
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
	    Log.w(Order.class.getName(), "Upgrading database from version " + oldVersion + " to " + newVersion
	        + ", which will destroy all old data");

	    database.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
	    onCreate(database);
	  }

	  private long mId;
	  private long mRemoteId;
	  private Address mRemoteAddress;
	  private boolean mCharge;
	  private double mChange;
	  private double mPaymentValue;
	  private String mDescription;
	  private long mAddressKey;
	  private String mClientName;
	  private String mDateTime;

		public long getRemoteId() {
			return mRemoteId;
		}

		public void setRemoteId(long remoteId) {
			this.mRemoteId = remoteId;
		}

		public Address getRemoteAddress() {
			return mRemoteAddress;
		}

		public void setRemoteAddress(Address remoteAddress) {
			this.mRemoteAddress = remoteAddress;
		}

		public boolean isCharge() {
			return mCharge;
		}

		public void setCharge(boolean charge) {
			this.mCharge = charge;
		}

		public double getChange() {
			return mChange;
		}

		public void setChange(double change) {
			this.mChange = change;
		}

		public double getPaymentValue() {
			return mPaymentValue;
		}

		public void setPaymentValue(double mPaymentValue) {
			this.mPaymentValue = mPaymentValue;
		}

		public String getDescription() {
			return mDescription;
		}

		public void setDescription(String description) {
			this.mDescription = description;
		}

		public long getId() {
			return mId;
		}

		public void setId(long mId) {
			this.mId = mId;
		}

		public long getAddressKey() {
			return mAddressKey;
		}

		public void setAddressKey(long mAddressKey) {
			this.mAddressKey = mAddressKey;
		}

		public String getClientName() {
			return mClientName;
		}

		public void setClientName(String mClientName) {
			this.mClientName = mClientName;
		}

		public String getDateTime() {
			return mDateTime;
		}

		public void setDateTime(String mDateTime) {
			this.mDateTime = mDateTime;
		}

		public ContentValues toContentValues() {
			ContentValues values = new ContentValues();
			values.put(COLUMN_REMOTE_ID, mRemoteId);
			values.put(COLUMN_ADDRESS_KEY, mAddressKey);
			values.put(COLUMN_RECEIVE_PAYMENT, (mCharge ? 1 : 0));
			if (mCharge) {
				values.put(COLUMN_PAYMENT_CHANGE, mChange);
				values.put(COLUMN_PAYMENT_VALUE, mPaymentValue);
			}
			values.put(COLUMN_DESCRIPTION, mDescription);
			values.put(COLUMN_CLIENT_NAME, mClientName);
			values.put(COLUMN_DATE_TIME, mDateTime);
			return values;
		}

		public static Order restore(Cursor c) {
			Order o = new Order();
			o.mId = c.getLong(c.getColumnIndex(COLUMN_ID));
			o.mRemoteId = c.getLong(c.getColumnIndex(COLUMN_REMOTE_ID));
			o.mRemoteAddress = null;
			o.mCharge = c.getLong(c.getColumnIndex(COLUMN_RECEIVE_PAYMENT)) == 1;
			if (o.mCharge) {
				o.mChange = c.getDouble(c.getColumnIndex(COLUMN_PAYMENT_CHANGE));
				o.mPaymentValue = c.getDouble(c.getColumnIndex(COLUMN_PAYMENT_VALUE));
			}
			o.mDescription = c.getString(c.getColumnIndex(COLUMN_DESCRIPTION));
			o.mAddressKey = c.getLong(c.getColumnIndex(COLUMN_ADDRESS_KEY));
			o.mClientName = c.getString(c.getColumnIndex(COLUMN_CLIENT_NAME));
			o.mDateTime = c.getString(c.getColumnIndex(COLUMN_DATE_TIME));
			return o;
		}

		@Override
		public String toString() {
			return "Order [mId=" + mId + ", mRemoteId=" + mRemoteId
					+ ", mRemoteAddress=" + mRemoteAddress + ", mCharge="
					+ mCharge + ", mChange=" + mChange + ", mPaymentValue="
					+ mPaymentValue + ", mDescription=" + mDescription
					+ ", mAddressKey=" + mAddressKey + ", mClientName="
					+ mClientName + ", mDateTime=" + mDateTime + "]";
		}

}
