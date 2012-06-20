package com.delivery.pageloader;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.PageLoaderCommand;
import com.delivery.menu.Flavour;
import com.delivery.menu.Optional;
import com.delivery.menu.Price;
import com.delivery.menu.Product;
import com.delivery.menu.ProductSize;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.FlavourDao;
import com.delivery.persistent.OptionalDao;
import com.delivery.persistent.PriceDao;
import com.delivery.persistent.ProductDao;
import com.delivery.persistent.ProductSizeDao;
import com.delivery.util.SQLUtils;

public class ProductViewLoader extends PageLoaderCommand {


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

            int sizeId;
            try {
                sizeId = Integer.parseInt(req.getParameter("sizeId"));
            } catch (NumberFormatException e) {
                sizeId = (int) SQLUtils.INVALID_ID;
            } catch (NullPointerException npe) {
                sizeId = (int) SQLUtils.INVALID_ID;
            }

            final int finalSizeId = sizeId;
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

                    OptionalDao oDao = manager.getOptionalDao();
                    Optional oQuery = new Optional();
                    oQuery.setProductId(product.getId());
                    product.setOptionals(oDao.get(oQuery));

                    ProductSizeDao psDao = manager.getProductSizeDao();
                    ProductSize psQuery = new ProductSize();
                    psQuery.setProductId(product.getId());
                    product.setSizesAvailable(psDao.get(psQuery));

                    FlavourDao fDao = manager.getFlavourDao();
                    Flavour fQuery = new Flavour();
                    fQuery.setProductId(product.getId());

                    List<Flavour> allFlavours = fDao.get(fQuery);
                    ArrayList<Flavour> flavours = new ArrayList<Flavour>();
                    int sizeId = finalSizeId;
                    if (sizeId == (int) SQLUtils.INVALID_ID) {
                        sizeId = product.getSizesAvailable().get(0).getId();
                    }
                    Price pQ = new Price();
                    PriceDao prDao = manager.getPriceDao();
                    pQ.setSizeId(sizeId);
                    for (Flavour f : allFlavours) {
                        // Para cada sabor verifica a existencia de um preco para o tamanho
                        // Se existir quer dizer que o sabor esta disponivel para o tamanho especificado
                        pQ.setFlavourId(f.getId());
                        List<Price> result = prDao.get(pQ);
                        if (result != null && result.size() > 0) {
                            flavours.add(f);
                        }
                    }

                    product.setFlavours(flavours);
                    product.setCurrentSizeId(sizeId);

                    return product;
                }
            });

            if (product != null) {
                req.setAttribute("product", product);
                mRedirect = "menu/product-view.jsp";
            } else {
                mRedirect = "seila";
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
