package com.delivery.servlet;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;

import com.delivery.Logger;
import com.delivery.engine.command.Command;
import com.delivery.engine.command.Command.CommandNotFoundException;
import com.delivery.engine.command.MenuCommand;
import com.delivery.util.StringUtils;

/**
 * Concentra as requisi��es referentes ao card�pio
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
        Logger.debug("Menu servlet!");
        String cmdName = req.getParameter("cmd");
        MenuCommand command;

        // Antes de inferir o comando, vamos analisar a requisicao.
        // Se o conteudo da requisicao for multipart/form-data, tratamos como
        // requisicao de upload de imagem
        if (ServletFileUpload.isMultipartContent(req)) {
            Logger.debug("Conteudo multipart, iniciando comando de upload...");
            cmdName = "UploadImage";
        }
        try {
            command = Command.getFromName(cmdName, MenuCommand.class);
            command.execute(req);
            if (!StringUtils.isEmpty(command.getRedirect())) {
                RequestDispatcher dispatcher = req.getRequestDispatcher(command.getRedirect());
                dispatcher.forward(req, resp);
            }
        } catch (CommandNotFoundException e) {
            Logger.warning("[Menu]Comando n�o definido! - " + cmdName);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            Logger.error("Erro!", e);

            // TODO mostrar algo ao usuario
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
