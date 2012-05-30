package com.delivery.persistent;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * NAO E SEGURO PARA MULTITHREAD
 * @author Tiago
 *
 */
public class DaoManager {
    private MenuCategoryDao mMenuCategoryDao;
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
    }

    public Object execute(DaoCommand command) {
        try {
            return command.execute(this);
        } catch (SQLException e) {
            // TODO logs
            return null;
        } finally {
            try {
                getConnection().close();
            } catch (SQLException ignore) { /*TODO logs*/ }
            cleanUp();
        }
    }

    public Object transaction(DaoCommand command) {
        Object returnObj = null;
        try {
            getConnection().setAutoCommit(false);
            return command.execute(this);
        } catch (SQLException e) {
            try {
                getConnection().rollback();
            } catch (SQLException ignore) { /*TODO logs*/ }
        } finally {
            try {
                getConnection().setAutoCommit(true);
            } catch (SQLException ignore) { /*TODO logs*/ }
        }
        return returnObj;
    }

    public interface DaoCommand {
        public Object execute(DaoManager manager) throws SQLException;
    }
}
