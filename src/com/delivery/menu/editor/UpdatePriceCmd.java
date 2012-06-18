package com.delivery.menu.editor;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.MenuEditorCommand;
import com.delivery.menu.Price;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.PriceDao;
import com.delivery.util.StringUtils;

public class UpdatePriceCmd extends MenuEditorCommand {

    @Override
    public void execute(HttpServletRequest request) {
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            final String flavourId = request.getParameter("flavourId");
            final String sizeId = request.getParameter("sizeId");
            final String price = request.getParameter("price");

            Logger.debug("[UpdatePriceCmd] - flavourId: " + flavourId + " sizeId: " + sizeId + " price: " + price);

            DaoManager daoManager = new DaoManager(dataSource);
            daoManager.execute(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    Price toUpdate = new Price();
                    if (!StringUtils.isEmpty(sizeId)) {
                        try {
                            toUpdate.setSizeId(Integer.parseInt(sizeId));
                            toUpdate.setFlavourId(Integer.parseInt(flavourId));
                            toUpdate.setPrice(Double.parseDouble(price.replace(',', '.')));
                        } catch (NumberFormatException e) {
                            Logger.error("Nao foi possivel atualizar preco", e);
                            return null;
                        } catch (NullPointerException npe) {
                            Logger.error("Nao foi possivel atualizar preco", npe);
                            return null;
                        }
                    }
                    PriceDao dao = manager.getPriceDao();
                    dao.update(toUpdate);
                    return null;
                }
            });
        } catch (NamingException e) {
            Logger.error("NamingException", e);
        }
    }

    @Override
    public String getRedirect() {
        // Nao mostra nenhuma view
        return null;
    }

}
