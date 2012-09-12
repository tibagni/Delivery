package com.delivery.order;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;

public class Order {

	private long mId;
	private int mAddressId;
	private long mUserAccountId;
	private int mDeliveryGuyId;
	private int mStatus;
	private double mPrice;
	private Calendar mTimestamp;

    // Usado enquanto o pedido esta na memoria
    List<OrderItem> mItems;
    int mNextTemporaryId = 0;
    private boolean mClosed;
    private boolean mFinalized;

    private final HashSet<Integer> mQueryStatusSet;
    private final HashSet<Integer> mQueryExcludeStatusSet;

    // Usado simplesmente para apresentacao de dados
    private String mCachedUserName;
    private String mCachedAddress;

    private Payment mPayment;

    public Order() {
    	mQueryStatusSet = new HashSet<Integer>();
    	mQueryExcludeStatusSet = new HashSet<Integer>();
    	// Inicia o timestamp a partir do momento em que o pedido foi criado
    	mTimestamp = Calendar.getInstance();
    }

    public List<OrderItem> getItems() {
        return mItems;
    }

    public void setItems(List<OrderItem> items) {
    	if (mClosed) {
    		throw new IllegalStateException("O pedido ja esta fechado");
    	}
        mItems = items;
    }

    public void addItem(OrderItem item) {
    	if (mClosed) {
    		throw new IllegalStateException("O pedido ja esta fechado");
    	}
        if (mItems == null) {
            mItems = new ArrayList<OrderItem>();
        }
        mNextTemporaryId++;
        mItems.add(item);
    }

    public int getNextTemporaryId() {
        return mNextTemporaryId;
    }

    public void removeItem(int tempItemId) {
    	if (mClosed) {
    		throw new IllegalStateException("O pedido ja esta fechado");
    	}
        OrderItem toRemove = null;
        for (OrderItem oi : mItems) {
            if (oi.getTemporaryId() == tempItemId) {
                toRemove = oi;
                break;
            }
        }

        if (toRemove != null) {
            mItems.remove(toRemove);
        }
    }

    public void clear() {
    	if (mClosed) {
    		throw new IllegalStateException("O pedido ja esta fechado");
    	}
        if (mItems != null) {
            mItems.clear();
        }
    }

    public void close() {
    	if (mClosed) {
    		throw new IllegalStateException("O pedido ja esta fechado");
    	}
    	mClosed = true;
    	calculateOrderPrice();
    }

    public void setOrderSeccessufullyFinalized() {
    	if (!mClosed) {
    		throw new IllegalStateException("O pedido nao esta fechado");
    	}
    	mFinalized = true;
    }

    private void calculateOrderPrice() {
    	double price = 0.0;
    	for (OrderItem item : mItems) {
    		price += item.getPrice();
    	}
    	mPrice = price;
    }

    public boolean isClosed() {
    	return mClosed;
    }

    public boolean isSuccessfullyFinalized() {
    	return mFinalized;
    }

    public int getAddressId() {
		return mAddressId;
	}

	public void setAddressId(int mAddressId) {
		this.mAddressId = mAddressId;
	}

	public long getUserAccountId() {
		return mUserAccountId;
	}

	public void setUserAccountId(long l) {
		this.mUserAccountId = l;
	}

	public int getDeliveryGuyId() {
		return mDeliveryGuyId;
	}

	public void setDeliveryGuyId(int mDeliveryGuyId) {
		this.mDeliveryGuyId = mDeliveryGuyId;
	}

	public int getStatus() {
		return mStatus;
	}

	public void setStatus(int mStatus) {
		this.mStatus = mStatus;
	}

	public String getStatusText() {
		return OrderStatus.getTextFromStatus(mStatus);
	}

	public boolean getCanChangeStatusManually() {
		int next = OrderStatus.getNextAllowedState(mStatus);
		if (next != mStatus && next != -1) {
			return true;
		}
		return false;
	}

	public int getNextAllowedStatus() {
		return OrderStatus.getNextAllowedState(mStatus);
	}

	public String getNextStatusText() {
		return OrderStatus.getTextFromStatus(OrderStatus.getNextAllowedState(mStatus));
	}

	public long getId() {
		return mId;
	}

	public void setId(long mId) {
		this.mId = mId;
	}

	public double getPrice() {
		return mPrice;
	}

	public void setPriceFromQuery(double price) {
		mPrice = price;
	}

	public HashSet<Integer> getQueryStatusSet() {
		return mQueryStatusSet;
	}

	public void addStatusToQuery(int status) {
		mQueryStatusSet.add(status);
	}

