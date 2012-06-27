package com.delivery.order;

import java.util.ArrayList;
import java.util.List;

public class Order {

    // Usado enquanto o pedido esta na memoria
    List<OrderItem> mItems;
    int mNextTemporaryId = 0;

    public List<OrderItem> getItems() {
        return mItems;
    }

    public void setItems(List<OrderItem> items) {
        mItems = items;
    }

    public void addItem(OrderItem item) {
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
        if (mItems != null) {
            mItems.clear();
        }
    }
}
