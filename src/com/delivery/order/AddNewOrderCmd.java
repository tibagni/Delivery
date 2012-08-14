package com.delivery.order;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.SessionConstants;
import com.delivery.engine.command.OrderCommand;
import com.delivery.menu.Optional;
import com.delivery.menu.Price;
import com.delivery.menu.ProductSize;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.OptionalDao;
import com.delivery.persistent.PriceDao;
import com.delivery.persistent.ProductSizeDao;

public class AddNewOrderCmd extends OrderCommand {

    @Override
    public void execute(HttpServletRequest request) {
        Map<String, String[]> parameters;

        Order order = (Order) request.getSession().getAttribute(SessionConstants.ORDER);
        if (order == null) {
            order = new Order();
        }

        int temporaryId = order.getNextTemporaryId();

        final OrderItem item = new OrderItem();
        parameters = request.getParameterMap();
        String[] flavours = parameters.get("flavour");
        String[] optionals = parameters.get("optional");
        String[] product = parameters.get("product");
        String[] size = parameters.get("size");

        if (product == null || size == null || flavours == null ||
            product.length == 0 || size.length == 0 || flavours.length == 0) {
            //mRedirect TODO
            return;
        }
        item.setmTeporaryId(temporaryId);
        try {
            String[] productParts = product[0].split("-");
            item.setProductId(Integer.parseInt(productParts[0]));
            item.setDescription(productParts[1]);
            item.setSize(Integer.parseInt(size[0]));
            for (String f : flavours) {
                String[] flavourParts = f.split("-");
                item.addNewFlavour(Integer.parseInt(flavourParts[0]), flavourParts[1]);
            }

            if (optionals != null && optionals.length > 0) {
                for (String o : optionals) {
                    String optionalParts[] = o.split("-");
                    item.addNewOptional(Integer.parseInt(optionalParts[0]), optionalParts[1]);
                }
            }

            final int finalSizeId = item.getSize();
            final Set<Integer> finalFlavoursSet = item.getFlavours().keySet();

            double orderItemPrice = 0;
            try {
                // Get DataSource
                Context initContext  = new InitialContext();
                Context envContext  = (Context)initContext.lookup("java:/comp/env");
                DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

                DaoManager daoManager = new DaoManager(dataSource);

                // Consulta o preco dos sabores. o preco de item sera igual ao do sabor mais caro
                orderItemPrice = (Double) daoManager.execute(new DaoManager.DaoCommand() {
                    @Override public Object execute(DaoManager manager) throws SQLException {
                        double higherPrice = 0;
                        Price query = new Price();
                        PriceDao dao = manager.getPriceDao();
                        query.setSizeId(finalSizeId);
                        List<Price> result;
                        double currentPrice;
                        for (Integer fId : finalFlavoursSet) {
                            query.setFlavourId(fId);
                            result = dao.get(query);
                            currentPrice = result.get(0).getPrice();
                            if (currentPrice > higherPrice) {
                                higherPrice = currentPrice;
                            }
                        }
                        // Antes de retornar aproveita para consultar o tamanho
                        ProductSize pSQuery = new ProductSize();
                        ProductSizeDao psDao = manager.getProductSizeDao();
                        pSQuery.setId(finalSizeId);
                        List<ProductSize> psResult = psDao.get(pSQuery);
                        item.setCachedSizeName(psResult.get(0).getName());

                        return higherPrice;
                    }
                });


                if (item.getOptionals() != null) {
                    final Set<Integer> finalOptionalsSet = item.getOptionals().keySet();
                    // Consulta os precos dos opcionais e soma ao valor final
                    orderItemPrice += (Double) daoManager.execute(new DaoManager.DaoCommand() {
                        @Override public Object execute(DaoManager manager) throws SQLException {
                            double sum = 0;
                            Optional query = new Optional();
                            OptionalDao dao = manager.getOptionalDao();

                            List<Optional> result;
                            for (Integer oId : finalOptionalsSet) {
                                query.setId(oId);
                                result = dao.get(query);
                                sum += result.get(0).getPrice();
                            }
                            return sum;
                        }
                    });
                }
            } catch (NamingException e) {
                Logger.error("NamingException", e);
                throw e;
            }

            item.setPrice(orderItemPrice);
            order.addItem(item);
            request.getSession().setAttribute("SessionOrder", order);
        } catch (Exception e) {
            Logger.error("erro ao inserir item de pedido", e);
            //mRedirect TODO
        }
    }

    @Override
    public String getRedirect() {
        return "order/OrderSuccess.jsp";
    }

}
