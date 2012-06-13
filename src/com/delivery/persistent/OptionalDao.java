package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public int update(List<Optional> objectsToUpdate) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(List<Optional> objectsToDelete) throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public List<Optional> get(Optional param) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getLastSavedId() throws SQLException {
        // TODO Auto-generated method stub
        return 0;
    }

}
