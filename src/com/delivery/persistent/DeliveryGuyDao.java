package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.Logger;
import com.delivery.order.DeliveryGuy;
import com.delivery.util.SQLUtils;
import com.delivery.util.StringUtils;

public class DeliveryGuyDao extends Dao<DeliveryGuy> {
    private static final String TABLE_NAME = "entregador";

    private static final String COLUMN_ID        = "cod_entregador";
    private static final String COLUMN_PASSWORD  = "senha";
    private static final String COLUMN_TOKEN     = "token";
    private static final String COLUMN_NAME      = "nome";

    private static final String PRIMARY_KEY_SEQUENCE = "entregador_cod_entregador_seq";

    private static final int IND_COLUMN_ID        = 1;
    private static final int IND_COLUMN_PASSWORD  = 2;
    private static final int IND_COLUMN_TOKEN     = 3;
    private static final int IND_COLUMN_NAME      = 4;

	public DeliveryGuyDao(Connection connection) {
		super(connection);
	}

	@Override
	public int[] save(List<DeliveryGuy> objectsToInsert) throws SQLException {
		if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
            		COLUMN_PASSWORD + ", " +
            		COLUMN_TOKEN + ", " +
            		COLUMN_NAME + ") " +
                    "VALUES (?,?,?)");
            for (DeliveryGuy dG : objectsToInsert) {
                if (StringUtils.isEmpty(dG.getName())) {
                    throw new IllegalArgumentException("Tentando adicionar entregador sem nome!");
                }
                stm.setString(1, dG.getPassword());
                stm.setString(2, dG.getToken());
                stm.setString(3, dG.getName());
                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao adicionar entregador!", e);
            throw e;
        }
        return inserted;
	}

	public long getNextSequenceVal() throws SQLException {
		int val = 0;
        try {
            Statement stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery("select nextval ('" + PRIMARY_KEY_SEQUENCE + "')");
            if (rs.next()) {
                val = rs.getInt(1);
            }
            if (val == 1) {
            	// Para evitar setarmos um valor invalido de sequencia (0)
            	// Vamos comecar do 2
            	val = 2;
            }
            // Agora vamos voltar a sequencia para o valor original (antes da consulta)
            rs = stm.executeQuery("select setval ('" + PRIMARY_KEY_SEQUENCE + "', " + (val - 1) + ")");
        } catch (SQLException e) {
            Logger.error("DeliveryGuyDao.getNextSeqVal", e);
            throw e;
        }
        return val;
	}

	@Override
	public int update(DeliveryGuy objectToUpdate) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(List<DeliveryGuy> objectsToDelete) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<DeliveryGuy> get(DeliveryGuy param) throws SQLException {
        String query = buildQuery(param);

        ArrayList<DeliveryGuy> deliveryGuys = null;
        Statement stm = null;
        try {
            stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            deliveryGuys = new ArrayList<DeliveryGuy>();
            while(rs.next()) {
            	DeliveryGuy dG = new DeliveryGuy();
                dG.setCode(rs.getInt(IND_COLUMN_ID));
                dG.setName(rs.getString(IND_COLUMN_NAME));
                dG.setPassword(rs.getString(IND_COLUMN_PASSWORD));
                dG.setToken(rs.getString(IND_COLUMN_TOKEN));

                deliveryGuys.add(dG);
            }
        } catch (SQLException e) {
            Logger.error("Erro ao listar entregadores", e);
            throw e;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }
        return deliveryGuys;
	}

    private String buildQuery(DeliveryGuy param) {
        final String where = " WHERE";
        final String and = " AND";
        String nextToken = where;
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + TABLE_NAME);
        if (param != null) {
            if (param.getCode() != SQLUtils.INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_ID + " = " + param.getCode());
                nextToken = and;
            }
            if (!StringUtils.isEmpty(param.getName())) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_NAME + " = '" + param.getName() + "'");
                nextToken = and;
            }
            if (param.isTokenValidForQuery()) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_TOKEN + " = '" + param.getToken() + "'");
                nextToken = and;
            }
            if (!StringUtils.isEmpty(param.getPassword())) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_PASSWORD + " = '" + param.getPassword() + "'");
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
