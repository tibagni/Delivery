package com.delivery.servlet.admin;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.Logger;
import com.delivery.engine.command.AdminPageLoaderCommand;
import com.delivery.engine.command.Command;
import com.delivery.engine.command.Command.CommandNotFoundException;

public class PageLoader extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String cmdName = req.getParameter("page");
        try {
            AdminPageLoaderCommand pageLoader = Command.getFromName(cmdName, AdminPageLoaderCommand.class);
            pageLoader.prepareToLoad(req, resp);
            RequestDispatcher dispatcher = req.getRequestDispatcher(pageLoader.getRedirect());
            dispatcher.forward(req, resp);
        } catch (CommandNotFoundException e) {
            Logger.warning("[AdminPageLoader]Comando n�o definido! - " + cmdName);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // N�o aceita requisi��es POST
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}