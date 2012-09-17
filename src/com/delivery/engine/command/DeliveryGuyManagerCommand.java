package com.delivery.engine.command;

import javax.servlet.http.HttpServletRequest;

import com.delivery.engine.command.Command.CommandBase;

/**
 * Comandos relacionados a manipuacao do cardapio. Adicionar categoria, produto
 * entre outras coisas.
 */
public abstract class DeliveryGuyManagerCommand implements CommandBase {

    public abstract void execute(HttpServletRequest request);
    public abstract String getRedirect();

    /**
     * Monta o nome completo da classe de comando a partir do simpleName.
     * Adicionando o prefixo (nome do pacote) e o sufixo (Cmd)<br>
     * <br>
     * <b>IMPORTANTE:</b> Todos os comandos que extenderem esta classe
     * devem estar em "com.delivery.admin.deliveryguy" e devem terminar em "Cmd"
     *
     * @param simpleName
     * @return
     */
    public static String getFullClassName(String simpleName) {
        return "com.delivery.admin.deliveryguy." + simpleName + "Cmd";
    }
}