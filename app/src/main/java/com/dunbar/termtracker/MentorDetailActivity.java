package com.dunbar.termtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class MentorDetailActivity extends AppCompatActivity {
    dbHelper db;
    EditText txtFullName;
    EditText txtPhone;
    EditText txtEmail;
    Intent intent;
    SimpleDateFormat formatter;
    Mentor mentor;
    int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new dbHelper(this);
        setContentView(R.layout.mentor_detail_view);

        intent = getIntent();
        courseId = intent.getExtras().getInt("courseId");

        formatter = new SimpleDateFormat("MM/dd/yyyy");

        txtFullName = (EditText)findViewById(R.id.txtFullName);
        txtPhone = (EditText)findViewById(R.id.txtPhoneNumber);
        txtEmail = (EditText)findViewById(R.id.txtEmailAddress);

        if(intent.getExtras().getBoolean("isNew")){
            createNewMentor();
        }else{
            loadExistingMentor(intent.getExtras().getInt("mentorId"));
        }
    }

    public void createNewMentor(){
        TextView lblTitle = (TextView)findViewById(R.id.lblMentorTitle);
        lblTitle.setText("New Mentor");
        mentor = new Mentor();
    }

    public void loadExistingMentor(int mentorId){
        mentor = db.getMentor(mentorId);
        txtFullName.setText(mentor.getFullName());
        txtPhone.setText(mentor.getPhone());
        txtEmail.setText(mentor.getEmail());
    }

    public void saveMentor(View view){
        //grab input from the user
        mentor.setCourseId(courseId);
        mentor.setFullName(txtFullName.getText().toString());
        mentor.setPhone(txtPhone.getText().toString());
        mentor.setEmail(txtEmail.getText().toString());

        //save to the db
        if(intent.getExtras().getBoolean("isNew")){
            db.insertMentor(mentor);
        }else{
            db.updateMentor(mentor);
        }

        //return to course list
        goBack();
    }

    public void deleteMentor(View view){
        db.deleteMentor(mentor.getId());
        goBack();
    }

    public void goBack(){
        super.onBackPressed();
    }
}
