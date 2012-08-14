package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import com.delivery.Logger;
import com.delivery.order.OrderItem;

public class OrderItemDao extends Dao<OrderItem> {
    private static final String TABLE_NAME = "item_pedido";

    private static final String COLUMN_ID = "cod_item_pedido";
    private static final String COLUMN_ORDER_ID = "pedido_cod_pedido";
    private static final String COLUMN_PRODUCT_ID = "produto_cod_produto";
    private static final String COLUMN_PRICE = "valor";
    private static final String COLUMN_SIZE = "tamanhos_cod_tamanho";

	public OrderItemDao(Connection connection) {
		super(connection);
	}

	@Override
	public int[] save(List<OrderItem> objectsToInsert) throws SQLException {
        if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
            		COLUMN_ID + ", " +
            		COLUMN_ORDER_ID + ", " +
            		COLUMN_PRODUCT_ID + ", " +
            		COLUMN_PRICE + ", " +
            		COLUMN_SIZE + ") " +
                    "VALUES (?,?,?,?,?)");

            for (OrderItem oi : objectsToInsert) {
        		stm.setInt(1, oi.getId());
        		stm.setLong(2, oi.getOrderId());
        		stm.setInt(3, oi.getProductId());
        		stm.setDouble(4, oi.getPrice());
        		stm.setInt(5, oi.getSize());

                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao adicionar item de pedido!", e);
            Logger.error("next: ", e.getNextException());
            throw e;
        }
        return inserted;
	}

	@Override
	public int update(OrderItem objectToUpdate) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(List<OrderItem> objectsToDelete) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<OrderItem> get(OrderItem param) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLastSavedId() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
