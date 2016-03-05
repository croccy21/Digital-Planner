package com.goddard.joel.digitalplanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class SubjectManager extends AppCompatActivity {

    private static final int REQUEST_CODE_REDRAW = 1;
    private ListView l;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SubjectEdit.class);
                startActivityForResult(i, REQUEST_CODE_REDRAW);
            }
        });

        l = (ListView) findViewById(R.id.subject_list);
        l.setAdapter(new SubjectAdapter(this));
        l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getApplicationContext(), SubjectEdit.class);
                i.putExtra(SubjectEdit.EXTRA_ID, id);
                startActivityForResult(i, REQUEST_CODE_REDRAW);
            }
        });
    }

    private class SubjectAdapter extends ArrayAdapter {

        private final Database db;

        public SubjectAdapter(Context context) {
            super(context, android.R.layout.simple_list_item_1);
            db = new Database(context);
            Cursor c = DatabaseTableSubject.getAll(db);
            c.moveToFirst();
            for(int i=0; i<c.getCount(); i++){
                Subject s = new Subject(db, c.getLong(c.getColumnIndex(DatabaseTableSubject.FIELD_SUBJECT_ID)));
                add(s);
                c.moveToNext();
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(android.R.layout.simple_list_item_1, null);
            }

            TextView t = (TextView) v.findViewById(android.R.id.text1);
            Subject s = (Subject) getItem(position);
            t.setText(s.getName());
            return v;
        }

        @Override
        public long getItemId(int position) {
            Subject s = (Subject) getItem(position);
            return s.getId();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_CODE_REDRAW){
            l.setAdapter(new SubjectAdapter(this));
            l.invalidate();
        }
    }
}
