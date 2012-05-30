package com.delivery.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.engine.command.Command;
import com.delivery.engine.command.Command.CommandNotFoundException;
import com.delivery.engine.command.PageLoaderCommand;

public class PageLoader extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            PageLoaderCommand pageLoader = Command.getFromName(req.getParameter("page"), PageLoaderCommand.class);
            pageLoader.prepareToLoad(req, resp);
            RequestDispatcher dispatcher = req.getRequestDispatcher(pageLoader.getRedirect());
            dispatcher.forward(req, resp);
        } catch (CommandNotFoundException e) {
            // TODO log erro
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Não aceita requisições POST
        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}