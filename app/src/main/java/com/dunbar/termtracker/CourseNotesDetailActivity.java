package com.dunbar.termtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class CourseNotesDetailActivity extends AppCompatActivity {
    dbHelper db;
    EditText txtNote;
    CourseNotes note;
    Intent intent;
    SimpleDateFormat formatter;
    int courseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new dbHelper(this);
        setContentView(R.layout.notes_detail_view);

        intent = getIntent();
        courseId = intent.getExtras().getInt("courseId");


        txtNote = (EditText)findViewById(R.id.txtNotes);

        if(intent.getExtras().getBoolean("isNew")){
            createNewNote();
        }else{
            loadExistingNote(intent.getExtras().getInt("noteId"));
        }
    }

    public void createNewNote(){
        note = new CourseNotes();
    }

    public void loadExistingNote(int noteId){
        note = db.getCourseNotes(noteId);
        txtNote.setText(note.getNotes());
    }

    public void saveNote(View view){
        //grab input from the user
            note.setCourseId(courseId);
            note.setNotes(txtNote.getText().toString());

        //save to the db
        if(intent.getExtras().getBoolean("isNew")){
            db.insertCourseNotes(note);
        }else{
            db.updateCourseNotes(note);
        }

        //return to course list
        goBack();
    }

    public void deleteNote(View view){
        //show error if assessment has not been saved
        if(note.getId() == 0){
            Toast.makeText(getApplicationContext(),"You cannot delete a note that has not been saved.",Toast.LENGTH_LONG).show();
            return;
        }
        db.deleteCourseNotes(note.getId());
        goBack();
    }

    public void shareCourseNotes(View view){
        //show error if assessment has not been saved
        if(note.getId() == 0){
            Toast.makeText(getApplicationContext(),"You cannot share a note that has not been saved.",Toast.LENGTH_LONG).show();
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,note.toString());
        intent.putExtra(Intent.EXTRA_TEXT,note.getNotes());
        startActivity(Intent.createChooser(intent, "Choose sharing method"));
    }

    public void goBack(){
        super.onBackPressed();
    }
}
