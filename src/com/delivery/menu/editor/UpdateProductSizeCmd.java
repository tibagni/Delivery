package com.delivery.menu.editor;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import com.delivery.Logger;
import com.delivery.engine.command.MenuEditorCommand;
import com.delivery.menu.ProductSize;
import com.delivery.persistent.DaoManager;
import com.delivery.persistent.ProductSizeDao;
import com.delivery.util.StringUtils;

public class UpdateProductSizeCmd extends MenuEditorCommand {

    @Override
    public void execute(HttpServletRequest request) {
        try {
            // Get DataSource
            Context initContext  = new InitialContext();
            Context envContext  = (Context)initContext.lookup("java:/comp/env");
            DataSource dataSource = (DataSource)envContext.lookup("jdbc/deliveryDB");

            final String sizeName = request.getParameter("name");
            final String sizeId = request.getParameter("id");

            Logger.debug("[UpdateProductSizeCmd] - name: " + sizeName + " id: " + sizeId);

            DaoManager daoManager = new DaoManager(dataSource);
            daoManager.execute(new DaoManager.DaoCommand() {
                @Override public Object execute(DaoManager manager) throws SQLException {
                    ProductSize toUpdate = new ProductSize();
                    toUpdate.setName(sizeName);
                    if (!StringUtils.isEmpty(sizeId)) {
                        try {
                            toUpdate.setId(Integer.parseInt(sizeId));
                        } catch (NumberFormatException e) {
                            Logger.error("Nao foi possivel atualizar tamanho: " + sizeName, e);
                            return null;
                        }
                    }
                    ProductSizeDao dao = manager.getProductSizeDao();
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
