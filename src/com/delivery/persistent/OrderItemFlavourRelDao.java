package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.delivery.Logger;
import com.delivery.order.OrderItemRelFlavour;

public class OrderItemFlavourRelDao extends Dao<OrderItemRelFlavour> {
    private static final String TABLE_NAME = "item_pedido_has_sabor";

    private static final String COLUMN_ORDER_ITEM_ID = "item_pedido_cod_item_pedido";
    private static final String COLUMN_FLAVOUR_ID = "sabor_cod_sabor";
    private static final String COLUMN_ORDER_ID = "item_pedido_pedido_cod_pedido";

	public OrderItemFlavourRelDao(Connection connection) {
		super(connection);
	}

	@Override
	public int[] save(List<OrderItemRelFlavour> objectsToInsert)
			throws SQLException {
        if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
            		COLUMN_ORDER_ITEM_ID + ", " +
            		COLUMN_FLAVOUR_ID + ", " +
            		COLUMN_ORDER_ID + ") " +
                    "VALUES (?,?,?)");

            for (OrderItemRelFlavour rel : objectsToInsert) {
        		stm.setInt(1, rel.getOrderItemId());
        		stm.setInt(2, rel.getFlavourId());
        		stm.setLong(3, rel.getOrderId());

                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao adicionar relacionamento entre item de pedido e sabor!", e);
            Logger.error("next: ", e.getNextException());
            throw e;
        }
        return inserted;
	}

	@Override
	public int update(OrderItemRelFlavour objectToUpdate) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(List<OrderItemRelFlavour> objectsToDelete)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<OrderItemRelFlavour> get(OrderItemRelFlavour param)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLastSavedId() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
