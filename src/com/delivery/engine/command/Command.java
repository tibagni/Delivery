package com.delivery.engine.command;

import java.lang.reflect.Method;

public class Command {

    /**
     * Retorna o objeto de comando a partir de seu nome.
     * <br>
     * <b>IMPORTANTE:</b> 'cmdClass' deve implementar o metodo estatico
     * 'getFullClassName'
     *
     * @param cmdName
     * @param cmdClass
     * @return
     * @throws CommandNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static <T extends CommandBase> T getFromName(String cmdName,
            Class<T> cmdClass) throws CommandNotFoundException {
        T cmd = null;
        try {
            Method getFullClassName = cmdClass.getMethod("getFullClassName", String.class);
            cmdName = (String)getFullClassName.invoke(null, cmdName);

            Class<?> specificClass = Class.forName(cmdName);
            cmd = (T) specificClass.newInstance();
        } catch (NoSuchMethodException e) {
            // Este tipo de erro nao pode passar em branco. Alguem esqueceu de implementar
            // o metodo estatico "getFullClassName" na superclasse de comando
            throw new RuntimeException("getFullClassName nao esta definido em "
                    + cmdClass.getName());
        } catch (Exception e) {
            // Qualquer outro problema durante a instanciacao do comando
            // e tratado como command not found
            throw new CommandNotFoundException(e);
        }
        return cmd;
    }

    /** Interface de marcacao. Todos os comandos devem implementar */
    /*package*/ interface CommandBase {}

    public static class CommandNotFoundException extends Exception {
        private static final long serialVersionUID = 1L;

        public CommandNotFoundException(Exception e) {
            super(e);
        }
    }
}
