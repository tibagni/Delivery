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
import com.delivery.menu.Product;
import com.delivery.menu.ProductSize;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.ProductDao;
import com.delivery.persistent.ProductSizeDao;
import com.delivery.util.StringUtils;

public class ProductEditorLoader extends AdminPageLoaderCommand{

    private String mRedirect;

    @Override
    public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
        mRedirect = "editor/product-editor.jsp";
        String editingId = req.getParameter("editing");
        if (StringUtils.isEmpty(editingId)) {
            // Mostra apenas a pagina de cadastro de produto!
            return;
        }

        int productId;
        try {
            productId = Integer.parseInt(editingId);
        } catch (NumberFormatException nfe) {
            // Mostra apenas a pagina de cadastro de produto!
            return;
        }

        final int finalProdId = productId;

        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            DaoManager daoManager = new DaoManager(dataSource);
            Product product = (Product) daoManager.execute(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    ProductDao dao = manager.getProductDao();
                    Product query = new Product();
                    query.setId(finalProdId);
                    List<Product> result = dao.get(query);
                    if (result != null && result.size() > 0) {
                        Product p = result.get(0);

                        // Preenche os tamanhos do produto!
                        // Sera usado na pagina exibir os tamanhos ja existentes do produto
                        ProductSizeDao psDao = manager.getProductSizeDao();
                        ProductSize psQuery = new ProductSize();
                        psQuery.setProductId(p.getId());
                        p.setSizesAvailable(psDao.get(psQuery));

                        return p;
                    }
                    return null;
                }
            });

            if (product != null) {
                int productSizes = 0;
                if (product.getSizesAvailable() != null) {
                    productSizes = product.getSizesAvailable().size();
                    if (productSizes < 0) productSizes = 0;
                }
                int sizesLeft = Product.MAX_SIZES - productSizes;
                req.setAttribute("sizesLeft", sizesLeft);
                req.setAttribute("product", product);
                mRedirect = "editor/product-editor-existing.jsp";
            }
        } catch (NamingException e) {
            Logger.error("NamingException", e);
        }

    }

    @Override
    public String getRedirect() {
        return mRedirect;
    }

}
