package com.delivery.persistent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.Logger;
import com.delivery.admin.Admin;
import com.delivery.util.StringUtils;

public class AdminDao extends Dao<Admin> {

	private static final String TABLE_NAME = "admin";

    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_PWD = "password";

    private static final int IND_COLUMN_USER_NAME = 1;
    private static final int IND_COLUMN_PWD = 2;


	public AdminDao(Connection connection) {
		super(connection);
	}

	@Override
	public int[] save(List<Admin> objectsToInsert) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int update(Admin objectToUpdate) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int delete(List<Admin> objectsToDelete) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Admin> get(Admin param) throws SQLException {
		String query = buildQuery(param);
	    ArrayList<Admin> admin = null;
	    Statement stm = null;
	    try {
	        stm = mConnection.createStatement();
	        ResultSet rs = stm.executeQuery(query);
	        admin = new ArrayList<Admin>();
	        while(rs.next()) {
	        	Admin adm = new Admin();
	            adm.setUserName(rs.getString(IND_COLUMN_USER_NAME));
	            adm.setPassword(rs.getString(IND_COLUMN_PWD));

	            admin.add(adm);
	        }
	    } catch (SQLException e) {
	        Logger.error("Erro ao listar admins", e);
	        throw e;
	    } finally {
	        if (stm != null) {
	            try {
	                stm.close();
	            } catch (SQLException ignore) { }
	        }
	    }
	    return admin;
	}

    private String buildQuery(Admin param) {
        final String where = " WHERE";
        final String and = " AND";
        String nextToken = where;
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + TABLE_NAME);
        if (param != null) {
            if (!StringUtils.isEmpty(param.getUserName())) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_USER_NAME + " = '" + param.getUserName() + "'");
                nextToken = and;
            }
            if (!StringUtils.isEmpty(param.getPassword())) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_PWD + " = '" + param.getPassword() + "'");
                nextToken = and;
            }
        }

        return queryBuilder.toString();
    }

	@Override
	public int getLastSavedId() throws SQLException {
		throw new UnsupportedOperationException();
	}

}
