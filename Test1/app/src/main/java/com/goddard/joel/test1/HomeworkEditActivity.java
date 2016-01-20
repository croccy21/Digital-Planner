package com.goddard.joel.test1;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

public class HomeworkEditActivity extends AppCompatActivity {

    public static final String PARAMETER_ID = "id";
    public static final String PARAMETER_NAME = "name";
    public static final String PARAMETER_DESCRIPTION = "description";

    private EditText editName;
    private EditText editDesctription;
    private int id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editName = (EditText) findViewById(R.id.homework_edit_name);
        editDesctription = (EditText) findViewById(R.id.homework_edit_description);
        Intent i = getIntent();
        if(i!=null){
            id = i.getIntExtra(PARAMETER_ID,-1);
            if(id==-1){
                Intent data = new Intent();
                data.putExtra(PARAMETER_ID, id);
                setResult(RESULT_CANCELED, data);
                finishAfterTransition();
            }
            else{
                String name = i.getStringExtra(PARAMETER_NAME);
                String description = i.getStringExtra(PARAMETER_DESCRIPTION);

                if (name!=null && description!=null){
                    editName.setText(name);
                    editDesctription.setText(description);
                }
            }
        }
    }


    public void saveData(View view) {
        Intent data = new Intent();
        data.putExtra(PARAMETER_ID, id);
        data.putExtra(PARAMETER_NAME, editName.getText().toString());
        data.putExtra(PARAMETER_DESCRIPTION, editDesctription.getText().toString());
        setResult(RESULT_OK, data);
        finishAfterTransition();
    }

    public void cancelEdit(View view){
        Intent data = new Intent();
        data.putExtra(PARAMETER_ID, id);
        setResult(RESULT_CANCELED, data);
        finishAfterTransition();
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(PARAMETER_ID, id);
        data.putExtra(PARAMETER_NAME, editName.getText().toString());
        data.putExtra(PARAMETER_DESCRIPTION, editDesctription.getText().toString());
        setResult(RESULT_OK, data);
        finishAfterTransition();
    }


}
