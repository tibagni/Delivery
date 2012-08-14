package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.delivery.Logger;
import com.delivery.order.Order;
import com.delivery.util.SQLUtils;

public class OrderDao extends Dao<Order> {
    private static final String TABLE_NAME = "pedido";

    private static final String COLUMN_ID = "cod_pedido";
    private static final String COLUMN_DELIVERY = "entregador_cod_entregador";
    private static final String COLUMN_ADDRESS = "endereco_cod_endereco";
    private static final String COLUMN_ACCOUNT_ID = "cliente_cpf";
    private static final String COLUMN_STATUS = "situacao";
    private static final String COLUMN_PRICE = "valor";

    private static final int IND_COLUMN_ID = 1;
    private static final int IND_COLUMN_DELIVERY = 2;
    private static final int IND_COLUMN_ADDRESS = 3;
    private static final int IND_COLUMN_ACCOUNT_ID = 4;
    private static final int IND_COLUMN_STATUS = 5;
    private static final int IND_COLUMN_PRICE = 6;

	public OrderDao(Connection connection) {
		super(connection);
	}

	@Override
	public int[] save(List<Order> objectsToInsert) throws SQLException {
        if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
            		COLUMN_DELIVERY + ", " +
            		COLUMN_ADDRESS + ", " +
            		COLUMN_ACCOUNT_ID + ", " +
            		COLUMN_STATUS + ", " +
            		COLUMN_PRICE + ") " +
                    "VALUES (?,?,?,?,?)");

            for (Order ord : objectsToInsert) {
            	if (ord.getDeliveryGuyId() != SQLUtils.INVALID_ID) {
            		stm.setInt(1, ord.getDeliveryGuyId());
            	} else {
            		stm.setNull(1,java.sql.Types.INTEGER);
            	}

        		stm.setInt(2, ord.getAddressId());
        		stm.setLong(3, ord.getUserAccountId());
        		stm.setInt(4, ord.getStatus());
        		stm.setDouble(5, ord.getPrice());

                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao adicionar pedido!", e);
            Logger.error("next: ", e.getNextException());
            throw e;
        }
        return inserted;
	}

	@Override
	public int update(Order objectToUpdate) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(List<Order> objectsToDelete) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Order> get(Order param) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLastSavedId() throws SQLException {
        int id = 0;
        try {
            Statement stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT MAX(" + COLUMN_ID + ") FROM " + TABLE_NAME);
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            Logger.error("OrderDao.getLastSavedId", e);
            throw e;
        }
        return id;
	}

}
