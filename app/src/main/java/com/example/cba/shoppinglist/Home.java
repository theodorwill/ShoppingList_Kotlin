package com.example.cba.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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

                    Intent intent = new Intent(Home.this, SecondActivity.class);
                    startActivity(intent);

                }else{
                    Toast.makeText(Home.this,
                            R.string.error_create_list_msg,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }//End of Create List popup

}
