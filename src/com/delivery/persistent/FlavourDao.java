package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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


    private static final int NO_PRODUCT = (int) SQLUtils.INVALID_ID;

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
    public int update(List<Flavour> objectsToUpdate) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(List<Flavour> objectsToDelete) throws SQLException {
        // Nao e possivel remover sabor
        return 0;
    }

    @Override
    public List<Flavour> get(Flavour param) throws SQLException {
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
            Logger.error("FlavourDao.getLastSavedId", e);
            throw e;
        }
        return id;
    }

}
