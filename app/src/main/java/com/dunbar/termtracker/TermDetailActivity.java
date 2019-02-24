package com.dunbar.termtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TermDetailActivity extends AppCompatActivity {
    dbHelper db;

    EditText txtTitle;
    EditText txtStartDate;
    EditText txtEndDate;

    Term term;

    Intent intent;

    SimpleDateFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new dbHelper(this);
        setContentView(R.layout.term_detail_view);

        intent = getIntent();

        formatter = new SimpleDateFormat("MM/dd/yyyy");

        txtTitle = (EditText)findViewById(R.id.txtTermTitle);
        txtStartDate = (EditText)findViewById(R.id.txtTermStartDate);
        txtEndDate = (EditText)findViewById(R.id.txtTermEndDate);

        if(intent.getExtras().getBoolean("isNew")){
            createNewTerm();
        }else{
            loadExistingTerm(intent.getExtras().getInt("termId"));
        }
    }

    public void createNewTerm(){
        TextView lblTitle = (TextView)findViewById(R.id.lblTermTitle);
        lblTitle.setText("New Term");
        term = new Term();
    }

    public void loadExistingTerm(int termId){
        term = db.getTerm(termId);
        txtTitle.setText(term.getTitle());
        txtStartDate.setText(formatter.format(term.getStartDate()));
        txtEndDate.setText(formatter.format(term.getEndDate()));
    }

    public void saveTerm(View view){
        //grab input from the user
        try {
            term.setTitle(txtTitle.getText().toString());
            term.setStartDate(formatter.parse(txtStartDate.getText().toString()));
            term.setEndDate(formatter.parse(txtEndDate.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //save to the db
        if(intent.getExtras().getBoolean("isNew")){
            db.insertTerm(term);
        }else{
            db.updateTerm(term);
        }

        //return to term list
        goBack();
    }

    public void deleteTerm(View view){
        //do nothing if termId is null (meaning this is a new term)
        if(term.getId() == 0){
            Toast.makeText(getApplicationContext(),"Delete Failed - You must save this term before you can delete it. Press back to cancel the term.",Toast.LENGTH_LONG).show();
            return;
        }

        //make sure the term doesn't have courses associated with it first.
        List<Course> associatedCourses = db.getCourses(term.getId());
        if(associatedCourses.size() > 0){
            Toast.makeText(getApplicationContext(),"Delete failed - this term has courses associated to it. You must delete them first.",Toast.LENGTH_LONG).show();
            return;
        }

        //Only delete if the required conditions are met
        if(associatedCourses.size() == 0 && term.getId() != 0){
            db.deleteTerm(term.getId());
            Toast.makeText(getApplicationContext(),"Delete success!",Toast.LENGTH_LONG).show();
            goBack();
        }
    }

    public void viewCourses(View view){
        Intent intent = new Intent(this, CourseListActivity.class);
        intent.putExtra("termId",term.getId());
        startActivity(intent);
    }

    public void goBack(){
        super.onBackPressed();
    }
}
