package com.delivery.persistent;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.delivery.Logger;

/**
 * NAO E SEGURO PARA MULTITHREAD
 * @author Tiago
 *
 */
public class DaoManager {
    private MenuCategoryDao mMenuCategoryDao;
    private ProductDao mProductDao;
    private ProductSizeDao mProductSizeDao;
    private FlavourDao mFlavourDao;
    private PriceDao mPriceDao;

    private DataSource mDataSource;
    private Connection mConnection;

    public DaoManager(DataSource dataSource) {
        mDataSource = dataSource;
    }

    public MenuCategoryDao getMenuCategoryDao() throws SQLException {
        if (mMenuCategoryDao == null) {
            mMenuCategoryDao = new MenuCategoryDao(getConnection());
        }
        return mMenuCategoryDao;
    }

    public ProductDao getProductDao() throws SQLException {
        if (mProductDao == null) {
            mProductDao = new ProductDao(getConnection());
        }
        return mProductDao;
    }

    public ProductSizeDao getProductSizeDao() throws SQLException {
        if (mProductSizeDao == null) {
            mProductSizeDao = new ProductSizeDao(getConnection());
        }
        return mProductSizeDao;
    }

    public FlavourDao getFlavourDao() throws SQLException {
        if (mFlavourDao == null) {
            mFlavourDao = new FlavourDao(getConnection());
        }
        return mFlavourDao;
    }

    public PriceDao getPriceDao() throws SQLException {
        if (mPriceDao == null) {
            mPriceDao = new PriceDao(getConnection());
        }
        return mPriceDao;
    }

    private Connection getConnection() throws SQLException {
        if (mConnection == null) {
            mConnection = mDataSource.getConnection();
        }
        return mConnection;
    }

    private void cleanUp() {
        // libera totalmente a conexao
        mConnection = null;

        // libera os DAOs
        mMenuCategoryDao = null;
        mProductDao = null;
        mProductSizeDao = null;
        mFlavourDao = null;
        mPriceDao = null;
    }

    public Object execute(DaoCommand command) {
        try {
            return command.execute(this);
        } catch (SQLException e) {
            Logger.error("Erro ao executar DaoCommand", e);
            return null;
        } finally {
            try {
                getConnection().close();
            } catch (SQLException ignore) {}
            cleanUp();
        }
    }

    public Object transaction(DaoCommand command) {
        Object returnObj = null;
        try {
            getConnection().setAutoCommit(false);
            return command.execute(this);
        } catch (SQLException e) {
            Logger.error("Erro ao executar transacao - DaoCommand");
            Logger.debug("Rollback!!!");
            try {
                Logger.error(e.getMessage(), e);
                getConnection().rollback();
            } catch (SQLException ignore) {}
        } finally {
            try {
                getConnection().setAutoCommit(true);
            } catch (SQLException ignore) {}
        }
        return returnObj;
    }

    public final void cancelTransaction() throws SQLException {
        throw new SQLException("Rollback!");
    }

    public interface DaoCommand {
        public Object execute(DaoManager manager) throws SQLException;
    }
}
