package com.delivery.persistent;

import java.sql.Connection;
import java.util.List;

public abstract class Dao<T> {
    protected Connection mConnection;

    public Dao(Connection connection) {
        mConnection = connection;
    }

    /**
     * Salva uma lista de objetos em disco (Ex: usando banco de dados)
     *
     * @param objectsToInsert Lista contendo os objetos a serem salvos
     * @return Numero de objetos salvos com sucesso
     */
    public abstract int[] save(List<T> objectsToInsert);

    /**
     * Atualiza objetos que ja foram salvos previamente em disco.
     * <br>
     * Os objetos devem conter um id unico para que possam ser identificados
     * (ex: em banco de dados seria a chave primaria)
     *
     * @param objectsToUpdate Lista contendo os objetos a serem atualizados
     * @return Numero de objetos atualizados com sucesso
     */
    public abstract int update(List<T> objectsToUpdate);

    /**
     * Remove objetos
     *
     * @param objectsToDelete
     * @return
     */
    public abstract int delete(List<T> objectsToDelete);

    /**
     * Recupera uma lista de objetos do disco baseado em parametros
     * definidos em 'param'
     *
     * @param param Define os parametros que serao utilizados na busca
     * @return Lista com os objetos encontrados
     */
    public abstract List<T> get(T param);
}
