package com.dunbar.termtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class AssessmentDetailActivity extends AppCompatActivity {
    dbHelper db;
    EditText txtStartDate;
    EditText txtDueDate;
    EditText txtNotes;
    Spinner typeDropdown;
    ArrayAdapter spinnerAdapter;
    Intent intent;
    SimpleDateFormat formatter;
    Assessment assessment;
    Button share;
    int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new dbHelper(this);
        setContentView(R.layout.assessment_detail_view);

        intent = getIntent();
        courseId = intent.getExtras().getInt("courseId");

        share = findViewById(R.id.btnShareAssessment);

        formatter = new SimpleDateFormat("MM/dd/yyyy");

        txtStartDate = (EditText)findViewById(R.id.txtAssessmentStartDate);
        txtDueDate = (EditText)findViewById(R.id.txtAssessmentDateDue);
        typeDropdown = (Spinner)findViewById(R.id.assessmentTypeDropdown);
        txtNotes = (EditText)findViewById(R.id.txtAssessmentNotes);
        String[] types = getResources().getStringArray(R.array.assessment_type_array);
        spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                types
        );
        typeDropdown.setAdapter(spinnerAdapter);

        if(intent.getExtras().getBoolean("isNew")){
            createNewAssessment();
        }else{
            loadExistingAssessment(intent.getExtras().getInt("assessmentId"));
        }
    }

    public void createNewAssessment(){
        TextView lblTitle = (TextView)findViewById(R.id.lblAssessmentTitle);
        lblTitle.setText("New Assessment");
        assessment = new Assessment();
    }

    public void loadExistingAssessment(int assessmentId){
        assessment = db.getAssessment(assessmentId);
        txtStartDate.setText(formatter.format(assessment.getStartDate()));
        txtDueDate.setText(formatter.format(assessment.getDueDate()));
        txtNotes.setText(assessment.getNotes());
        int spinnerPosition = spinnerAdapter.getPosition(assessment.getType());
        typeDropdown.setSelection(spinnerPosition);
    }

    public void saveAssessment(View view){
        //grab input from the user
        try {
            assessment.setCourseId(courseId);
            assessment.setStartDate(formatter.parse(txtStartDate.getText().toString()));
            assessment.setDueDate(formatter.parse(txtDueDate.getText().toString()));
            assessment.setNotes(txtNotes.getText().toString());
            assessment.setType(typeDropdown.getSelectedItem().toString());
            assessment.setAlert(false); //TODO - CHANGE THIS!!!!!!!!
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //save to the db
        if(intent.getExtras().getBoolean("isNew")){
            db.insertAssessment(assessment);
        }else{
            db.updateAssessment(assessment);
        }

        //return to course list
        goBack();
    }

    public void deleteAssessment(View view){
        db.deleteAssessment(assessment.getId());
        goBack();
    }

    public void share(View view){
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,assessment.toString());
        intent.putExtra(Intent.EXTRA_TEXT,"Hello! This shared assessment starts on " + assessment.getStartDate().toString() + " and is due on " + assessment.getDueDate().toString() + ".");
        startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }

    public void goBack(){
        super.onBackPressed();
    }
}
