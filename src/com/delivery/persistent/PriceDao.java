package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.Logger;
import com.delivery.menu.Price;

public class PriceDao extends Dao<Price> {
    private static final String TABLE_NAME = "preco";

    private static final String COLUMN_FLAVOUR_ID = "sabor_cod_sabor";
    private static final String COLUMN_SIZE_ID    = "tamanhos_cod_tamanho";
    private static final String COLUMN_PRICE      = "preco";

    private static final int INVALID_ID = 0;
    private static final int NO_FLAVOUR = INVALID_ID;
    private static final int NO_SIZE    = INVALID_ID;

    private static final int IND_COLUMN_FLAVOUR_ID = 1;
    private static final int IND_COLUMN_SIZE_ID    = 2;
    private static final int IND_COLUMN_PRICE      = 3;

    public PriceDao(Connection connection) {
        super(connection);
    }

    @Override
    public int[] save(List<Price> objectsToInsert) throws SQLException {
        if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
                    COLUMN_FLAVOUR_ID + ", " +
                    COLUMN_SIZE_ID + ", " +
                    COLUMN_PRICE + ") " +
                    "VALUES (?,?,?)");
            for (Price price : objectsToInsert) {
                if (price.getSizeId() == NO_SIZE) {
                    throw new IllegalArgumentException("Preco nao esta relacionado com tamanho!");
                }
                if (price.getFlavourId() == NO_FLAVOUR) {
                    throw new IllegalArgumentException("Preco nao esta relacionado com sabor!!");
                }
                if (price.getPrice() == 0) {
                    Logger.warning("Inserindo preco ZERO para tamanho - " + price.getSizeId() + " e sabor - " + price.getFlavourId());
                }
                stm.setInt(1, price.getFlavourId());
                stm.setInt(2, price.getSizeId());
                stm.setDouble(3, price.getPrice());
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
    public int update(List<Price> objectsToUpdate) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(List<Price> objectsToDelete) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Price> get(Price param) throws SQLException {
        String query = buildQuery(param);

        ArrayList<Price> prices = null;
        Statement stm = null;
        try {
            stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            prices = new ArrayList<Price>();
            while(rs.next()) {
                Price p = new Price();
                p.setFlavourId(rs.getInt(IND_COLUMN_FLAVOUR_ID));
                p.setSizeId(rs.getInt(IND_COLUMN_SIZE_ID));
                p.setPrice(rs.getDouble(IND_COLUMN_PRICE));

                prices.add(p);
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

        return prices;
    }

    @Override
    public int getLastSavedId() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    private String buildQuery(Price param) {
        final String where = " WHERE";
        final String and = " AND";
        String nextToken = where;
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + TABLE_NAME);
        if (param.getFlavourId() != NO_FLAVOUR) {
            queryBuilder.append(nextToken);
            queryBuilder.append(" " + COLUMN_FLAVOUR_ID + " = " + param.getFlavourId());
            nextToken = and;
        }
        if (param.getSizeId() != NO_SIZE) {
            queryBuilder.append(nextToken);
            queryBuilder.append(" " + COLUMN_SIZE_ID + " = " + param.getSizeId());
            nextToken = and;
        }
        if (param.getPrice() > 0) {
            queryBuilder.append(nextToken);
            queryBuilder.append(" " + COLUMN_PRICE + " = " + param.getPrice());
            nextToken = and;
        }

        return queryBuilder.toString();
    }

}