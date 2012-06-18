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
import com.delivery.menu.Price;
import com.delivery.menu.Product;
import com.delivery.menu.ProductSize;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.FlavourDao;
import com.delivery.persistent.PriceDao;
import com.delivery.persistent.ProductDao;
import com.delivery.persistent.ProductSizeDao;
import com.delivery.util.SQLUtils;
import com.delivery.util.StringUtils;

public class FlavourEditorLoader extends AdminPageLoaderCommand {

    private String mRedirect;

    @Override
    public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
        boolean isEditing = false;
        String editingId = req.getParameter("editing");
        if (!StringUtils.isEmpty(editingId)) {
            isEditing = true;
        }

        int flavourId = (int) SQLUtils.INVALID_ID;
        if (isEditing) {
            try {
                flavourId = Integer.parseInt(editingId);
            } catch (NumberFormatException nfe) {
                isEditing = false;
            }
        }

        final int finalFlavourId = flavourId;

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
                    Product product = products.get(0);

                    // Preenche os tamanhos do produto!
                    // Sera usado na pagina de sabor para definir um preco para cada tamanho
                    ProductSizeDao psDao = manager.getProductSizeDao();
                    ProductSize psQuery = new ProductSize();
                    psQuery.setProductId(product.getId());
                    product.setSizesAvailable(psDao.get(psQuery));

                    return product;
                }
            });

            Flavour flavour = null;
            if (isEditing) {
                flavour = (Flavour) daoManager.execute(new DaoManager.DaoCommand() {
                    @Override public Object execute(DaoManager manager)
                            throws SQLException {
                        FlavourDao dao = manager.getFlavourDao();
                        Flavour query = new Flavour();
                        query.setId(finalFlavourId);

                        List<Flavour> result = dao.get(query);
                        if (result != null && result.size() > 0) {
                            Flavour f = result.get(0);

                            // Se estamos editando o sabor, devemos dar a chance do usuario de editar os precos tambem
                            // Para isso vamos pegar os precos.
                            PriceDao pDao = manager.getPriceDao();
                            Price priceQuery = new Price();
                            priceQuery.setFlavourId(f.getId());
                            List<Price> prices = pDao.get(priceQuery);
                            f.setPrices(prices);

                            ProductSizeDao pSDao = manager.getProductSizeDao();
                            ProductSize sizeQuery = new ProductSize();
                            sizeQuery.setProductId(f.getProductId());
                            List<ProductSize> savedSizes = pSDao.get(sizeQuery);

                            // Vamos popular o cachedSizeName aqui para ficar mais simples
                            // de mostrar a qual tamanho o preco esta relacionado na view
                            for (Price p : prices) {
                                for (ProductSize pS : savedSizes) {
                                    if (p.getSizeId() == pS.getId()) {
                                        p.setCachedSizeName(pS.getName());
                                        break;
                                    }
                                }
                            }

                            return f;
                        }
                        return null;
                    }
                });
            }

            if (product != null) {
                req.setAttribute("produto", product);
                if (flavour == null) {
                    mRedirect = "editor/prod-flavour-editor.jsp";
                } else {
                    req.setAttribute("flavour", flavour);
                    mRedirect = "editor/prod-flavour-editor-existing.jsp";
                }
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
