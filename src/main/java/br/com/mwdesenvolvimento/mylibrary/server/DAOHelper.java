package br.com.mwdesenvolvimento.mylibrary.server;

import com.j256.ormlite.dao.Dao;

/**
 * Created by geppetto on 04/12/15.
 */
public interface DAOHelper {
    <T> Dao<T, Integer> getDao(Class<T> entityClazz);
    <T, U> Dao<T, U> getDao(Class<T> entityClazz, Class<U> pkClass);
}
