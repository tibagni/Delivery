package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.Logger;
import com.delivery.account.Address;
import com.delivery.util.SQLUtils;
import com.delivery.util.StringUtils;

public class AddressDao extends Dao<Address> {
    private static final String TABLE_NAME = "endereco";

    private static final String COLUMN_ID = "cod_endereco";
    private static final String COLUMN_ZIP = "cep";
    private static final String COLUMN_STREET = "rua";
    private static final String COLUMN_NEIGHB = "bairro";
    private static final String COLUMN_CITY = "cidade";
    private static final String COLUMN_COMPL = "complemento";
    private static final String COLUMN_UF = "uf";
    private static final String COLUMN_USER_ACC = "conta_cliente_cpf";
    private static final String COLUMN_NUMBER = "numero";

    private static final int IND_COLUMN_STREET = 1;
    private static final int IND_COLUMN_ID = 2;
    private static final int IND_COLUMN_NEIGHB = 3;
    private static final int IND_COLUMN_CITY = 4;
    private static final int IND_COLUMN_UF = 5;
    private static final int IND_COLUMN_COMPL = 6;
    private static final int IND_COLUMN_NUMBER = 7;
    private static final int IND_COLUMN_USER_ACC = 8;
    private static final int IND_COLUMN_ZIP = 9;

	public AddressDao(Connection connection) {
		super(connection);
	}

	@Override
	public int[] save(List<Address> objectsToInsert) throws SQLException {
        if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
            		COLUMN_ZIP + ", " +
            		COLUMN_STREET + ", " +
            		COLUMN_NEIGHB + ", " +
            		COLUMN_CITY + ", " +
            		COLUMN_COMPL + ", " +
            		COLUMN_UF + ", " +
            		COLUMN_USER_ACC + ", " +
            		COLUMN_NUMBER + ") " +
                    "VALUES (?,?,?,?,?,?,?,?)");
            for (Address add : objectsToInsert) {
                stm.setString(1, add.getZipCode());
                stm.setString(2, add.getStreet());
                stm.setString(3, add.getNeighborhood());
                stm.setString(4, add.getCity());
                stm.setString(5, add.getCompl());
                stm.setString(6, add.getUF());
                stm.setLong(7, add.getUserAccountId());
                stm.setInt(8, add.getNumber());
                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao adicionar endereco!", e);
            Logger.error("next: ", e.getNextException());
            throw e;
        }
        return inserted;
	}

	@Override
	public int update(Address objectToUpdate) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(List<Address> objectsToDelete) throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Address> get(Address param) throws SQLException {
		String query = buildQuery(param);
	    ArrayList<Address> addrs = null;
	    Statement stm = null;
	    try {
	        stm = mConnection.createStatement();
	        ResultSet rs = stm.executeQuery(query);
	        addrs = new ArrayList<Address>();
	        while(rs.next()) {
	        	Address addr = new Address();
	        	addr.setId(rs.getInt(IND_COLUMN_ID));
	        	addr.setCity(rs.getString(IND_COLUMN_CITY));
	        	addr.setCompl(rs.getString(IND_COLUMN_COMPL));
	        	addr.setNeighborhood(rs.getString(IND_COLUMN_NEIGHB));
	        	addr.setNumber(rs.getInt(IND_COLUMN_NUMBER));
	        	addr.setStreet(rs.getString(IND_COLUMN_STREET));
	        	addr.setUF(rs.getString(IND_COLUMN_UF));
	        	addr.setUserAccountId(IND_COLUMN_USER_ACC);
	        	addr.setZipCode(rs.getString(IND_COLUMN_ZIP));

	            addrs.add(addr);
	        }
	    } catch (SQLException e) {
	        Logger.error("Erro ao listar enderecos", e);
	        throw e;
	    } finally {
	        if (stm != null) {
	            try {
	                stm.close();
	            } catch (SQLException ignore) { }
	        }
	    }
	    return addrs;
	}

	@Override
	public long getLastSavedId() throws SQLException {
        int id = 0;
        try {
            Statement stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery("SELECT MAX(" + COLUMN_ID + ") FROM " + TABLE_NAME);
            if (rs.next()) {
                id = rs.getInt(1);
            }
        } catch (SQLException e) {
            Logger.error("AddressDao.getLastSavedId", e);
            throw e;
        }
        return id;
	}

    private String buildQuery(Address param) {
        final String where = " WHERE";
        final String and = " AND";
        String nextToken = where;
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + TABLE_NAME);
        if (param != null) {
            if (param.getId() != SQLUtils.INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_ID + " = " + param.getId());
                nextToken = and;
            }
            if (param.getUserAccountId() != SQLUtils.INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_USER_ACC + " = " + param.getUserAccountId());
                nextToken = and;
            }
            if (!StringUtils.isEmpty(param.getZipCode())) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_ZIP + " = '" + param.getZipCode() + "'");
                nextToken = and;
            }
        }

        return queryBuilder.toString();
    }
}
