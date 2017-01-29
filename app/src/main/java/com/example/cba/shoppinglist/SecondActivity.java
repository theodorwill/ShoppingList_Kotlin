package com.example.cba.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        setTitle("Your list"); //sätter titel i appbaren, antar att vi ska ha själva listans namn här
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

                        //lägg till varan

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
}