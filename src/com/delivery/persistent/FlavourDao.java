package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.Logger;
import com.delivery.menu.Flavour;
import com.delivery.util.SQLUtils;
import com.delivery.util.StringUtils;

public class FlavourDao extends Dao<Flavour> {
    private static final String TABLE_NAME = "sabor";

    private static final String COLUMN_ID        = "cod_sabor";
    private static final String COLUMN_PRODUCT   = "produto_cod_produto";
    private static final String COLUMN_NAME      = "nome";
    private static final String COLUMN_DESC      = "descricao";
    private static final String COLUMN_PICTURE   = "foto";

    private static final int IND_COLUMN_ID        = 1;
    private static final int IND_COLUMN_PRODUCT   = 2;
    private static final int IND_COLUMN_NAME      = 3;
    private static final int IND_COLUMN_DESC      = 4;
    private static final int IND_COLUMN_PICTURE   = 5;


    private static final int INVALID_ID = (int) SQLUtils.INVALID_ID;
    private static final int NO_PRODUCT = INVALID_ID;

    public FlavourDao(Connection connection) {
        super(connection);
    }

    @Override
    public int[] save(List<Flavour> objectsToInsert) throws SQLException {
        if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
                    COLUMN_PRODUCT + ", " +
                    COLUMN_NAME + ", " +
                    COLUMN_DESC + ", " +
                    COLUMN_PICTURE + ") " +
                    "VALUES (?,?,?,?)");
            for (Flavour flav : objectsToInsert) {
                if (StringUtils.isEmpty(flav.getName())) {
                    throw new IllegalArgumentException("Tentando adicionar sabor sem nome!");
                }
                if (flav.getProductId() == NO_PRODUCT) {
                    throw new IllegalArgumentException("Tentando adicionar sabor sem produto!");
                }
                stm.setInt(1, flav.getProductId());
                stm.setString(2, flav.getName());
                stm.setString(3, flav.getDescription());
                stm.setString(4, flav.getPicturePath());
                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao adicionar sabor!", e);
            throw e;
        }
        return inserted;
    }

    @Override
    public int update(Flavour objectToUpdate) throws SQLException {
        if (objectToUpdate == null) {
            return 0;
        }

        int updated = 0;
        Statement stm = null;
        try {
            if (objectToUpdate.getId() == INVALID_ID ||
                    StringUtils.isEmpty(objectToUpdate.getName())) {
                // Nada para atualizar
                return 0;
            }

            String sql = "UPDATE " + TABLE_NAME + " SET " +
                    COLUMN_NAME + "='" + objectToUpdate.getName() + "', " +
                    COLUMN_DESC + "='" + objectToUpdate.getDescription() + "', " +
                    COLUMN_PICTURE + "='" + objectToUpdate.getPicturePath() + "' WHERE " +
                    COLUMN_ID + "=" + objectToUpdate.getId();
            stm = mConnection.createStatement();
            updated = stm.executeUpdate(sql);
        } catch (SQLException e) {
            Logger.error("Erro ao editar sabor", e);
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
    public int delete(List<Flavour> objectsToDelete) throws SQLException {
        // Nao e possivel remover sabor
        return 0;
    }

    @Override
    public List<Flavour> get(Flavour param) throws SQLException {
        String query = buildQuery(param);

        ArrayList<Flavour> flavours = null;
        Statement stm = null;
        try {
            stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            flavours = new ArrayList<Flavour>();
            while(rs.next()) {
                Flavour f = new Flavour();
                int id = rs.getInt(IND_COLUMN_ID);
                f.setId(id);
                f.setProductId(rs.getInt(IND_COLUMN_PRODUCT));
                f.setName(rs.getString(IND_COLUMN_NAME));
                f.setDescription(rs.getString(IND_COLUMN_DESC));
                f.setPicturePath(rs.getString(IND_COLUMN_PICTURE));

                flavours.add(f);
            }
        } catch (SQLException e) {
            Logger.error("Erro ao listar sabores", e);
            throw e;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }
        return flavours;
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
            Logger.error("FlavourDao.getLastSavedId", e);
            throw e;
        }
        return id;
    }

    private String buildQuery(Flavour param) {
        final String where = " WHERE";
        final String and = " AND";
        String nextToken = where;
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + TABLE_NAME);
        if (param != null) {
            if (param.getProductId() != NO_PRODUCT) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_PRODUCT + " = " + param.getProductId());
                nextToken = and;
            }
            if (param.getId() != INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_ID + " = " + param.getId());
                nextToken = and;
            }
            if (!StringUtils.isEmpty(param.getName())) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_NAME + " = '" + param.getName() + "'");
                nextToken = and;
            }
        }

        return queryBuilder.toString();
    }

}
