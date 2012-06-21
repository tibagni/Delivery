package com.delivery.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.Logger;
import com.delivery.engine.command.Command;
import com.delivery.engine.command.Command.CommandNotFoundException;
import com.delivery.engine.command.OrderCommand;
import com.delivery.util.StringUtils;

public class Order extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Não aceita requisições GET
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Logger.debug("Order servlet");
        String cmdName = req.getParameter("cmd");

        try {
            OrderCommand command = Command.getFromName(cmdName, OrderCommand.class);
            command.execute(req);
            if (!StringUtils.isEmpty(command.getRedirect())) {
                RequestDispatcher dispatcher = req.getRequestDispatcher(command.getRedirect());
                dispatcher.forward(req, resp);
            } else {
                // Se nao tem view pra mostrar, manda status OK pelo menos!
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (CommandNotFoundException e) {
            Logger.warning("[Order]Comando não definido! - " + cmdName);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            Logger.error("Erro!", e);

            // TODO mostrar algo ao usuario
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

}
