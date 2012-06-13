package com.delivery.pageloader.admin;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.engine.command.AdminPageLoaderCommand;

public class ProductEditorLoader extends AdminPageLoaderCommand{

    @Override
    public void prepareToLoad(HttpServletRequest req, HttpServletResponse resp) {
        // Nada pra preparar. todos os parametros do request ja foram passados!
    }

    @Override
    public String getRedirect() {
        return "editor/product-editor.jsp";
    }

}
