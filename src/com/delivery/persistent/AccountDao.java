package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.Logger;
import com.delivery.account.UserAccount;
import com.delivery.util.StringUtils;

public class AccountDao extends Dao<UserAccount> {
    private static final String TABLE_NAME = "conta_cliente";

    private static final String COLUMN_ID = "cpf";
    private static final String COLUMN_NAME = "nome";
    private static final String COLUMN_TEL = "telefone";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PWD = "senha";

    private static final int IND_COLUMN_ID = 1;
    private static final int IND_COLUMN_NAME = 2;
    private static final int IND_COLUMN_TEL = 3;
    private static final int IND_COLUMN_EMAIL = 4;
    private static final int IND_COLUMN_PWD = 5;

	public AccountDao(Connection connection) {
		super(connection);
	}

	@Override
	public int[] save(List<UserAccount> objectsToInsert) throws SQLException {
        if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
            		COLUMN_ID + ", " +
            		COLUMN_NAME + ", " +
            		COLUMN_TEL + ", " +
            		COLUMN_EMAIL + ", " +
            		COLUMN_PWD + ") " +
                    "VALUES (?,?,?,?,?)");
            for (UserAccount acc : objectsToInsert) {
                if (acc.getCpf() == 0) {
                    throw new IllegalArgumentException("Tentando adicionar conta sem CPF do cliente!");
                }
                if (StringUtils.isEmpty(acc.getName())) {
                    throw new IllegalArgumentException("Tentando adicionar conta sem nome do cliente!");
                }
                stm.setLong(1, acc.getCpf());
                stm.setString(2, acc.getName());
                stm.setString(3, acc.getTel());
                stm.setString(4, acc.getEmail());
                stm.setString(5, acc.getPassword());
                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao adicionar conta de usuario!", e);
            throw e;
        }
        return inserted;
	}

	@Override
	public int update(UserAccount objectToUpdate) throws SQLException {
        if (objectToUpdate == null) {
            return 0;
        }

        int updated = 0;
        Statement stm = null;
        try {
            if (objectToUpdate.getCpf() == 0) {
                // Nada para atualizar
                return 0;
            }

            String sql = "UPDATE " + TABLE_NAME + " SET " +
                    COLUMN_NAME + "='" + objectToUpdate.getName() + "', " +
                    COLUMN_PWD + "='" + objectToUpdate.getPassword() + "', " +
                    COLUMN_TEL + "='" + objectToUpdate.getTel() + "', " + " WHERE " +
                    COLUMN_ID + "=" + objectToUpdate.getCpf();
            stm = mConnection.createStatement();
            updated = stm.executeUpdate(sql);
        } catch (SQLException e) {
            Logger.error("Erro ao editar conta", e);
            throw e;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }
        return updated;
	}

	@Override
	public int delete(List<UserAccount> objectsToDelete) throws SQLException {
        Logger.error("Nao e possivel remover um produto");
		return 0;
	}

	@Override
	public List<UserAccount> get(UserAccount param) throws SQLException {
		String query = buildQuery(param);
	    ArrayList<UserAccount> accounts = null;
	    Statement stm = null;
	    try {
	        stm = mConnection.createStatement();
	        ResultSet rs = stm.executeQuery(query);
	        accounts = new ArrayList<UserAccount>();
	        while(rs.next()) {
	            UserAccount acc = new UserAccount();
	            acc.setCpf(rs.getLong(IND_COLUMN_ID));
	            acc.setName(rs.getString(IND_COLUMN_NAME));
	            acc.setTel(rs.getString(IND_COLUMN_TEL));
	            acc.setEmail(rs.getString(IND_COLUMN_EMAIL));
	            acc.setPassword(rs.getString(IND_COLUMN_PWD));

	            accounts.add(acc);
	        }
	    } catch (SQLException e) {
	        Logger.error("Erro ao listar contas", e);
	        throw e;
	    } finally {
	        if (stm != null) {
	            try {
	                stm.close();
	            } catch (SQLException ignore) { }
	        }
	    }
	    return accounts;
	}

	@Override
	public int getLastSavedId() throws SQLException {
		throw new UnsupportedOperationException();
	}

    private String buildQuery(UserAccount param) {
        final String where = " WHERE";
        final String and = " AND";
        String nextToken = where;
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + TABLE_NAME);
        if (param != null) {
            if (param.getCpf() > 0) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_ID + " = '" + param.getCpf() + "'");
                nextToken = and;
            }
            if (!StringUtils.isEmpty(param.getEmail())) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_EMAIL + " = '" + param.getEmail() + "'");
                nextToken = and;
            }
            if (!StringUtils.isEmpty(param.getPassword())) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_PWD + " = '" + param.getPassword() + "'");
                nextToken = and;
            }
            if (!StringUtils.isEmpty(param.getTel())) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_TEL + " = " + param.getTel());
                nextToken = and;
            }
        }

        return queryBuilder.toString();
    }

    public boolean changeAccountPwd(long accId, String newPwd) throws SQLException {
    	Statement stm = null;
        try {
            stm = mConnection.createStatement();
            int result = stm.executeUpdate("UPDATE " + TABLE_NAME + " SET " + COLUMN_PWD + "='" + newPwd +
            		"' WHERE " + COLUMN_ID + "=" + accId);

            return result > 0;
        } catch (SQLException e) {
            Logger.error("Erro ao atualizar senha", e);
            throw e;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }
    }

    public boolean changeAccountTel(long accId, String newTel) throws SQLException {
    	Statement stm = null;
        try {
            stm = mConnection.createStatement();
            int result = stm.executeUpdate("UPDATE " + TABLE_NAME + " SET " + COLUMN_TEL + "='" + newTel +
            		"' WHERE " + COLUMN_ID + "=" + accId);

            return result > 0;
        } catch (SQLException e) {
            Logger.error("Erro ao atualizar Telefone", e);
            throw e;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }
    }
}
