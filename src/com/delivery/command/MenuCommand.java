package com.delivery.command;

import javax.servlet.http.HttpServletRequest;

public interface MenuCommand {

    String execute(HttpServletRequest request);
}