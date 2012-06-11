package com.delivery.menu;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.MenuCommand;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.FlavourDao;
import com.delivery.persistent.PriceDao;
import com.delivery.persistent.ProductSizeDao;
import com.delivery.util.StringUtils;

public class AddFlavourCmd extends MenuCommand {

    private static final String SIZE_PATTERN  = "produto\\[tamanhos\\]\\[([0-9]*)\\]\\[tamanho\\]";

    private static final String PRICE_FORMAT = "produto[precos][%d][preco]";

    private String mRedirect;

    @Override
    public void execute(HttpServletRequest request) {
        try {
            // Get DataSource
            Context initContext   = new InitialContext();
            Context envContext    = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            int productId;
            try {
                productId = Integer.parseInt(request.getParameter("prodId"));
            } catch (NumberFormatException e) {
                Logger.error("Sabor sem produto???", e);
                throw new RuntimeException("Id de produto inválido");
            }

            String flavName = request.getParameter("nome");
            String flavDesc = request.getParameter("desc");
            String picturePath = request.getParameter("foto");

            final Flavour finalFlavour = new Flavour();
            finalFlavour.setProductId(productId);
            finalFlavour.setName(flavName);
            finalFlavour.setDescription(flavDesc);
            finalFlavour.setPicturePath(picturePath);

            final Map<String, String[]> requestParameters = request.getParameterMap();

            DaoManager manager = new DaoManager(dataSource);
            Flavour savedFlavour = (Flavour) manager.transaction(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    ArrayList<Flavour> toInsert = new ArrayList<Flavour>();
                    Flavour flavour = finalFlavour;

                    toInsert.add(flavour);
                    FlavourDao dao = manager.getFlavourDao();
                    int[] saved = dao.save(toInsert);
                    if (saved != null && saved.length > 0) {
                        flavour.setId(dao.getLastSavedId());

                        // Depois de inserir o sabor, vamos inserir os precos de cada tamanho
                        List<Price> prices = getPricesFromParameters(requestParameters, flavour.getId());
                        if (prices != null && prices.size() > 0) {
                            PriceDao pDao = manager.getPriceDao();
                            saved = pDao.save(prices);
                            ProductSizeDao pSDao = manager.getProductSizeDao();
                            if (saved != null && saved.length > 0) {
                                Logger.debug(saved.length + " precos para " + flavour.getName());
                                // Para colocar todos os objetos 'preco' no sabor, e melhor fazer uma query no db
                                // Deste modo os objetos ja veem com os Ids e todos os outros campos preenchidos!
                                Price priceQuery = new Price();
                                priceQuery.setFlavourId(flavour.getId());
                                List<Price> savedPrices = pDao.get(priceQuery);
                                flavour.setPrices(savedPrices);

                                ProductSize sizeQuery = new ProductSize();
                                sizeQuery.setProductId(flavour.getProductId());
                                List<ProductSize> savedSizes = pSDao.get(sizeQuery);

                                HashMap<String, Double> cost = new HashMap<String, Double>();
                                for (Price p : savedPrices) {
                                    for (ProductSize pS : savedSizes) {
                                        if (p.getSizeId() == pS.getId()) {
                                            cost.put(pS.getName(), p.getPrice());
                                            break;
                                        }
                                    }
                                }
                                flavour.setCacheCost(cost);
                            } else {
                                // Nenhum preco foi inserido, cancela transacao!
                                manager.cancelTransaction();
                            }
                        } else {
                            // Nenhum preco a ser inserido, cancela transacao!
                            manager.cancelTransaction();
                        }
                        return flavour;
                    }
                    return null;
                }
            });


            if (savedFlavour != null) {
                Logger.debug("O sabor foi salvo com sucesso!");
                // Vamos mandar o usuario para a pagina final com uma mensagem de sucesso!
                request.setAttribute("finalMsg", "Sabor adicionado ao produto com sucesso! - '" + savedFlavour.getName() + "'");
                if (!StringUtils.isEmpty(savedFlavour.getPicturePath())) {
                    request.setAttribute("finalPicture", savedFlavour.getPicturePath());
                }
                mRedirect = "cardapio/novoProd-cont.jsp";
            } else {
                // TODO retornar pagina de erro ao usuario. sabor nao foi inserido!!!
            }

        } catch(NamingException e) {
            Logger.error("NamingException", e);
        }
    }

    private List<Price> getPricesFromParameters(Map<String, String[]> parameters, int flavourId) {
        ArrayList<Price> prices = new ArrayList<Price>();

        Iterator<String> it = parameters.keySet().iterator();
        while (it.hasNext()) {
            String sizeKey = it.next();
            if (StringUtils.matches(SIZE_PATTERN, sizeKey)) {
                String sizeVal = parameters.get(sizeKey)[0];

                Pattern p = Pattern.compile(SIZE_PATTERN);
                Matcher m = p.matcher(sizeKey);
                m.find(); // Sempre vai ser verdade se passou pelo if
                int index = Integer.parseInt(m.group(1)); // Nao sera lancada exception se passou no if!
                String priceKey = String.format(PRICE_FORMAT, index);
                String priceVal = parameters.get(priceKey)[0];

                if (!StringUtils.isEmpty(sizeVal) && !StringUtils.isEmpty(priceVal)) {
                    Logger.debug("Preco de sabor: [" + sizeKey + " = " + sizeVal + "]" +
                    		"[" + priceKey + " = " + priceVal + "]");
                    Price price = new Price();
                    price.setFlavourId(flavourId);
                    try {
                        price.setSizeId(Integer.parseInt(sizeVal));
                        // Para evitar problemas, vamos normalizar a string
                        // trocando virgulas por pontos ja que, para o usuario, nao faz diferenca
                        priceVal = priceVal.replace(',', '.');
                        price.setPrice(Double.parseDouble(priceVal));
                    } catch (NumberFormatException e) {
                        Logger.error("Erro ao parsear o preco!", e);
                        // Houve um problema com este preco, continua para tentar
                        // adicionar os proximos (se houverem). Se nenhum for adicionado.
                        continue;
                    }
                    prices.add(price);
                }
            }
        }

        return prices;
    }

    @Override
    public String getRedirect() {
        return mRedirect;
    }

}
