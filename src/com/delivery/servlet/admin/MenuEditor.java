package com.delivery.servlet.admin;

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
import com.delivery.engine.command.MenuEditorCommand;
import com.delivery.util.StringUtils;

/**
 * Concentra as requisicoes referentes ao cardapio
 * Adicionar categoria, remover categoria, adicionar produto...
 *
 *
 * @author Tiago
 *
 */
public class MenuEditor extends HttpServlet {
    private static final long serialVersionUID = 3520609095918073998L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Logger.debug("Menu servlet!");
        String cmdName = req.getParameter("cmd");
        MenuEditorCommand command;

        // TODO - Talvez seja melhor criar um servlet separado para esta acao
        // Nao me parece muito adequado atrelar o comando de upload com as acoes do cardapio!

        // Antes de inferir o comando, vamos analisar a requisicao.
        // Se o conteudo da requisicao for multipart/form-data, tratamos como
        // requisicao de upload de imagem
        if (ServletFileUpload.isMultipartContent(req)) {
            Logger.debug("Conteudo multipart, iniciando comando de upload...");
            cmdName = "UploadImage";
        }
        Logger.debug("[MenuEditor] Cmd: " + cmdName);
        try {
            command = Command.getFromName(cmdName, MenuEditorCommand.class);
            command.execute(req);
            if (!StringUtils.isEmpty(command.getRedirect())) {
                RequestDispatcher dispatcher = req.getRequestDispatcher(command.getRedirect());
                dispatcher.forward(req, resp);
            } else {
                // Se nao tem view pra mostrar, manda status OK pelo menos!
                resp.setStatus(HttpServletResponse.SC_OK);
            }
        } catch (CommandNotFoundException e) {
            Logger.warning("[Menu]Comando não definido! - " + cmdName);
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (Exception e) {
            Logger.error("Erro!", e);

            // TODO mostrar algo ao usuario
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