	public boolean hasValidState() {
		switch (mStatus) {
			case OrderStatus.DELIVERING:
			case OrderStatus.FINISHED:
			case OrderStatus.PREPARING:
			case OrderStatus.READY_TO_DELIVER:
			case OrderStatus.WAITING_FOR_PAYMENT:
			case OrderStatus.CANCELLED:
				return true;
		}
		return false;
	}

	public Calendar getTimestamp() {
		return mTimestamp;
	}

	public void setTimestamp(Calendar mTimestamp) {
		this.mTimestamp = mTimestamp;
	}

	public String getTimestampAsText() {
		int day = mTimestamp.get(Calendar.DAY_OF_MONTH);
		int month = mTimestamp.get(Calendar.MONTH) + 1;
		int year = mTimestamp.get(Calendar.YEAR);

		int hour = mTimestamp.get(Calendar.HOUR_OF_DAY);
		int minutes = mTimestamp.get(Calendar.MINUTE);
		int seconds = mTimestamp.get(Calendar.SECOND);

		return String.format("%02d/%02d/%04d - %02d:%02d:%02d", day, month, year, hour, minutes, seconds);
	}

	public HashSet<Integer> getQueryExcludeStatusSet() {
		return mQueryExcludeStatusSet;
	}

	public void addExcludeStatusToQuery(int status) {
		mQueryExcludeStatusSet.add(status);
	}

	public String getCachedUserName() {
		return mCachedUserName;
	}

	public void setCachedUserName(String mCachedUserName) {
		this.mCachedUserName = mCachedUserName;
	}

	public String getCachedAddress() {
		return mCachedAddress;
	}

	public void setCachedAddress(String mCachedAddress) {
		this.mCachedAddress = mCachedAddress;
	}

	public Payment getPayment() {
		return mPayment;
	}

	public void setPayment(Payment mPayment) {
		this.mPayment = mPayment;
	}

	public boolean getWaitingPaymentStatus() {
		return mStatus == OrderStatus.WAITING_FOR_PAYMENT;
	}

	public static class OrderStatus {
		public static final int NOT_INITIALIZED 	= 0;
    	/**
    	 * O pedido acabou de ser realizado. Esperando confirmacao de pagamento
    	 */
    	public static final int WAITING_FOR_PAYMENT = 1;

    	/**
    	 * O pagamento do pedido ja foi confirmado e esta na fila para ser preparado
    	 */
    	public static final int READY_TO_PREPARE   = 2;

    	/**
    	 * O pagamento do pedido ja foi confirmado. O pedido esta sendo preparado
    	 */
    	public static final int PREPARING		    = 3;

    	/**
    	 * O pedido ja foi preparado, esperando para ser entregue
    	 */
    	public static final int READY_TO_DELIVER    = 4;

    	/**
    	 * O pedido ja saiu para entrega e logo deve chegar ao seu destino
    	 */
    	public static final int DELIVERING		    = 5;

    	/**
    	 * Pedido concluido!
    	 */
    	public static final int FINISHED		    = 6;

    	/**
    	 * Pedido cancelado!
    	 */
    	public static final int CANCELLED		    = 7;

    	private static final String[] STATUS_TEXT = new String[8];
    	static {
    		STATUS_TEXT[NOT_INITIALIZED] = "Pedido n‹o inicializado";
    		STATUS_TEXT[WAITING_FOR_PAYMENT] = "Aguardando pagamento";
    		STATUS_TEXT[READY_TO_PREPARE] = "Na fila para preparo";
    		STATUS_TEXT[PREPARING] = "Pedido sendo preparado";
    		STATUS_TEXT[READY_TO_DELIVER] = "Pedido pronto para ser entregue";
    		STATUS_TEXT[DELIVERING] = "Pedido sendo entregue";
    		STATUS_TEXT[FINISHED] = "Pedido finalizado";
    		STATUS_TEXT[CANCELLED] = "Pedido cancelado";
    	}

    	public static String getTextFromStatus(int status) {
    		if (status < 0 || status > 7) return null;

    		return STATUS_TEXT[status];
    	}

    	public static int getNextAllowedState(int currentState) {
    		switch (currentState) {
    			case OrderStatus.WAITING_FOR_PAYMENT:
    				// Este estado nao pode ser alterado manualmente
    				// entao vamos dizer que o proximo estado e o proprio estado atual
    				return currentState;
    			case OrderStatus.READY_TO_PREPARE:
    				return OrderStatus.PREPARING;
    			case OrderStatus.PREPARING:
    				return OrderStatus.READY_TO_DELIVER;
    			case OrderStatus.READY_TO_DELIVER:
    				return OrderStatus.DELIVERING;
    			case OrderStatus.DELIVERING:
    				return OrderStatus.FINISHED;
    		}
    		return -1;
    	}
    }
}
