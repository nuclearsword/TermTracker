package com.dunbar.termtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class AssessmentListActivity extends AppCompatActivity {
    dbHelper db;
    int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new dbHelper(this);
        setContentView(R.layout.assessment_list_layout);

        Intent intent = getIntent();
        courseId = intent.getExtras().getInt("courseId");

        initializeAssessmentList();
    }

    @Override
    public void onResume(){
        super.onResume();
        initializeAssessmentList();
    }

    public void initializeAssessmentList(){
        ListView lvAssessments = (ListView)findViewById(R.id.lvAssessments);
        List<Assessment> assessments = db.getAssessments(courseId);

        ArrayAdapter<Assessment> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                assessments
        );

        lvAssessments.setAdapter(arrayAdapter);

        lvAssessments.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View parentView, int itemPosition, long itemId){
                Assessment assessment = (Assessment) parent.getAdapter().getItem(itemPosition);
                viewAssessment(parentView, assessment.getId());
            }
        });
    }

    public void viewAssessment(View view, int assessmentId){
        Intent intent = new Intent(this, AssessmentDetailActivity.class);
        intent.putExtra("isNew",false);
        intent.putExtra("courseId",courseId);
        intent.putExtra("assessmentId",assessmentId);
        startActivity(intent);
    }

    public void addAssessment(View view){
        //make sure there aren't more than 5 assessments already associated with this course
        List<Assessment> assessments = db.getAssessments(courseId);
        if(assessments.size() >= 5){
            Toast.makeText(getApplicationContext(),"Sorry, you can only add 5 assessments to a course.",Toast.LENGTH_LONG).show();
           return;
        }

        Intent intent = new Intent(this, AssessmentDetailActivity.class);
        intent.putExtra("isNew",true);
        intent.putExtra("courseId",courseId);
        startActivity(intent);
    }
}
