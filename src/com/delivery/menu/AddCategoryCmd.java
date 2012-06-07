package com.delivery.menu;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.MenuCommand;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.MenuCategoryDao;
import com.delivery.util.StringUtils;

public class AddCategoryCmd extends MenuCommand {
    private String mRedirect;

    @Override
    public void execute(HttpServletRequest request) {
        MenuCategory savedCat = null;
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            final String catName = request.getParameter("catName");
            final String parentId = request.getParameter("parentId");

            DaoManager daoManager = new DaoManager(dataSource);
            savedCat = (MenuCategory) daoManager.execute(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    MenuCategory cat = new MenuCategory();
                    cat.setName(catName);
                    if (!StringUtils.isEmpty(parentId)) {
                        try {
                            cat.setParentId(Integer.parseInt(parentId));
                        } catch (NumberFormatException ignore) { }
                    }
                    MenuCategoryDao dao = manager.getMenuCategoryDao();
                    ArrayList<MenuCategory> toInsert = new ArrayList<MenuCategory>();
                    toInsert.add(cat);

                    int[] saved = dao.save(toInsert);
                    if (saved != null && saved.length > 0) {
                        cat.setCategoryId(dao.getLastSavedId());
                        return cat;
                    }
                    return null;
                }
            });
        } catch (NamingException e) {
            Logger.error("NamingException", e);
        }
        if (savedCat != null) {
            request.setAttribute("categoria", savedCat);
            mRedirect = "cardapio/categoria.jsp";
        } else {
            // TODO retornar pagina de erro ao usuario. Categoria nao foi inserida!!!
        }
    }

    @Override
    public String getRedirect() {
        return mRedirect;
    }

}
