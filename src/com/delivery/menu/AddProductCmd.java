package com.delivery.menu;

import javax.servlet.http.HttpServletRequest;

import com.delivery.engine.command.MenuCommand;

public class AddProductCmd extends MenuCommand {

    @Override
    public void execute(HttpServletRequest request) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getRedirect() {
        //TODO
        return "Delivery/PageLoader";
    }

}
