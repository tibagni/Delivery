package com.delivery.menu.editor;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.MenuEditorCommand;
import com.delivery.menu.MenuCategory;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.MenuCategoryDao;
import com.delivery.util.StringUtils;

public class UpdateCategoryCmd extends MenuEditorCommand {

    @Override
    public void execute(HttpServletRequest request) {
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            final String catName = request.getParameter("catName");
            final String catId = request.getParameter("catId");

            DaoManager daoManager = new DaoManager(dataSource);
            daoManager.execute(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    MenuCategory toUpdate = new MenuCategory();
                    toUpdate.setName(catName);
                    if (!StringUtils.isEmpty(catId)) {
                        try {
                            toUpdate.setCategoryId(Integer.parseInt(catId));
                        } catch (NumberFormatException e) {
                            Logger.error("Nao foi possivel atualizar categoria: " + catName, e);
                            return null;
                        }
                    }
                    MenuCategoryDao dao = manager.getMenuCategoryDao();
                    dao.update(toUpdate);
                    return null;
                }
            });
        } catch (NamingException e) {
            Logger.error("NamingException", e);
        }
    }

    @Override
    public String getRedirect() {
        // Sem view para mostrar!
        return null;
    }

}
