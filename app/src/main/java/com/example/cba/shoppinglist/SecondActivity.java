package com.example.cba.shoppinglist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    private ArrayAdapter<String> mAdapter1;
    private DatabaseHandler dbHandler;
    private ListView mListItemView;
    private ArrayList<String> itemList;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        mListItemView = (ListView) findViewById(R.id.list_show_items); //kopplar vår ListView här
        dbHandler = new DatabaseHandler(this); //nytt objekt av databasehandler som vi kommer använda

        // Här tar vi emot ett intent från MainActivity med titeln på listan
        // beroende på vilken man tryckt på och sätter titeln här i appbaren.
        Intent intent = getIntent();
        title = intent.getStringExtra(Home.EXTRA_MESSAGE);
        setTitle(title);

        //Anropar updateItemView() metoden när denna aktivitet skapas.
        updateItemView();
    }

    //Lägger till ikonerna i appbaren för denna activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_add) {

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(SecondActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.dialog_create_item, null);
            final EditText mItemName = (EditText) mView.findViewById(R.id.etItemName);
            Button mAdd = (Button) mView.findViewById(R.id.buttonAdd);

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!mItemName.getText().toString().isEmpty()){

                        //här anropar vi addItem metoden, lägger till datan i db
                        //Vi tar in strängen från dialog rutan(mItemName)
                        dbHandler.addItem(mItemName.getText().toString(), title);
                        dialog.dismiss();
                        updateItemView();

                    }else{
                        Toast.makeText(SecondActivity.this,
                                R.string.error_create_item_msg,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        else {
            //gör inget i guess
        }

        return true;
    }
    /*
    Metod för att hämta in alla items för listan från databasen.
    Det kommer finnas något NULL värde i tabellen, därför gör vi en check
    och lägger bara till items i ArrayListen om raden innehåller ett värde.

    Via en Arrayadapter populerar vi vår listview
     */
    public void updateItemView(){
        itemList = new ArrayList<>();

        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.DatabaseEntry.TABLE,
                new String[]{DatabaseContract.DatabaseEntry.COL_LIST_TITLE, DatabaseContract.DatabaseEntry.COL_LIST_ITEM},
                DatabaseContract.DatabaseEntry.COL_LIST_TITLE + " = ?",
                new String[]{title}, null, null, null
        );
        while(cursor.moveToNext()) {
            int index = cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COL_LIST_ITEM);
            if (cursor.isNull(index) != true) {
                itemList.add(cursor.getString(index));

            }
        }

        if(mAdapter1 == null){
            mAdapter1 = new ArrayAdapter<>(this,
                    R.layout.list_item_view,
                    R.id.list_item,
                    itemList);
            mListItemView.setAdapter(mAdapter1);
        } else {
            mAdapter1.clear();
            mAdapter1.addAll(itemList);
            mAdapter1.notifyDataSetChanged();
        }

        cursor.close();
        db.close();

    }

}