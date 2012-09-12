package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.Logger;
import com.delivery.order.OrderItem;
import com.delivery.util.SQLUtils;

public class OrderItemDao extends Dao<OrderItem> {
    private static final String TABLE_NAME = "item_pedido";

    private static final String COLUMN_ID = "cod_item_pedido";
    private static final String COLUMN_ORDER_ID = "pedido_cod_pedido";
    private static final String COLUMN_PRODUCT_ID = "produto_cod_produto";
    private static final String COLUMN_PRICE = "valor";
    private static final String COLUMN_SIZE = "tamanhos_cod_tamanho";

    private static final int IND_COLUMN_ID = 1;
    private static final int IND_COLUMN_ORDER_ID = 2;
    private static final int IND_COLUMN_PRODUCT_ID = 3;
    private static final int IND_COLUMN_PRICE = 4;
    private static final int IND_COLUMN_SIZE = 5;

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
        String query = buildQuery(param);

        ArrayList<OrderItem> itens = null;
        Statement stm = null;
        try {
            stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            itens = new ArrayList<OrderItem>();
            while(rs.next()) {
            	OrderItem oi = new OrderItem();
            	oi.setId(rs.getInt(IND_COLUMN_ID));
            	oi.setOrderId(rs.getLong(IND_COLUMN_ORDER_ID));
            	oi.setPrice(rs.getDouble(IND_COLUMN_PRICE));
            	oi.setProductId(rs.getInt(IND_COLUMN_PRODUCT_ID));
            	oi.setSize(rs.getInt(IND_COLUMN_SIZE));

                itens.add(oi);
            }
        } catch (SQLException e) {
            Logger.error("Erro ao listar itens de pedido", e);
            throw e;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }
        return itens;
	}

	private String buildQuery(OrderItem param) {
        final String where = " WHERE";
        final String and = " AND";
        String nextToken = where;
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + TABLE_NAME);
        if (param != null) {
            if (param.getOrderId() != SQLUtils.INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_ORDER_ID + " = " + param.getOrderId());
                nextToken = and;
            }
            if (param.getId() != SQLUtils.INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_ID + " = " + param.getId());
                nextToken = and;
            }
            if (param.getProductId() != SQLUtils.INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_PRODUCT_ID + " = " + param.getProductId());
                nextToken = and;
            }
        }

        return queryBuilder.toString();
    }

	@Override
	public long getLastSavedId() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

}
