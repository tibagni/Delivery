package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.delivery.Logger;
import com.delivery.order.Payment;

public class PaymentDao extends Dao<Payment> {
    private static final String TABLE_NAME = "pagamento";

    private static final String COLUMN_ID = "cod_pagamento";
    private static final String COLUMN_ORDER_ID = "pedido_cod_pedido";
    private static final String COLUMN_REF = "ref";
    private static final String COLUMN_STATUS = "status";

    private static final String PRIMARY_KEY_SEQUENCE = "pagamento_cod_pagamento_seq";

    private static final int IND_COLUMN_ID = 1;
    private static final int IND_COLUMN_ORDER_ID = 2;
    private static final int IND_COLUMN_REF = 3;
    private static final int IND_COLUMN_STATUS = 4;

	public PaymentDao(Connection connection) {
		super(connection);
	}

	@Override
	public int[] save(List<Payment> objectsToInsert) throws SQLException {
		if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
            		COLUMN_ORDER_ID + ", " +
            		COLUMN_REF + ", " +
            		COLUMN_STATUS + ") " +
                    "VALUES (?,?,?)");

            for (Payment p : objectsToInsert) {
            	stm.setLong(1, p.getOrderId());
        		stm.setString(2, p.getReference());
        		stm.setInt(3, p.getPaymentStatus());

                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao adicionar pagamento!", e);
            Logger.error("next: ", e.getNextException());
            throw e;
        }
        return inserted;
	}

	@Override
	public int update(Payment objectToUpdate) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(List<Payment> objectsToDelete) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Payment> get(Payment param) throws SQLException {
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
            Logger.error("PaymentDao.getLastSavedId", e);
            throw e;
        }
        return id;
	}

	public long getNextSequenceVal() throws SQLException {
		int val = 0;
        try {
            Statement stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery("select nextval ('" + PRIMARY_KEY_SEQUENCE + "')");
            if (rs.next()) {
                val = rs.getInt(1);
            }
        } catch (SQLException e) {
            Logger.error("PaymentDao.getNextSeqVal", e);
            throw e;
        }
        return val;
	}

}
