package com.delivery.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.engine.command.Command;
import com.delivery.engine.command.Command.CommandNotFoundException;
import com.delivery.engine.command.MenuCommand;
import com.delivery.util.StringUtils;

/**
 * Concentra as requisições referentes ao cardápio
 * Adicionar categoria, remover categoria, adicionar produto...
 *
 *
 * @author Tiago
 *
 */
public class Menu extends HttpServlet {
    private static final long serialVersionUID = 3520609095918073998L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String cmdName = req.getParameter("cmd");
        MenuCommand command;
        try {
            command = Command.getFromName(cmdName, MenuCommand.class);
            command.execute(req);
            if (!StringUtils.isEmpty(command.getRedirect())) {
                RequestDispatcher dispatcher = req.getRequestDispatcher(command.getRedirect());
                dispatcher.forward(req, resp);
            }
        } catch (CommandNotFoundException e) {
            // TODO log erro
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
