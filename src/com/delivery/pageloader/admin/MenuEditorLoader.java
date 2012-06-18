package com.delivery.pageloader.admin;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.AdminPageLoaderCommand;
import com.delivery.menu.MenuCategory;
import com.delivery.menu.Product;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.MenuCategoryDao;
import com.delivery.persistent.ProductDao;

public class MenuEditorLoader extends AdminPageLoaderCommand {
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

                    // Adiciona os produtos de cada categoria para poder mostrar
                    // os produtos no editor!
                    ProductDao pDao = manager.getProductDao();
                    for (MenuCategory cat : categories) {
                        addProductsToCategory(cat, pDao);
                    }

                    return categories;
                }
            });

            req.setAttribute("MenuCategories", categories);
        } catch (NamingException e) {
            Logger.error("NamingException", e);
            // TODO mostra a pagina de cardapio sem nada mesmo?!?!
        }
    }

    private void addProductsToCategory(MenuCategory category, ProductDao pDao) throws SQLException {
        List<MenuCategory> subCategories = category.getSubCategories();
        if (subCategories != null && subCategories.size() > 0) {
            for (MenuCategory subCategory : subCategories) {
                addProductsToCategory(subCategory, pDao);
            }
        }

        Product productQuery = new Product();
        productQuery.setCategoryId(category.getCategoryId());
        category.setProducts(pDao.get(productQuery));
    }

    @Override
    public String getRedirect() {
        return "menu-editor.jsp";
    }

}
