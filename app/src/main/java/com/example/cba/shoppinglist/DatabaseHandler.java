package com.example.cba.shoppinglist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/*
Klass som ärver sqlite standard klassen, här skapas databasen och metoder för att
skapa/lägga till/modifiera data finns här
 */

public class DatabaseHandler extends SQLiteOpenHelper {


    public DatabaseHandler(Context context) {
        super(context, DatabaseContract.DB_NAME, null, DatabaseContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DatabaseContract.DatabaseEntry.TABLE + " ( " +
                DatabaseContract.DatabaseEntry.COL_LIST_ITEM + " TEXT, " +
                DatabaseContract.DatabaseEntry.COL_LIST_COLOR + " TEXT DEFAULT '#FFFFFF', " +
                DatabaseContract.DatabaseEntry.COL_LIST_TITLE + " TEXT NOT NULL);";

        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.DatabaseEntry.TABLE);
        onCreate(db);
    }

    //Metod för test-syfte endast, skapar om tabellen
    public void recreateTable(){

        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("DROP TABLE IF EXISTS lists");
        onCreate(db);
    }

    //Metod för att skapa listorna, kollar även om titeln redan existerar
    public boolean createList(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean exists = false;

        Cursor cursor = db.query(DatabaseContract.DatabaseEntry.TABLE,
                new String[]{DatabaseContract.DatabaseEntry.COL_LIST_TITLE},
                DatabaseContract.DatabaseEntry.COL_LIST_TITLE + " = ?",
                new String[]{title}, null, null, null
        );

        // kolla om titeln redan finns
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COL_LIST_TITLE);
            if (cursor.getString(index).equalsIgnoreCase(title)) {
                exists = true;
                break;

            }

        }

        if (!exists) {
            // Skapa lista med titeln
            ContentValues values = new ContentValues();
            values.put(DatabaseContract.DatabaseEntry.COL_LIST_TITLE, title);
            values.putNull(DatabaseContract.DatabaseEntry.COL_LIST_ITEM);

            db.insert(DatabaseContract.DatabaseEntry.TABLE, null, values);
            cursor.close();
            db.close();
        }
        else {
            cursor.close();
            db.close();
        }

        return exists;

    }

    public void deleteList(String title){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DatabaseContract.DatabaseEntry.TABLE,
                DatabaseContract.DatabaseEntry.COL_LIST_TITLE + " LIKE ?",
                new String[] {title});

        db.close();
    }

    public void addItem(String list_item, String list_title, String color) {
        SQLiteDatabase db = this.getReadableDatabase();


        ContentValues values = new ContentValues();
        values.put(DatabaseContract.DatabaseEntry.COL_LIST_TITLE, list_title);
        values.put(DatabaseContract.DatabaseEntry.COL_LIST_ITEM, list_item);
        values.put(DatabaseContract.DatabaseEntry.COL_LIST_COLOR, color);

        db.insert(DatabaseContract.DatabaseEntry.TABLE, null, values);
        db.close();
    }
    public void deleteItem(String list_item, String list_title) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DatabaseContract.DatabaseEntry.TABLE,
                DatabaseContract.DatabaseEntry.COL_LIST_ITEM + " LIKE ? AND " +
                DatabaseContract.DatabaseEntry.COL_LIST_TITLE + " LIKE ? ",
                new String[] {list_item, list_title});

        db.close();
    }
}
