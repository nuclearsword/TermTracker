package com.dunbar.termtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class CourseListActivity extends AppCompatActivity {
    dbHelper db;
    int termId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new dbHelper(this);
        setContentView(R.layout.course_list_layout);

        Intent intent = getIntent();
        termId = intent.getExtras().getInt("termId");

        initializeCourseList();
    }

    @Override
    public void onResume(){
        super.onResume();
        initializeCourseList();
    }

    public void initializeCourseList(){
        ListView lvCourses = (ListView)findViewById(R.id.lvCourses);
        List<Course> courses = db.getCourses(termId);

        ArrayAdapter<Course> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                courses
        );

        lvCourses.setAdapter(arrayAdapter);

        lvCourses.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View parentView, int itemPosition, long itemId){
                Course course = (Course)parent.getAdapter().getItem(itemPosition);
                viewCourse(parentView, course.getId());
            }
        });
    }

    public void viewCourse(View view, int courseId){
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra("isNew",false);
        intent.putExtra("termId",termId);
        intent.putExtra("courseId",courseId);
        startActivity(intent);
    }

    public void addCourse(View view){
        Intent intent = new Intent(this, CourseDetailActivity.class);
        intent.putExtra("isNew",true);
        intent.putExtra("termId",termId);
        startActivity(intent);
    }
}
