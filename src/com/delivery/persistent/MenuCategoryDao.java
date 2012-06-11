package com.delivery.persistent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.delivery.Logger;
import com.delivery.menu.MenuCategory;
import com.delivery.menu.Product;
import com.delivery.util.SQLUtils;
import com.delivery.util.StringUtils;

public class MenuCategoryDao extends Dao<MenuCategory> {
    private static final String TABLE_NAME = "categoria";

    private static final String COLUMN_ID = "cod_categoria";
    private static final String COLUMN_NAME = "nome";
    private static final String COLUMN_PARENT = "categoria_cod_categoria";

    private static final int IND_COLUMN_ID = 1;
    private static final int IND_COLUMN_PARENT = 2;
    private static final int IND_COLUMN_NAME = 3;

    private static final int NO_PARENT = (int) SQLUtils.INVALID_ID;

    public MenuCategoryDao(Connection connection) {
        super(connection);
    }

    @Override
    public int[] save(List<MenuCategory> objectsToInsert) throws SQLException {
        if (objectsToInsert == null || objectsToInsert.size() == 0) {
            return null;
        }
        int inserted[] = null;
        try {
            PreparedStatement stm = mConnection.prepareStatement("INSERT INTO " + TABLE_NAME + " (" +
                    COLUMN_NAME + ", " +
                    COLUMN_PARENT + ") " +
                    "VALUES (?,?)");
            for (MenuCategory category : objectsToInsert) {
                if (StringUtils.isEmpty(category.getName())) {
                    throw new IllegalArgumentException("Tentando adicionar categoria sem nome!");
                }
                stm.setString(1, category.getName());
                int parentId = category.getParentId();
                if (parentId == NO_PARENT) {
                    stm.setNull(2, java.sql.Types.INTEGER);
                } else {
                    stm.setInt(2, category.getParentId());
                }
                stm.addBatch();
            }
            inserted = stm.executeBatch();
        } catch (SQLException e) {
            Logger.error("Erro ao inserir categoria", e);
            throw e;
        }
        return inserted;
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
            Logger.error("Erro ao tentar recuperar ultimo Id de categoria", e);
            throw e;
        }
        return id;
    }

    @Override
    public int update(List<MenuCategory> objectsToUpdate) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int delete(List<MenuCategory> objectsToDelete) {
        Logger.info("Nao e possivel remover uma categoria");
        return 0;
    }

    private List<MenuCategory> get(MenuCategory param, int parentId) throws SQLException {
        final String query;
        if (parentId == NO_PARENT) {
            query = buildQuery(param, COLUMN_PARENT + " is null");
        } else {
            // Montar a query levando em conta o parentId
            query = buildQuery(param, COLUMN_PARENT + "=" + parentId);
        }

        ArrayList<MenuCategory> result = null;
        Statement stm = null;
        try {
            stm = mConnection.createStatement();
            ResultSet rs = stm.executeQuery(query);
            result = new ArrayList<MenuCategory>();
            while(rs.next()) {
                MenuCategory mc = new MenuCategory();
                int id = rs.getInt(IND_COLUMN_ID);
                mc.setCategoryId(id);
                mc.setName(rs.getString(IND_COLUMN_NAME));
                mc.setParentId(rs.getInt(IND_COLUMN_PARENT));
                // Chama get recursivamente para popular as
                // sub categorias
                mc.setSubCategories(get(null, id));

                // Query para os produtos desta categoria

                // TODO Talvez uma query mais leve aqui fosse melhor
                // Somente com o nome e o id do produto, e carrega-lo sob demanda mais tarde
                ProductDao pDao = new ProductDao(mConnection);
                Product productQuery = new Product();
                productQuery.setCategoryId(id);
                mc.setProducts(pDao.get(productQuery));


                result.add(mc);
            }
        } catch (SQLException e) {
            Logger.error("Erro ao listar categorias!", e);
            throw e;
        } finally {
            if (stm != null) {
                try {
                    stm.close();
                } catch (SQLException ignore) { }
            }
        }
        return result;

    }

    @Override
    public List<MenuCategory> get(MenuCategory param) throws SQLException {
        return get(param, NO_PARENT);
    }

    private String buildQuery(MenuCategory param, String... extraWhereArgs) {
        final String where = " WHERE";
        final String and = " AND";
        String nextToken = where;
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append("SELECT * FROM " + TABLE_NAME);
        if (param != null) {
            int id = param.getCategoryId();
            String catName = param.getName();
            if (id != MenuCategory.INVALID_ID) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_ID + "=" + id);
                nextToken = and;
            }
            if (!StringUtils.isEmpty(catName)) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + COLUMN_NAME + " LIKE " + catName);
                nextToken = and;
            }
        }
        // Adiciona argumentos extras
        if (extraWhereArgs != null) {
            for (String arg : extraWhereArgs) {
                queryBuilder.append(nextToken);
                queryBuilder.append(" " + arg);
                nextToken = and;
            }
        }
        return queryBuilder.toString();
    }
}
