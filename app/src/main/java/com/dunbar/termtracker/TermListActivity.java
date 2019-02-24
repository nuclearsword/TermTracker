package com.dunbar.termtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class TermListActivity extends AppCompatActivity {
    dbHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new dbHelper(this);
        setContentView(R.layout.term_list_layout);

        initializeTermList();

    }

    @Override
    public void onResume(){
        super.onResume();
        initializeTermList();

    }

    public void initializeTermList(){
        ListView lvTerms = (ListView)findViewById(R.id.lvTerms);
        List<Term> terms = db.getTerms();

        ArrayAdapter<Term> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                terms
        );

        lvTerms.setAdapter(arrayAdapter);

        lvTerms.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View parentView, int itemPosition, long itemId){
                Term term = (Term)parent.getAdapter().getItem(itemPosition);
                viewTerm(parentView, term.getId());
            }
        });
    }

    public void viewTerm(View view, int termId){
        Intent intent = new Intent(this, TermDetailActivity.class);
        intent.putExtra("isNew",false);
        intent.putExtra("termId",termId);
        startActivity(intent);
    }

    public void addTerm(View view){
        Intent intent = new Intent(this, TermDetailActivity.class);
        intent.putExtra("isNew",true);
        startActivity(intent);
    }


}
