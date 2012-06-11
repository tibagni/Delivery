package com.delivery.pageloader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.engine.command.PageLoaderCommand;

public class NewProdLoader extends PageLoaderCommand{

    @Override
    public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
        // Nada pra preparar. todos os parametros do request ja foram passados!
    }

    @Override
    public String getRedirect() {
        return "cardapio/novoProd.jsp";
    }

}
