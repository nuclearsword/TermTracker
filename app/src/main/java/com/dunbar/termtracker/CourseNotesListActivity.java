package com.dunbar.termtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class CourseNotesListActivity extends AppCompatActivity {
    dbHelper db;
    int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new dbHelper(this);
        setContentView(R.layout.notes_list_layout);

        Intent intent = getIntent();
        courseId = intent.getExtras().getInt("courseId");

        initializeCourseNotesList();
    }

    @Override
    public void onResume(){
        super.onResume();
        initializeCourseNotesList();
    }

    public void initializeCourseNotesList(){
        ListView lvCourseNotes = (ListView)findViewById(R.id.lvCourseNotes);
        List<CourseNotes> courseNotes = db.getCourseNotesByCourseId(courseId);

        ArrayAdapter<CourseNotes> arrayAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                courseNotes
        );

        lvCourseNotes.setAdapter(arrayAdapter);

        lvCourseNotes.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View parentView, int itemPosition, long itemId){
                CourseNotes note = (CourseNotes)parent.getAdapter().getItem(itemPosition);
                viewNote(parentView, note.getId());
            }
        });
    }

    public void viewNote(View view, int noteId){
        Intent intent = new Intent(this, CourseNotesDetailActivity.class);
        intent.putExtra("isNew",false);
        intent.putExtra("courseId",courseId);
        intent.putExtra("noteId",noteId);
        startActivity(intent);
    }

    public void addNote(View view){
        Intent intent = new Intent(this, CourseNotesDetailActivity.class);
        intent.putExtra("isNew",true);
        intent.putExtra("courseId",courseId);
        startActivity(intent);
    }
}
