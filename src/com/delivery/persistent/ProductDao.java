package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.Logger;
import com.delivery.menu.Product;
import com.delivery.menu.ProductSize;
import com.delivery.util.SQLUtils;
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

    private static final int IND_COLUMN_ID        = 1;
    private static final int IND_COLUMN_CATEGORY  = 2;
    private static final int IND_COLUMN_NAME      = 3;
    private static final int IND_COLUMN_DESC      = 4;
    private static final int IND_COLUMN_FLAVOURS  = 5;
    private static final int IND_COLUMN_OPTIONALS = 6;
    private static final int IND_COLUMN_PICTURE   = 7;

    private static final int INVALID_ID  = (int) SQLUtils.INVALID_ID;
    private static final int NO_CATEGORY = INVALID_ID;

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
                if (prod.getCategoryId() == NO_CATEGORY) {
                    throw new IllegalArgumentException("Tentando adicionar produto sem categoria!");
                }
                stm.setString(1, prod.getName());
                stm.setString(2, prod.getDescription());
                stm.setInt(3, prod.getFlavoursPerOrder());
                stm.setInt(4, prod.getOptionalsPerOrder());
                stm.setInt(5, prod.getCategoryId());
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
        Logger.error("Nao e possivel remover um produto");
        return 0;
    }

    @Override
    public List<Product> get(Product param) throws SQLException {
        String query = buildQuery(param);

        ArrayList<Product> products = null;
        Statement stm = null;
        try {
            stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            products = new ArrayList<Product>();
            while(rs.next()) {
                Product p = new Product();
                p.setCategoryId(rs.getInt(IND_COLUMN_CATEGORY));
                p.setDecription(rs.getString(IND_COLUMN_DESC));
                p.setFlavoursPerOrder(rs.getInt(IND_COLUMN_FLAVOURS));
                int id = rs.getInt(IND_COLUMN_ID);
                p.setId(id);
                p.setName(rs.getString(IND_COLUMN_NAME));
                p.setOptionalsPerOrder(rs.getInt(IND_COLUMN_OPTIONALS));
                p.setPicturePath(rs.getString(IND_COLUMN_PICTURE));

                // Preenche os precos
                ProductSizeDao psDao = new ProductSizeDao(mConnection);
                ProductSize psQuery = new ProductSize();
                psQuery.setProductId(id);
                p.setSizesAvailable(psDao.get(psQuery));

                products.add(p);
            }
        } catch (SQLException e) {
            Logger.error("Erro ao listar produtos", e);
            throw e;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }
        return products;
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


    private String buildQuery(Product param) {
        final String where = " WHERE";
        final String and = " AND";
        String nextToken = where;
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + TABLE_NAME);
        if (param != null) {
            if (param.getCategoryId() != NO_CATEGORY) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_CATEGORY + " = " + param.getCategoryId());
                nextToken = and;
            }
            if (param.getId() != INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_ID + " = " + param.getId());
                nextToken = and;
            }
            if (!StringUtils.isEmpty(param.getName())) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_NAME + " = " + param.getName());
                nextToken = and;
            }
        }

        return queryBuilder.toString();
    }
}
