package com.delivery.menu.editor;

import java.sql.SQLException;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.MenuEditorCommand;
import com.delivery.menu.Optional;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.OptionalDao;
import com.delivery.util.StringUtils;

public class UpdateOptionalCmd extends MenuEditorCommand {

    @Override
    public void execute(HttpServletRequest request) {
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            final String id = request.getParameter("id");
            final String price = request.getParameter("optionalPrice");

            Logger.debug("[UpdatePriceCmd] - Id: " + id + " price: " + price);

            DaoManager daoManager = new DaoManager(dataSource);
            daoManager.execute(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    OptionalDao dao = manager.getOptionalDao();
                    Optional query = new Optional();
                    Optional toUpdate;

                    if (!StringUtils.isEmpty(id)) {
                        try {
                            query.setId(Integer.parseInt(id));
                            List<Optional> result = dao.get(query);
                            if (result == null || result.size() == 0) {
                                return null;
                            }
                            toUpdate = result.get(0);

                            toUpdate.setPrice(Double.parseDouble(price.replace(',', '.')));
                        } catch (NumberFormatException e) {
                            Logger.error("Nao foi possivel atualizar opcional", e);
                            return null;
                        } catch (NullPointerException npe) {
                            Logger.error("Nao foi possivel atualizar opcional", npe);
                            return null;
                        }
                        dao.update(toUpdate);
                    }
                    return null;
                }
            });
        } catch (NamingException e) {
            Logger.error("NamingException", e);
        }
    }

    @Override
    public String getRedirect() {
        // Sem view pra mostrar
        return null;
    }

}
