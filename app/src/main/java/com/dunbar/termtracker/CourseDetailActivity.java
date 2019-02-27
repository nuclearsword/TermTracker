package com.dunbar.termtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CourseDetailActivity extends AppCompatActivity {
    dbHelper db;
    EditText txtTitle;
    EditText txtStartDate;
    EditText txtEndDate;
    Spinner courseStatusDropdown;
    Course course;
    Intent intent;
    SimpleDateFormat formatter;
    ArrayAdapter spinnerAdapter;
    int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new dbHelper(this);
        setContentView(R.layout.course_detail_view);

        intent = getIntent();
        termId = intent.getExtras().getInt("termId");

        formatter = new SimpleDateFormat("MM/dd/yyyy");

        txtTitle = (EditText)findViewById(R.id.txtCourseTitle);
        txtStartDate = (EditText)findViewById(R.id.txtCourseStartDate);
        txtEndDate = (EditText)findViewById(R.id.txtCourseEndDate);
        courseStatusDropdown = (Spinner)findViewById(R.id.courseStatusDropdown);
        String[] statuses = getResources().getStringArray(R.array.status_array);
        spinnerAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                statuses
                );
        courseStatusDropdown.setAdapter(spinnerAdapter);

        if(intent.getExtras().getBoolean("isNew")){
            createNewCourse();
        }else{
            loadExistingCourse(intent.getExtras().getInt("courseId"));
        }
    }

    public void createNewCourse(){
        TextView lblTitle = (TextView)findViewById(R.id.lblCourseTitle);
        lblTitle.setText("New Course");
        course = new Course();
    }

    public void loadExistingCourse(int courseId){
        course = db.getCourse(courseId);
        txtTitle.setText(course.getTitle());
        txtStartDate.setText(formatter.format(course.getStartDate()));
        txtEndDate.setText(formatter.format(course.getEndDate()));
        int spinnerPosition = spinnerAdapter.getPosition(course.getStatus());
        courseStatusDropdown.setSelection(spinnerPosition);
    }

    public void saveCourse(View view){
        //grab input from the user
        try {
            course.setTermId(termId);
            course.setTitle(txtTitle.getText().toString());
            course.setStartDate(formatter.parse(txtStartDate.getText().toString()));
            course.setEndDate(formatter.parse(txtEndDate.getText().toString()));
            course.setStatus(courseStatusDropdown.getSelectedItem().toString());
            course.setAlert(false); //TODO - CHANGE THIS!!!!!!!!
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //save to the db
        if(intent.getExtras().getBoolean("isNew")){
            db.insertCourse(course);
        }else{
            db.updateCourse(course);
        }

        //return to course list
        goBack();
    }

    public void deleteCourse(View view){
        db.deleteCourse(course.getId());
        goBack();
    }

    public void viewMentors(View view){
        Intent intent = new Intent(this, MentorListActivity.class);
        intent.putExtra("courseId",course.getId());
        startActivity(intent);
    }
    public void viewAssessments(View view){
        Intent intent = new Intent(this, AssessmentListActivity.class);
        intent.putExtra("courseId",course.getId());
        startActivity(intent);
    }
    public void viewCourseNotes(View view){
        Intent intent = new Intent(this, CourseNotesListActivity.class);
        intent.putExtra("courseId",course.getId());
        startActivity(intent);
    }

    public void shareCourse(View view){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,course.toString());
        intent.putExtra(Intent.EXTRA_TEXT,"Hello! The " + course.getTitle() +" course starts on " + formatter.format(course.getStartDate()) + " and ends on " + formatter.format(course.getEndDate()) + ".");
        startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }

    public void goBack(){
        super.onBackPressed();
    }
}
