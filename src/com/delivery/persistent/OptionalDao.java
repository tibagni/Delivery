package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.Logger;
import com.delivery.menu.Optional;
import com.delivery.util.SQLUtils;
import com.delivery.util.StringUtils;

public class OptionalDao extends Dao<Optional> {
    private static final String TABLE_NAME = "opcional";

    private static final String COLUMN_ID         = "cod_opcional";
    private static final String COLUMN_PRODUCT_ID = "produto_cod_produto";
    private static final String COLUMN_PRICE      = "valor_acrescimo";
    private static final String COLUMN_NAME       = "nome";

    private static final int INVALID_ID = (int) SQLUtils.INVALID_ID;
    private static final int NO_PRODUCT = INVALID_ID;

    private static final int IND_COLUMN_ID         = 1;
    private static final int IND_COLUMN_PRODUCT_ID = 2;
    private static final int IND_COLUMN_PRICE      = 3;
    private static final int IND_COLUMN_NAME       = 4;

    public OptionalDao(Connection connection) {
        super(connection);
    }

    @Override
    public int[] save(List<Optional> objectsToInsert) throws SQLException {
        if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
                    COLUMN_PRODUCT_ID + ", " +
                    COLUMN_PRICE + ", " +
                    COLUMN_NAME + ") " +
                    "VALUES (?,?,?)");
            for (Optional optional : objectsToInsert) {
                if (optional.getProductId() == NO_PRODUCT) {
                    throw new IllegalArgumentException("Opcional nao esta relacionado a nenhum produto!");
                }
                if (StringUtils.isEmpty(optional.getName())) {
                    throw new IllegalArgumentException("Opcional nao tem nome!");
                }
                if (optional.getPrice() == 0) {
                    Logger.warning("Inserindo preco ZERO para opcional - " + optional.getName());
                }
                stm.setInt(1, optional.getProductId());
                stm.setDouble(2, optional.getPrice());
                stm.setString(3, optional.getName());
                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao adicionar opcional!", e);
            throw e;
        }
        return inserted;
    }

    @Override
    public int update(Optional objectToUpdate) throws SQLException {
        if (objectToUpdate == null) {
            return 0;
        }

        int updated = 0;
        Statement stm = null;
        try {
            if (objectToUpdate.getId() == INVALID_ID ||
                objectToUpdate.getPrice() <= 0) {
                // Nada para atualizar
                return 0;
            }

            String sql = "UPDATE " + TABLE_NAME + " SET " +
                    COLUMN_PRICE + "=" + objectToUpdate.getPrice() + ", " +
                    COLUMN_NAME + "='" + objectToUpdate.getName() + "' WHERE " +
                    COLUMN_ID + "=" + objectToUpdate.getId();
            stm = mConnection.createStatement();
            updated = stm.executeUpdate(sql);
        } catch (SQLException e) {
            Logger.error("Erro ao editar opcional", e);
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
    public int delete(List<Optional> objectsToDelete) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Optional> get(Optional param) throws SQLException {
        String query = buildQuery(param);

        ArrayList<Optional> optionals = null;
        Statement stm = null;
        try {
            stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            optionals = new ArrayList<Optional>();
            while(rs.next()) {
                Optional o = new Optional();
                o.setId(rs.getInt(IND_COLUMN_ID));
                o.setProductId(rs.getInt(IND_COLUMN_PRODUCT_ID));
                o.setPrice(rs.getDouble(IND_COLUMN_PRICE));
                o.setName(rs.getString(IND_COLUMN_NAME));

                optionals.add(o);
            }
        } catch (SQLException e) {
            Logger.error("Erro ao listar opcionais", e);
            throw e;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }
        return optionals;
    }

    @Override
    public int getLastSavedId() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }


    private String buildQuery(Optional param) {
        final String where = " WHERE";
        final String and = " AND";
        String nextToken = where;
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + TABLE_NAME);
        if (param != null) {
            if (param.getProductId() != NO_PRODUCT) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_PRODUCT_ID + " = " + param.getProductId());
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
            if (param.getPrice() > 0) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_PRICE + " = " + param.getPrice());
                nextToken = and;
            }
        }

        return queryBuilder.toString();
    }
}
