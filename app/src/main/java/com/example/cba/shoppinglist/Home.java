package com.example.cba.shoppinglist;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    private DatabaseHandler dbHandler;
    private ListView mListTitleView;
    private ArrayAdapter<String> mAdapter;
    private ArrayList<String> titleList;
    public final static String EXTRA_MESSAGE = "com.example.cba.shoppinglist.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mListTitleView = (ListView) findViewById(R.id.home_lv); //koppla listview till xml
        dbHandler = new DatabaseHandler(this);

        updateListCollection();

    }

    //Start of Create List popup
    public void showCreateListDialog(View view){

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(Home.this);
        View mView = getLayoutInflater().inflate(R.layout.dialog_create_list, null);
        final EditText mCreateList = (EditText) mView.findViewById(R.id.etListName);
        Button mCreate = (Button) mView.findViewById(R.id.create);

        mBuilder.setView(mView);
        final AlertDialog dialog = mBuilder.create();
        dialog.show();

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mCreateList.getText().toString().isEmpty()){

                    dbHandler.createList(mCreateList.getText().toString());
                    dialog.dismiss();
                    Toast.makeText(Home.this,
                            "List created",
                            Toast.LENGTH_SHORT).show();

                    updateListCollection();

                }else{
                    Toast.makeText(Home.this,
                            R.string.error_create_list_msg,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }//End of Create List popup

    /*
    Metod för att populera listviewen med datan från db.
    Den tar in alla titlar på listorna och sätter in dom i en arraylist.
    Sedan visas de i listviewen via en arrayadapter som för in datan från arrayen.
    Längst ner i koden sätter vi en onItemClickListener på varje fält i ListViewen
    så att beroende på vilken lista du klickar på, tas du till SecondActivity.
    En sträng med titeln skickas med och rätt titel sätts i nästa screen.
     */
    public void updateListCollection(){
        titleList = new ArrayList<>();
        SQLiteDatabase db = dbHandler.getReadableDatabase();
        Cursor cursor = db.query(true, DatabaseContract.DatabaseEntry.TABLE,  //true = tar ej duplicates
                new String[]{DatabaseContract.DatabaseEntry.COL_LIST_TITLE},
                null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            int idx = cursor.getColumnIndex(DatabaseContract.DatabaseEntry.COL_LIST_TITLE);
            titleList.add(cursor.getString(idx));
        }

        if (mAdapter == null) {
            mAdapter = new ArrayAdapter<>(this,
                    R.layout.list_collection,
                    R.id.list_title,
                    titleList);
            mListTitleView.setAdapter(mAdapter);
        } else {
            mAdapter.clear();
            mAdapter.addAll(titleList);
            mAdapter.notifyDataSetChanged();
        }

        cursor.close();
        db.close();

        mListTitleView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(Home.this, SecondActivity.class);
                intent.putExtra(EXTRA_MESSAGE, titleList.get(position));
                startActivity(intent);
            }
        });
    }

}
