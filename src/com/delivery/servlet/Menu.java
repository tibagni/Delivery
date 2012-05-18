package com.delivery.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.command.MenuCommand;

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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String responseString = null;

        try {
            String cmd = req.getParameter("cmd") + "Cmd";
            Class<?> cmdClass = Class.forName(cmd);
            MenuCommand command = (MenuCommand) cmdClass.newInstance();
            responseString = command.execute(req);
        } catch (ClassNotFoundException e) {
            // TODO return error (comando não existe)
            e.printStackTrace();
        } catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
