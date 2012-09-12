package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.Logger;
import com.delivery.order.Payment;
import com.delivery.util.SQLUtils;

public class PaymentDao extends Dao<Payment> {
    private static final String TABLE_NAME = "pagamento";

    private static final String COLUMN_ID = "cod_pagamento";
    private static final String COLUMN_ORDER_ID = "pedido_cod_pedido";
    private static final String COLUMN_REF = "ref";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_URL = "URLpagamento";
    private static final String COLUMN_MANUAL_VALUE = "valorCombinado";

    private static final String PRIMARY_KEY_SEQUENCE = "pagamento_cod_pagamento_seq";

    private static final int IND_COLUMN_ID = 1;
    private static final int IND_COLUMN_ORDER_ID = 2;
    private static final int IND_COLUMN_REF = 3;
    private static final int IND_COLUMN_STATUS = 4;
    private static final int IND_COLUMN_URL = 5;
    private static final int IND_COLUMN_MANUAL_VALUE = 6;

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
        String query = buildQuery(param);

        ArrayList<Payment> payments = null;
        Statement stm = null;
        try {
            stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            payments = new ArrayList<Payment>();
            while(rs.next()) {
                Payment p = new Payment();
                p.setId(rs.getLong(IND_COLUMN_ID));
                p.setOrderId(rs.getLong(IND_COLUMN_ORDER_ID));
                p.setPaymentStatus(rs.getInt(IND_COLUMN_STATUS));
                p.setReference(rs.getString(IND_COLUMN_REF));
                p.setURL(rs.getString(IND_COLUMN_URL));
                p.setManualPaymentValue(rs.getDouble(IND_COLUMN_MANUAL_VALUE));

                payments.add(p);
            }
        } catch (SQLException e) {
            Logger.error("Erro ao listar pagamentos", e);
            throw e;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }
        return payments;
	}

	public boolean setPaymentURL(long paymentId, String url) throws SQLException{
		try {
			Statement stm = mConnection.createStatement();
			int updated = stm.executeUpdate("UPDATE " + TABLE_NAME + " SET " + COLUMN_URL + "='" + url +
					"' WHERE " + COLUMN_ID + "=" + paymentId);
			return updated == 1;
		} catch(SQLException e) {
            Logger.error("PaymentDao.setPaymentURL", e);
            throw e;
		}
	}

	public boolean changePaymentStatus(long paymentId, int status) throws SQLException{
		if (!Payment.PaymentStatus.isValidState(status)) {
			throw new IllegalStateException();
		}
		try {
			Statement stm = mConnection.createStatement();
			int updated = stm.executeUpdate("UPDATE " + TABLE_NAME + " SET " + COLUMN_STATUS + "=" + status +
					" WHERE " + COLUMN_ID + "=" + paymentId);
			return updated == 1;
		} catch(SQLException e) {
            Logger.error("PaymentDao.changePaymentState", e);
            throw e;
		}
	}

	public boolean setPaymentManualValue(long paymentId, double value) throws SQLException{
		try {
			Statement stm = mConnection.createStatement();
			int updated = stm.executeUpdate("UPDATE " + TABLE_NAME + " SET " + COLUMN_MANUAL_VALUE + "=" + value +
					" WHERE " + COLUMN_ID + "=" + paymentId);
			return updated == 1;
		} catch(SQLException e) {
            Logger.error("PaymentDao.setPaymentManualValue", e);
            throw e;
		}
	}

	@Override
	public long getLastSavedId() throws SQLException {
		long id = 0;
        try {
            Statement stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT MAX(" + COLUMN_ID + ") FROM " + TABLE_NAME);
            if (rs.next()) {
                id = rs.getLong(1);
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
            // Agora vamos voltar a sequencia para o valor original (antes da consulta)
            rs = stm.executeQuery("select setval ('" + PRIMARY_KEY_SEQUENCE + "', " + (val - 1) + ")");
        } catch (SQLException e) {
            Logger.error("PaymentDao.getNextSeqVal", e);
            throw e;
        }
        return val;
	}


    private String buildQuery(Payment param) {
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
        }

        return queryBuilder.toString();
    }

}
