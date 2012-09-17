package com.delivery.servlet.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.Logger;
import com.delivery.engine.command.Command;
import com.delivery.engine.command.Command.CommandNotFoundException;
import com.delivery.engine.command.DeliveryGuyManagerCommand;
import com.delivery.util.StringUtils;

/**
 * Concentra as requisicoes referentes ao cardapio
 * Adicionar categoria, remover categoria, adicionar produto...
 *
 *
 * @author Tiago
 *
 */
public class DeliveryGuyManager extends HttpServlet {
    private static final long serialVersionUID = 3520609095918073998L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Logger.debug("Delivery Guy servlet!");
        String cmdName = req.getParameter("cmd");
        DeliveryGuyManagerCommand command;

        Logger.debug("[DeliveryGuyManager] Cmd: " + cmdName);
        try {
            command = Command.getFromName(cmdName, DeliveryGuyManagerCommand.class);
            command.execute(req);
            if (!StringUtils.isEmpty(command.getRedirect())) {
                RequestDispatcher dispatcher = req.getRequestDispatcher(command.getRedirect());
                dispatcher.forward(req, resp);
            } else {
                // Se nao tem view pra mostrar, manda status OK pelo menos!
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (CommandNotFoundException e) {
            Logger.warning("[DeliveryGuyManager]Comando não definido! - " + cmdName);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            Logger.error("Erro!", e);

            // TODO mostrar algo ao usuario
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
