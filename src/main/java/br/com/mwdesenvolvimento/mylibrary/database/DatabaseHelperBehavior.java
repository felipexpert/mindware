package br.com.mwdesenvolvimento.mylibrary.database;

import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by geppetto on 05/12/15.
 */
public interface DatabaseHelperBehavior {
    ArrayList<Cursor> getData(String Query);
}
