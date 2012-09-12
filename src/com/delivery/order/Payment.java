package com.delivery.order;

public class Payment {

	private String mReference;
	private long mId;

	private long mOrderId;

	private int mPaymentStatus;
	private String mURL;

	private double mManualPaymentValue;

	public String getReference() {
		return mReference;
	}

	public void setReference(String mReference) {
		this.mReference = mReference;
	}

	public void generateReferenceFromId() {
		mReference = "REF-PAY-" + mId;
	}

	public long getId() {
		return mId;
	}

	public void setId(long mId) {
		this.mId = mId;
	}

	public long getOrderId() {
		return mOrderId;
	}

	public void setOrderId(long mOrderId) {
		this.mOrderId = mOrderId;
	}

	public int getPaymentStatus() {
		return mPaymentStatus;
	}

	public void setPaymentStatus(int mPaymentStatus) {
		this.mPaymentStatus = mPaymentStatus;
	}


	public String getURL() {
		return mURL;
	}

	public void setURL(String mURL) {
		this.mURL = mURL;
	}

	public boolean getManualPayment() {
		return mPaymentStatus == PaymentStatus.MANUAL;
	}


	public double getManualPaymentValue() {
		return mManualPaymentValue;
	}

	public void setManualPaymentValue(double mManualPaymentValue) {
		this.mManualPaymentValue = mManualPaymentValue;
	}


	public static class PaymentStatus {
		public static final int NEW = 0;
		public static final int FINALIZED = 1;
		public static final int CANELLED = 2;
		//Pagamento manual (Nao pela internet) no momento da entrega
		public static final int MANUAL = 3;

		public static boolean isValidState(int status) {
			switch(status) {
				case NEW:
				case FINALIZED:
				case CANELLED:
				case MANUAL:
					return true;
			}
			return false;
		}
	}
}
