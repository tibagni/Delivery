package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import com.delivery.Logger;
import com.delivery.menu.Product;
import com.delivery.util.StringUtils;

public class ProductDao extends Dao<Product> {
    private static final String TABLE_NAME = "produto";

    private static final String COLUMN_ID        = "cod_produto";
    private static final String COLUMN_CATEGORY  = "categoria_cod_categoria";
    private static final String COLUMN_NAME      = "nome";
    private static final String COLUMN_DESC      = "descricao";
    private static final String COLUMN_FLAVOURS  = "qtde_sabores_perm";
    private static final String COLUMN_OPTIONALS = "qtde_opcionais_perm";
    private static final String COLUMN_PICTURE   = "foto";

    private static final int NO_CATEGORY = 0;

    public ProductDao(Connection connection) {
        super(connection);
    }

    @Override
    public int[] save(List<Product> objectsToInsert) throws SQLException {
        if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
                    COLUMN_NAME + ", " +
                    COLUMN_DESC + ", " +
                    COLUMN_FLAVOURS + ", " +
                    COLUMN_OPTIONALS + ", " +
                    COLUMN_CATEGORY + ", " +
                    COLUMN_PICTURE + ") " +
                    "VALUES (?,?,?,?,?,?)");
            for (Product prod : objectsToInsert) {
                if (StringUtils.isEmpty(prod.getName())) {
                    throw new IllegalArgumentException("Tentando adicionar produto sem nome!");
                }
                if (prod.getCategory() == NO_CATEGORY) {
                    throw new IllegalArgumentException("Tentando adicionar produto sem categoria!");
                }
                stm.setString(1, prod.getName());
                stm.setString(2, prod.getDescription());
                stm.setInt(3, prod.getFlavoursPerOrder());
                stm.setInt(4, prod.getOptionalsPerOrder());
                stm.setInt(5, prod.getCategory());
                stm.setString(6, prod.getPicturePath());
                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao adicionar produto!", e);
            throw e;
        }
        return inserted;
    }

    @Override
    public int update(List<Product> objectsToUpdate) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(List<Product> objectsToDelete) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Product> get(Product param) {
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
            Logger.error("ProductDao.getLastSavedId", e);
            throw e;
        }
        return id;
    }
}
