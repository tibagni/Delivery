package com.delivery.engine.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.delivery.engine.command.Command.CommandBase;

/**
 * Comando responsavel apenas por carregar as paginas (views) e seus conteudos
 * (query no banco de dados). Comandos que estenderem esta classe nao deverao
 * realizar operacoes com o banco de dados que nao sejam consultas para a exibicao correta das views!
 */
public abstract class PageLoaderCommand implements CommandBase {
    public abstract void prepareToLoad(HttpServletRequest req, HttpServletResponse resp);
    public abstract String getRedirect();

    /**
     * Monta o nome completo da classe de comando a partir do simpleName.
     * Adicionando o prefixo (nome do pacote) e o sufixo (Loader)<br>
     * <br>
     * <b>IMPORTANTE:</b> Todos os comandos que extenderem esta classe
     * devem estar em "com.delivery.pageloader" e devetm terminar em "Loader"
     *
     * @param simpleName
     * @return
     */
    public static String getFullClassName(String simpleName) {
        return "com.delivery.pageloader." + simpleName + "Loader";
    }
}