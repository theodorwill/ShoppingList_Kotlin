package com.example.cba.shoppinglist;

import android.content.ContentValues;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/*
Klass som ärver sqlite standard klassen, här skapas databasen och metoder för att
skapa/lägga till data finns här
 */

public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(Context context) {
        super(context, DatabaseContract.DB_NAME, null, DatabaseContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DatabaseContract.DatabaseEntry.TABLE + " ( " +
                DatabaseContract.DatabaseEntry.COL_LIST_ITEM + " TEXT, " +
                DatabaseContract.DatabaseEntry.COL_LIST_TITLE + " TEXT NOT NULL);";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.DatabaseEntry.TABLE);
        onCreate(db);
    }
    public void recreateTable(){

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS lists");
        onCreate(db);
    }

    //Behöver lägga till en check här nere i metoden och se om titeln inte finns redan
    public void createList(String title) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.DatabaseEntry.COL_LIST_TITLE, title);
        values.putNull(DatabaseContract.DatabaseEntry.COL_LIST_ITEM);

        db.insert(DatabaseContract.DatabaseEntry.TABLE, null, values);
        db.close();

    }

    public void deleteList(String title){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DatabaseContract.DatabaseEntry.TABLE,
                DatabaseContract.DatabaseEntry.COL_LIST_TITLE + " LIKE ?",
                new String[] {title});

        db.close();
    }

    public void addItem(String list_item, String list_title) {
        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.DatabaseEntry.COL_LIST_TITLE, list_title);
        values.put(DatabaseContract.DatabaseEntry.COL_LIST_ITEM, list_item);

        db.insert(DatabaseContract.DatabaseEntry.TABLE, null, values);
        db.close();
    }
}
