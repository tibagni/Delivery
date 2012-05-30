package com.delivery.pageloader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.engine.command.PageLoaderCommand;

public class NewProdLoader extends PageLoaderCommand{

    @Override
    public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {

    }

    @Override
    public String getRedirect() {
        return "cardapio/novoProd.jsp";
    }

}
