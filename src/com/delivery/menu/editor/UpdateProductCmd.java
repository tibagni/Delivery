package com.delivery.menu.editor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.MenuEditorCommand;
import com.delivery.menu.Product;
import com.delivery.menu.ProductSize;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.ProductDao;
import com.delivery.persistent.ProductSizeDao;
import com.delivery.util.SQLUtils;
import com.delivery.util.StringUtils;

public class UpdateProductCmd extends MenuEditorCommand {

    private static final String SIZE_PATTERN = "produto\\[tamanhos\\]\\[[0-9]*\\]\\[tamanho\\]";
    private String mRedirect;

    @Override
    public void execute(HttpServletRequest request) {
        Product updatedProduct = null;
        int productId = (int) SQLUtils.INVALID_ID;
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            int id;
            try {
                id = Integer.parseInt(request.getParameter("prodId"));
            } catch (NumberFormatException e) {
                Logger.error("Produto sem ID???", e);
                throw new RuntimeException("Id de produto inválido");
            }
            productId = id;
            String prodName    = request.getParameter("nome");
            String prodDesc    = request.getParameter("desc");
            String picturePath = request.getParameter("foto");
            int opPerOrder;
            try {
                opPerOrder = Integer.parseInt(request.getParameter("op"));
            } catch (NumberFormatException e) {
                opPerOrder = 0;
            }
            int flavoursPerOrder;
            try {
                flavoursPerOrder = Integer.parseInt(request.getParameter("sab"));
            } catch (NumberFormatException e) {
                flavoursPerOrder = 1;
            }

            final Product submittedProduct = new Product();
            submittedProduct.setId(id);
            // Nao e necessario o id da categoria, ja que nao podemos mudar
            // um produto de categoria
            submittedProduct.setName(prodName);
            submittedProduct.setDescription(prodDesc);
            submittedProduct.setPicturePath(picturePath);
            submittedProduct.setOptionalsPerOrder(opPerOrder);
            submittedProduct.setFlavoursPerOrder(flavoursPerOrder);

            final Map<String, String[]> requestParameters = request.getParameterMap();

            DaoManager daoManager = new DaoManager(dataSource);
            updatedProduct = (Product) daoManager.transaction(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    ProductDao dao = manager.getProductDao();
                    Product query = new Product();
                    query.setId(submittedProduct.getId());

                    List<Product> result = dao.get(query);
                    if (result == null || result.size() == 0) {
                        return null;
                    }

                    // Vamos atualizar o 'currentProduct' e persistir as alteracoes!
                    Product currentProduct = result.get(0);
                    if (!StringUtils.areEquals(currentProduct.getName(), submittedProduct.getName())) {
                        currentProduct.setName(submittedProduct.getName());
                    }
                    if (!StringUtils.areEquals(currentProduct.getDescription(), submittedProduct.getDescription())) {
                        currentProduct.setDescription(submittedProduct.getDescription());
                    }
                    if (!StringUtils.areEquals(currentProduct.getPicturePath(), submittedProduct.getPicturePath())) {
                        currentProduct.setPicturePath(submittedProduct.getPicturePath());
                    }
                    if (currentProduct.getOptionalsPerOrder() != submittedProduct.getOptionalsPerOrder()) {
                        if (submittedProduct.getOptionalsPerOrder() >= 0) {
                            currentProduct.setOptionalsPerOrder(submittedProduct.getOptionalsPerOrder());
                        }
                    }
                    if (currentProduct.getFlavoursPerOrder() != submittedProduct.getFlavoursPerOrder()) {
                        if (submittedProduct.getFlavoursPerOrder() > 0) {
                            currentProduct.setFlavoursPerOrder(submittedProduct.getFlavoursPerOrder());
                        }
                    }
                    dao.update(currentProduct);

                    // Depois de atualizar o produto, vamos inserir os tamanhos adicionais (se houverem)
                    List<ProductSize> sizes = getSizesFromParameters(requestParameters, currentProduct.getId());
                    if (sizes != null && sizes.size() > 0) {
                        ProductSizeDao pSDao = manager.getProductSizeDao();
                        int[] saved = pSDao.save(sizes);
                        if (saved != null && saved.length > 0) {
                            Logger.debug(saved.length + " tamanhos para " + currentProduct.getName());
                            // Para colocar todos os objetos 'tamanho' no produto, e melhor fazer uma query no db
                            // Deste modo os objetos ja veem com os Ids e todos os outros campos preenchidos!
                            ProductSize psQuery = new ProductSize();
                            psQuery.setProductId(currentProduct.getId());
                            currentProduct.setSizesAvailable(pSDao.get(psQuery));
                        }
                    }

                    return currentProduct;
                }
            });
        } catch (NamingException e) {
            Logger.error("NamingException", e);
        }
        String finalMsg;
        if (updatedProduct != null) {
            Logger.debug("O produto foi atualizado com sucesso!");
            finalMsg = "Produto atualizado com sucesso!";
        } else {
            Logger.debug("O produto NAO foi atualizado!");
            finalMsg = "Produto NÃO foi atualizado!";
        }

        request.setAttribute("prodId", productId);
        request.setAttribute("finalMsg", finalMsg);
        mRedirect = "editor/whats-next.jsp";
    }

    private List<ProductSize> getSizesFromParameters(Map<String, String[]> parameters, int productId) {
        ArrayList<ProductSize> sizes = new ArrayList<ProductSize>();

        Iterator<String> it = parameters.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next();
            if (StringUtils.matches(SIZE_PATTERN, key)) {
                String val = parameters.get(key)[0];
                if (!StringUtils.isEmpty(val)) {
                    Logger.debug("Tamanho de produto encontrado: " + key + " = " + val);
                    ProductSize pS = new ProductSize();
                    pS.setName(val);
                    pS.setProductId(productId);
                    sizes.add(pS);
                }
            }
        }

        return sizes;
    }

    @Override
    public String getRedirect() {
        return mRedirect;
    }

}
