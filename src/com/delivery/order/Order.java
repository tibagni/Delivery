package com.delivery.order;

import java.util.ArrayList;
import java.util.List;

public class Order {

	private long mId;
	private int mAddressId;
	private long mUserAccountId;
	private int mDeliveryGuyId;
	private int mStatus;
	private double mPrice;

    // Usado enquanto o pedido esta na memoria
    List<OrderItem> mItems;
    int mNextTemporaryId = 0;
    private boolean mClosed;

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

	public long getId() {
		return mId;
	}

	public void setId(long mId) {
		this.mId = mId;
	}

	public double getPrice() {
		return mPrice;
	}

	public static class OrderStatus {
    	/**
    	 * O pedido acabou de ser realizado. Esperando confirmacao de pagamento
    	 */
    	public static final int WAITING_FOR_PAYMENT = 0;

    	/**
    	 * O pagamento do pedido ja foi confirmado. O pedido esta sendo preparado
    	 * ou na fila para ser preparado
    	 */
    	public static final int PREPARING		    = 1;

    	/**
    	 * O pedido ja foi preparado, esperando para ser entregue
    	 */
    	public static final int READY_TO_DELIVER    = 2;

    	/**
    	 * O pedido ja saiu para entrega e logo deve chegar ao seu destino
    	 */
    	public static final int DELIVERING		    = 3;

    	/**
    	 * Pedido concluido!
    	 */
    	public static final int FINISHED		    = 4;
    }
}
