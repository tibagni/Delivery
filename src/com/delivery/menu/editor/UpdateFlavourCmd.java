package com.delivery.menu.editor;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.MenuEditorCommand;
import com.delivery.menu.Flavour;
import com.delivery.menu.Price;
import com.delivery.menu.ProductSize;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.FlavourDao;
import com.delivery.persistent.PriceDao;
import com.delivery.persistent.ProductSizeDao;
import com.delivery.util.StringUtils;

public class UpdateFlavourCmd extends MenuEditorCommand {
    private String mRedirect;

    @Override
    public void execute(HttpServletRequest request) {
        Flavour updatedFlavour = null;
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            int flavourId;
            try {
                flavourId = Integer.parseInt(request.getParameter("flavId"));
            } catch (NumberFormatException e) {
                Logger.error("Sabor sem ID???", e);
                throw new RuntimeException("Id de sabor inválido");
            }

            String flavName = request.getParameter("nome");
            String flavDesc = request.getParameter("desc");
            String picturePath = request.getParameter("foto");

            final Flavour submittedFlavour = new Flavour();
            submittedFlavour.setId(flavourId);
            submittedFlavour.setName(flavName);
            submittedFlavour.setDescription(flavDesc);
            submittedFlavour.setPicturePath(picturePath);


            final Map<String, String[]> requestParameters = request.getParameterMap();

            DaoManager daoManager = new DaoManager(dataSource);
            updatedFlavour = (Flavour) daoManager.transaction(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    FlavourDao dao = manager.getFlavourDao();
                    Flavour query = new Flavour();
                    query.setId(submittedFlavour.getId());

                    List<Flavour> result = dao.get(query);
                    if (result == null || result.size() == 0) {
                        return null;
                    }

                    // Vamos atualizar o 'currentFlavour' e persistir as alteracoes!
                    Flavour currentFlavour = result.get(0);
                    if (!StringUtils.areEquals(currentFlavour.getName(), submittedFlavour.getName())) {
                        currentFlavour.setName(submittedFlavour.getName());
                    }
                    if (!StringUtils.areEquals(currentFlavour.getDescription(), submittedFlavour.getDescription())) {
                        currentFlavour.setDescription(submittedFlavour.getDescription());
                    }
                    if (!StringUtils.areEquals(currentFlavour.getPicturePath(), submittedFlavour.getPicturePath())) {
                        currentFlavour.setPicturePath(submittedFlavour.getPicturePath());
                    }

                    dao.update(currentFlavour);

                    // Depois de atualizar o produto, vamos inserir os precos adicionais (se houverem)
                    List<Price> prices = AddFlavourCmd.getPricesFromParameters(requestParameters, currentFlavour.getId());
                    if (prices != null && prices.size() > 0) {
                        PriceDao pDao = manager.getPriceDao();
                        int[] saved = pDao.save(prices);
                        ProductSizeDao pSDao = manager.getProductSizeDao();
                        if (saved != null && saved.length > 0) {
                            Logger.debug(saved.length + " precos para " + currentFlavour.getName());
                            // Para colocar todos os objetos 'preco' no sabor, e melhor fazer uma query no db
                            // Deste modo os objetos ja veem com os Ids e todos os outros campos preenchidos!
                            Price priceQuery = new Price();
                            priceQuery.setFlavourId(currentFlavour.getId());
                            List<Price> savedPrices = pDao.get(priceQuery);
                            currentFlavour.setPrices(savedPrices);

                            ProductSize sizeQuery = new ProductSize();
                            sizeQuery.setProductId(currentFlavour.getProductId());
                            List<ProductSize> savedSizes = pSDao.get(sizeQuery);

                            for (Price p : savedPrices) {
                                for (ProductSize pS : savedSizes) {
                                    if (p.getSizeId() == pS.getId()) {
                                        p.setCachedSizeName(pS.getName());
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    return currentFlavour;
                }
            });
        } catch (NamingException e) {
            Logger.error("NamingException", e);
        }
        String finalMsg;
        if (updatedFlavour != null) {
            Logger.debug("O sabor foi atualizado com sucesso!");
            finalMsg = "Sabor atualizado com sucesso!";
        } else {
            Logger.debug("O sabor NAO foi atualizado!");
            finalMsg = "Sabor NÃO foi atualizado!";
        }

        request.setAttribute("prodId", updatedFlavour.getProductId());
        request.setAttribute("finalMsg", finalMsg);
        mRedirect = "editor/whats-next.jsp";
    }

    @Override
    public String getRedirect() {
        return mRedirect;
    }

}
