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
import com.delivery.util.StringUtils;

public class AddProductCmd extends MenuEditorCommand {

    private static final String SIZE_PATTERN = "produto\\[tamanhos\\]\\[[0-9]*\\]\\[tamanho\\]";
    private String mRedirect;

    @Override
    public void execute(HttpServletRequest request) {
        Product savedProd = null;
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            int categoryId;
            try {
                categoryId = Integer.parseInt(request.getParameter("catId"));
            } catch (NumberFormatException e) {
                Logger.error("Produto sem categria???", e);
                throw new RuntimeException("Id da categoria de produto inválida");
            }
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

            final Product finalProduct = new Product();
            finalProduct.setCategoryId(categoryId);
            finalProduct.setName(prodName);
            finalProduct.setDescription(prodDesc);
            finalProduct.setPicturePath(picturePath);
            finalProduct.setOptionalsPerOrder(opPerOrder);
            finalProduct.setFlavoursPerOrder(flavoursPerOrder);

            final Map<String, String[]> requestParameters = request.getParameterMap();

            DaoManager daoManager = new DaoManager(dataSource);
            savedProd = (Product) daoManager.transaction(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    ArrayList<Product> toInsert = new ArrayList<Product>();
                    ProductDao dao = manager.getProductDao();
                    Product product = finalProduct;

                    toInsert.add(product);
                    int[] saved = dao.save(toInsert);
                    if (saved != null && saved.length > 0) {
                        product.setId((int) dao.getLastSavedId());

                        // Depois de inserir o produto, vamos inserir os tamanhos deste produto
                        List<ProductSize> sizes = getSizesFromParameters(requestParameters, product.getId());
                        if (sizes != null && sizes.size() > 0) {
                            ProductSizeDao pSDao = manager.getProductSizeDao();
                            saved = pSDao.save(sizes);
                            if (saved != null && saved.length > 0) {
                                Logger.debug(saved.length + " tamanhos para " + product.getName());
                                // Para colocar todos os objetos 'tamanho' no produto, e melhor fazer uma query no db
                                // Deste modo os objetos ja veem com os Ids e todos os outros campos preenchidos!
                                ProductSize query = new ProductSize();
                                query.setProductId(product.getId());
                                product.setSizesAvailable(pSDao.get(query));
                            } else {
                                // Nenhum tamanho foi inserido, cancela transacao!
                                manager.cancelTransaction();
                            }
                        } else {
                            // Nenhum tamanho a ser inserido, cancela transacao!
                            manager.cancelTransaction();
                        }
                        return product;
                    }
                    return null;
                }
            });
        } catch (NamingException e) {
            Logger.error("NamingException", e);
        }
        if (savedProd != null) {
            Logger.debug("O produto foi salvo com sucesso!");
            request.setAttribute("produto", savedProd);
            // Mensagem para o usuario
            request.setAttribute("finalMsg", "Produto salvo com sucesso! Adicione um sabor a ele!");

            // E necessario pelo menos um sabor associado ao produto, entao vamos aproveitar e mandar o usuario
            // para a pagina de cadastro de sabor!
            mRedirect = "editor/prod-flavour-editor.jsp";
        } else {
            // TODO retornar pagina de erro ao usuario. Produto nao foi inserido!!!
        }
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
