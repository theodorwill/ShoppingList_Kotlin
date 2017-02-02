package com.example.cba.shoppinglist;

import android.provider.BaseColumns;

/*
Denna klassen innehåller bara information om hur databasen ska se ut när den skall skapas.
Variablerna här kallas i de andra klasserna för att lätt få namnet på kolumnerna osv.
 */

public class DatabaseContract {
    public static final String DB_NAME = "com.example.cba.shoppinglist";
    public static final int DB_VERSION = 1;

    public class DatabaseEntry implements BaseColumns {
        public static final String TABLE = "lists";

        public static final String COL_LIST_TITLE = "title";
        public static final String COL_LIST_ITEM = "item";
    }
}