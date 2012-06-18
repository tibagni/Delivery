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
        Logger.debug("[AdminPageLoader] Page=" + cmdName);
        try {
            AdminPageLoaderCommand pageLoader = Command.getFromName(cmdName, AdminPageLoaderCommand.class);
            pageLoader.prepareToLoad(req, resp);
            RequestDispatcher dispatcher = req.getRequestDispatcher(pageLoader.getRedirect());
            dispatcher.forward(req, resp);
        } catch (CommandNotFoundException e) {
            Logger.warning("[AdminPageLoader]Comando não definido! - " + cmdName);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            Logger.error("[AdminPageLoader] Erro ao tentar carregar pagina!", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Não aceita requisições POST
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}