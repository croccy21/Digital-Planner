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

    private EditText editName;
    private EditText editDesctription;

    public static String PARAMETER_NAME = "name";
    public static String PARAMETER_DESCRIPTION = "description";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homework_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        editName = (EditText) findViewById(R.id.homework_edit_name);
        editDesctription = (EditText) findViewById(R.id.homework_edit_description);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void saveData(View view) {
        Intent data = new Intent();
        data.putExtra(PARAMETER_NAME, editName.getText().toString());
        data.putExtra(PARAMETER_DESCRIPTION, editDesctription.getText().toString());
        setResult(RESULT_OK, data);
        finishAfterTransition();
    }

    public void cancelEdit(View view){
        setResult(RESULT_CANCELED);
        finishAfterTransition();
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra(PARAMETER_NAME, editName.getText().toString());
        data.putExtra(PARAMETER_DESCRIPTION, editDesctription.getText().toString());
        setResult(RESULT_OK, data);
        finishAfterTransition();
    }


}
