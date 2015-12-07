package br.com.mwdesenvolvimento.mylibrary.server;

import br.com.mwdesenvolvimento.mylibrary.database.DatabaseHelperBehavior;

/**
 * Created by geppetto on 05/12/15.
 */
public class Initializer {
    public static final Initializer INSTANCE = new Initializer();
    private boolean initialized;
    private DAOHelper helper;
    private DatabaseHelperBehavior databaseHelperBehavior;

    private Initializer() {}

    public void init(DAOHelper helper, DatabaseHelperBehavior b) {
        if(initialized) throw new IllegalStateException("Already initialized!");
        this.helper = helper;
        this.databaseHelperBehavior = b;
        this.initialized = true;
    }

    public DAOHelper getDAOHelper() {
        if(!initialized) throw new IllegalStateException("It's not initialized!");
        return helper;
    }

    public DatabaseHelperBehavior getDatabaseHelperBehavior() {
        if(!initialized) throw new IllegalStateException("It's not initialized!");
        return databaseHelperBehavior;
    }
}
