package com.delivery.pageloader.admin;

import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.AdminPageLoaderCommand;
import com.delivery.menu.Flavour;
import com.delivery.menu.Optional;
import com.delivery.menu.Product;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.FlavourDao;
import com.delivery.persistent.OptionalDao;
import com.delivery.persistent.ProductDao;
import com.delivery.util.SQLUtils;

public class ProductOverviewEditorLoader extends AdminPageLoaderCommand {

    private String mRedirect;

    @Override
    public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            int productId;
            try {
                productId = Integer.parseInt(req.getParameter("prodId"));
            } catch (NumberFormatException e) {
                productId = (int) SQLUtils.INVALID_ID;
            }

            final int finalProductId = productId;

            DaoManager daoManager = new DaoManager(dataSource);
            Product product = (Product) daoManager.execute(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    ProductDao productDao = manager.getProductDao();
                    Product query = new Product();
                    query.setId(finalProductId);
                    List<Product> products = productDao.get(query);
                    Product product = products.get(0);

                    // Preenche os sabores e opcionais do produto!
                    FlavourDao fDao = manager.getFlavourDao();
                    Flavour fQuery = new Flavour();
                    fQuery.setProductId(product.getId());
                    product.setFlavours(fDao.get(fQuery));

                    OptionalDao oDao = manager.getOptionalDao();
                    Optional oQuery = new Optional();
                    oQuery.setProductId(product.getId());
                    product.setOptionals(oDao.get(oQuery));

                    return product;
                }
            });

            if (product != null) {
                req.setAttribute("product", product);
                mRedirect = "editor/product-overview-editor.jsp";
            } else {
                mRedirect = "editor/product-overview-editor.jsp";
            }
        } catch (NamingException e) {
            Logger.error("NamingException", e);
            // TODO mostra a pagina de cardapio sem nada mesmo?!?!
        }
    }

    @Override
    public String getRedirect() {
        return mRedirect;
    }

}
