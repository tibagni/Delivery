package com.delivery.pageloader;

import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.PageLoaderCommand;
import com.delivery.menu.Product;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.ProductDao;
import com.delivery.util.SQLUtils;

public class NewFlavourLoader extends PageLoaderCommand {

    private String mRedirect;

    @Override
    public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
        // Limpa o atributo finalMsg (se houver)
        if (req.getParameterMap().containsKey("finalMsg")) {
            req.getParameterMap().remove("finalMsg");
        }
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            int productId = 0;
            try {
                productId = Integer.parseInt(req.getParameter("prodId"));
            } catch (NumberFormatException e) {
                productId = (int) SQLUtils.INVALID_ID;
            }

            final int finalProductId = productId;
            DaoManager daoManager = new DaoManager(dataSource);

            Product product = (Product) daoManager.execute(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    ProductDao dao = manager.getProductDao();
                    Product query = new Product();
                    query.setId(finalProductId);
                    List<Product> products = dao.get(query);
                    if (products == null || products.size() != 1) {
                        return null;
                    }

                    return products.get(0);
                }
            });

            if (product != null) {
                req.setAttribute("produto", product);
                mRedirect = "cardapio/novoProd-sabor.jsp";
            } else {
                // TODO
            }

        } catch (NamingException e) {
            Logger.error("NamingException", e);
            // TODO
        }

    }

    @Override
    public String getRedirect() {
        return mRedirect;
    }

}
