package com.delivery.order;

import java.util.ArrayList;
import java.util.List;

public class Order {

    // Usado enquanto o pedido esta na memoria
    List<OrderItem> mItems;

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
        mItems.add(item);
    }
}
