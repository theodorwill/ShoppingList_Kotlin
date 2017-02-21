package com.example.cba.shoppinglist;

import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class SecondActivity extends AppCompatActivity {
    private ListItemAdapter mAdapter1;
    private DatabaseHandler dbHandler;
    private ListView mListItemView;
    private ArrayList<String> itemList;
    private String title;
    // String variabel för createItem dialogrutan för färgvärdet.
    private String color;
    // ArrayList för färgvärdena som hämtas ut från db
    private ArrayList<String> colors;
    private String order = "noSort";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        // Tillbaka knapp i Actionbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mListItemView = (ListView) findViewById(R.id.list_show_items); //kopplar vår ListView här
        dbHandler = new DatabaseHandler(this); //nytt objekt av databasehandler som vi kommer använda

        // Här tar vi emot ett intent från MainActivity med titeln på listan
        // beroende på vilken man tryckt på och sätter titeln här i appbaren.
        Intent intent = getIntent();
        title = intent.getStringExtra(Home.EXTRA_MESSAGE);
        setTitle(title);

        updateItemView("noSort");

        //Delete item on itemLongClick
        mListItemView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int pos, long id) {

                dbHandler.deleteItem(itemList.get(pos), title);
                updateItemView(order);
                return true;
            }
        });

        //Markera item som klar onClick
        mListItemView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                View rowView = mListItemView.getChildAt(pos);
                CheckBox cBox = (CheckBox) rowView.findViewById(R.id.checkBox);
                TextView itemTxt = (TextView) rowView.findViewById(R.id.list_item);

                if(cBox.isChecked() != true) {
                    cBox.setChecked(true);
                    itemTxt.setTextColor(Color.parseColor("#757575"));
                    itemTxt.setBackgroundColor(Color.parseColor("#424242"));
                } else {
                    cBox.setChecked(false);
                    itemTxt.setTextColor(Color.parseColor("#212121"));
                    itemTxt.setBackgroundColor(Color.parseColor(colors.get(pos)));
                }
            }
        });
    }

    //Lägger till ikonerna i appbaren för denna activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /*
    Sätter color variabeln beroende på vilken radiobutton som är vald
    i createItem dialogrutan, så att det kan passeras in i
    db
     */
    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio1:
                if (checked)
                    color = "#ef5350";
                    break;
            case R.id.radio2:
                if (checked)
                    color = "#66bb6a";
                    break;
            case R.id.radio3:
                if (checked)
                    color = "#42a5f5";
                    break;
            case R.id.radio4:
                if (checked)
                    color = "#ffee58";
                    break;
        }
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

            // Sätt color variabeln till default white för item om ingen färg är vald
            color = "#FFFFFF";


            mAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!mItemName.getText().toString().isEmpty()) {

                        //här anropar vi addItem metoden, lägger till datan i db
                        //Vi tar in strängen från dialog rutan(mItemName)
                        dbHandler.addItem(mItemName.getText().toString(), title, color);
                        dialog.dismiss();
                        updateItemView(order);

                    } else {
                        Toast.makeText(SecondActivity.this,
                                R.string.error_create_item_msg,
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        if(item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if(item.getItemId() == R.id.menuSortByColor) {
            order = "color";
            updateItemView(order);
        }
        if(item.getItemId() == R.id.menuSortASC) {
            order = "asc";
            updateItemView(order);
        }
        if(item.getItemId() == R.id.menuSortDESC) {
            order = "desc";
            updateItemView(order);
        }

        return true;
    }

    /*
    Metod för att hämta in alla items för listan från databasen.
    Det kommer finnas något NULL värde i tabellen, därför gör vi en check
    och lägger bara till items i ArrayListen om raden innehåller ett värde.

    Via en Arrayadapter populerar vi vår listview
     */
    public void updateItemView(String order) {
        itemList = new ArrayList<>();
        colors = new ArrayList<>();

        String orderBy;

        if(order == "color") {
            orderBy = DatabaseContract.DatabaseEntry.COL_LIST_COLOR + " ASC";
        }
        else if(order == "asc") {
            orderBy = DatabaseContract.DatabaseEntry.COL_LIST_ITEM + " ASC";
        }
        else if(order == "desc") {
            orderBy = DatabaseContract.DatabaseEntry.COL_LIST_ITEM + " DESC";
        }
        else {
            orderBy = null;
        }

        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(DatabaseContract.DatabaseEntry.TABLE,
                new String[]{DatabaseContract.DatabaseEntry.COL_LIST_TITLE, DatabaseContract.DatabaseEntry.COL_LIST_ITEM,
                DatabaseContract.DatabaseEntry.COL_LIST_COLOR},
                DatabaseContract.DatabaseEntry.COL_LIST_TITLE + " = ?",
                new String[]{title}, null, null, orderBy
        );
        while (cursor.moveToNext()) {
            int index = cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COL_LIST_ITEM);
            int index2 = cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COL_LIST_COLOR);
            if (cursor.isNull(index) != true) {
                itemList.add(cursor.getString(index));
                colors.add(cursor.getString(index2));
            }
        }

        mAdapter1 = new ListItemAdapter(this, itemList, colors);
        mListItemView.setAdapter(mAdapter1);

        cursor.close();
        db.close();

    }

}