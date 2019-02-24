package com.dunbar.termtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class MentorListActivity extends AppCompatActivity {
    dbHelper db;
    int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new dbHelper(this);
        setContentView(R.layout.mentor_list_layout);

        Intent intent = getIntent();
        courseId = intent.getExtras().getInt("courseId");

        initializeMentorList();
    }

    @Override
    public void onResume(){
        super.onResume();
        initializeMentorList();
    }

    public void initializeMentorList(){
        ListView lvMentors = (ListView)findViewById(R.id.lvMentors);
        List<Mentor> mentors = db.getMentors(courseId);

        ArrayAdapter<Mentor> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                mentors
        );

        lvMentors.setAdapter(arrayAdapter);

        lvMentors.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View parentView, int itemPosition, long itemId){
                Mentor mentor = (Mentor)parent.getAdapter().getItem(itemPosition);
                viewMentor(parentView, mentor.getId());
            }
        });
    }

    public void viewMentor(View view, int mentorId){
        Intent intent = new Intent(this, MentorDetailActivity.class);
        intent.putExtra("isNew",false);
        intent.putExtra("courseId",courseId);
        intent.putExtra("mentorId",mentorId);
        startActivity(intent);
    }

    public void addMentor(View view){
        Intent intent = new Intent(this, MentorDetailActivity.class);
        intent.putExtra("isNew",true);
        intent.putExtra("courseId",courseId);
        startActivity(intent);
    }
}
