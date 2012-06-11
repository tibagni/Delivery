package com.delivery.pageloader;

import java.sql.SQLException;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.PageLoaderCommand;
import com.delivery.menu.MenuCategory;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.MenuCategoryDao;

public class MenuLoader extends PageLoaderCommand {
    @Override
    public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            DaoManager daoManager = new DaoManager(dataSource);
            @SuppressWarnings("unchecked")
            Collection<MenuCategory> categories = (Collection<MenuCategory>) daoManager.execute(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    MenuCategoryDao menuCategoryDao = manager.getMenuCategoryDao();
                    Collection<MenuCategory> categories = menuCategoryDao.get(null);
                    return categories;
                }
            });

            req.setAttribute("MenuCategories", categories);
        } catch (NamingException e) {
            Logger.error("NamingException", e);
            // TODO mostra a pagina de cardapio sem nada mesmo?!?!
        }
    }

    @Override
    public String getRedirect() {
        return "cardapio.jsp";
    }

}
