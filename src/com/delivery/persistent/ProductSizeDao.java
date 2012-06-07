package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.Logger;
import com.delivery.menu.ProductSize;
import com.delivery.util.StringUtils;

public class ProductSizeDao extends Dao<ProductSize> {
    private static final String TABLE_NAME = "tamanhos";

    private static final String COLUMN_COD     = "cod_tamanho";
    private static final String COLUMN_NAME    = "nome";
    private static final String COLUMN_PRODUCT = "produto_cod_produto";

    private static final int INVALID_ID = 0;
    private static final int NO_PRODUCT = INVALID_ID;

    private static final int IND_COLUMN_COD = 1;
    private static final int IND_COLUMN_NAME = 2;
    private static final int IND_COLUMN_PRODUCT = 3;

    public ProductSizeDao(Connection connection) {
        super(connection);
    }

    @Override
    public int[] save(List<ProductSize> objectsToInsert) throws SQLException {
        if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
                    COLUMN_NAME + ", " +
                    COLUMN_PRODUCT + ") " +
                    "VALUES (?,?)");
            for (ProductSize prodSize : objectsToInsert) {
                if (StringUtils.isEmpty(prodSize.getName())) {
                    throw new IllegalArgumentException("Tentando adicionar tamanho de produto sem nome!");
                }
                if (prodSize.getProductId() == NO_PRODUCT) {
                    throw new IllegalArgumentException("Tamanho de produto não está relacionado com nenhum produto?");
                }
                stm.setString(1, prodSize.getName());
                stm.setInt(2, prodSize.getProductId());
                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao adicionar tamanho(s) de produto!", e);
            throw e;
        }
        return inserted;
    }

    @Override
    public int update(List<ProductSize> objectsToUpdate) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(List<ProductSize> objectsToDelete) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<ProductSize> get(ProductSize param) {
        String query = buildQuery(param);

        ArrayList<ProductSize> sizes = null;
        Statement stm = null;
        try {
            stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            sizes = new ArrayList<ProductSize>();
            while(rs.next()) {
                ProductSize pS = new ProductSize();
                int id = rs.getInt(IND_COLUMN_COD);
                pS.setId(id);
                pS.setName(rs.getString(IND_COLUMN_NAME));
                pS.setProductId(rs.getInt(IND_COLUMN_PRODUCT));

                sizes.add(pS);
            }
        } catch (SQLException e) {
            // Ignore TODO log
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }

        return sizes;
    }

    @Override
    public int getLastSavedId() {
        // TODO Auto-generated method stub
        return 0;
    }


    private String buildQuery(ProductSize param) {
        final String where = " WHERE";
        final String and = " AND";
        String nextToken = where;
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + TABLE_NAME);
        if (param.getId() != INVALID_ID) {
            queryBuilder.append(nextToken);
            queryBuilder.append(" " + COLUMN_COD + " = " + param.getId());
            nextToken = and;
        }
        if (param.getProductId() != NO_PRODUCT) {
            queryBuilder.append(nextToken);
            queryBuilder.append(" " + COLUMN_PRODUCT + " = " + param.getProductId());
            nextToken = and;
        }
        if (!StringUtils.isEmpty(param.getName())) {
            queryBuilder.append(nextToken);
            queryBuilder.append(" " + COLUMN_NAME + " = " + param.getName());
            nextToken = and;
        }

        return queryBuilder.toString();
    }

}
