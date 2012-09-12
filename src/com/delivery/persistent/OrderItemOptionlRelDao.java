package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.Logger;
import com.delivery.order.OrderItemRelOptional;
import com.delivery.util.SQLUtils;

public class OrderItemOptionlRelDao extends Dao<OrderItemRelOptional> {
    private static final String TABLE_NAME = "item_pedido_has_opcional";

    private static final String COLUMN_ORDER_ITEM_ID = "item_pedido_cod_item_pedido";
    private static final String COLUMN_OPTIONAL_ID = "opcional_cod_opcional";
    private static final String COLUMN_ORDER_ID = "item_pedido_pedido_cod_pedido";

    private static final int IND_COLUMN_ORDER_ITEM_ID = 1;
    private static final int IND_COLUMN_OPTIONAL_ID = 2;
    private static final int IND_COLUMN_ORDER_ID = 3;

	public OrderItemOptionlRelDao(Connection connection) {
		super(connection);
	}

	@Override
	public int[] save(List<OrderItemRelOptional> objectsToInsert)
			throws SQLException {
        if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
            		COLUMN_ORDER_ITEM_ID + ", " +
            		COLUMN_OPTIONAL_ID + ", " +
            		COLUMN_ORDER_ID + ") " +
                    "VALUES (?,?,?)");

            for (OrderItemRelOptional rel : objectsToInsert) {
        		stm.setInt(1, rel.getOrderItemId());
        		stm.setInt(2, rel.getOptionalId());
        		stm.setLong(3, rel.getOrderId());

                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao adicionar relacionamento entre item de pedido e opcional!", e);
            Logger.error("next: ", e.getNextException());
            throw e;
        }
        return inserted;
	}

	@Override
	public int update(OrderItemRelOptional objectToUpdate) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(List<OrderItemRelOptional> objectsToDelete)
			throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<OrderItemRelOptional> get(OrderItemRelOptional param) throws SQLException {
        String query = buildQuery(param);

        ArrayList<OrderItemRelOptional> rels = null;
        Statement stm = null;
        try {
            stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            rels = new ArrayList<OrderItemRelOptional>();
            while(rs.next()) {
            	OrderItemRelOptional rel = new OrderItemRelOptional();
            	rel.setOptionalId(rs.getInt(IND_COLUMN_OPTIONAL_ID));
            	rel.setOrderId(rs.getLong(IND_COLUMN_ORDER_ID));
            	rel.setOrderItemId(rs.getInt(IND_COLUMN_ORDER_ITEM_ID));

                rels.add(rel);
            }
        } catch (SQLException e) {
            Logger.error("Erro ao listar relacionamentos entre item de pedido e opcional", e);
            throw e;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }
        return rels;
	}

	private String buildQuery(OrderItemRelOptional param) {
        final String where = " WHERE";
        final String and = " AND";
        String nextToken = where;
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + TABLE_NAME);
        if (param != null) {
            if (param.getOptionalId() != SQLUtils.INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_OPTIONAL_ID + " = " + param.getOptionalId());
                nextToken = and;
            }
            if (param.getOrderId() != SQLUtils.INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_ORDER_ID + " = " + param.getOrderId());
                nextToken = and;
            }
            if (param.getOrderItemId() != SQLUtils.INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_ORDER_ITEM_ID + " = " + param.getOrderItemId());
                nextToken = and;
            }
        }

        return queryBuilder.toString();
    }

	@Override
	public long getLastSavedId() throws SQLException {
		throw new UnsupportedOperationException();
	}

}
