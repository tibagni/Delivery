package com.delivery.command;

import javax.servlet.http.HttpServletRequest;

public class NewCategoryCmd implements MenuCommand {

    private static final String CATEGORY_NAME_PARAMETER = "catName";
    private static final String PARENT_CATEGORY_PARAMETER = "parent";

    @Override
    public String execute(HttpServletRequest request) {
        String name = request.getParameter(CATEGORY_NAME_PARAMETER);
        int parentId = Integer.parseInt(request.getParameter(PARENT_CATEGORY_PARAMETER));
        return null;
    }

}
