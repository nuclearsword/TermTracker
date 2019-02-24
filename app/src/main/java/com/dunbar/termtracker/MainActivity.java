package com.dunbar.termtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    dbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new dbHelper(this);
        setContentView(R.layout.activity_main);
        initializeReport();
        initializeNavigation();
    }

    @Override
    public void onResume(){
        super.onResume();
        initializeReport();
    }

    public void initializeReport(){
        //get the labels
        TextView lblTermsInProgress = (TextView)findViewById(R.id.txtTermsInProgress);
        TextView lblCompletedTerms = (TextView)findViewById(R.id.txtCompletedTerms);
        TextView lblTotalTerms = (TextView)findViewById(R.id.txtTotalTerms);
        TextView lblTermCompletionProgress = (TextView)findViewById(R.id.txtTermProgressPercentage);
        TextView lblCoursesInProgress = (TextView)findViewById(R.id.txtCoursesInProgress);
        TextView lblCompletedCourses = (TextView)findViewById(R.id.txtCompletedCourses);
        TextView lblTotalCourses = (TextView)findViewById(R.id.txtTotalCourses);
        TextView lblCourseCompletionProgress = (TextView)findViewById(R.id.txtCourseProgressPercentage);

        //get the values for Term
        Iterator<Term> iTerm;

        ArrayList<Term> termsInProgress = db.getTerms();
        iTerm = termsInProgress.iterator();
        while(iTerm.hasNext()){
            Term t = iTerm.next();
            if(t.getEndDate().before(new Date())){
                iTerm.remove();
            }
        }
        int termsInProgressNum = termsInProgress.size();

        int totalTermsNum = db.getTerms().size();

        List<Term> completedTerms = db.getTerms();
        iTerm = completedTerms.iterator();
        while(iTerm.hasNext()){
            Term t = iTerm.next();
            if(t.getEndDate().after(new Date())){
                iTerm.remove();
            }
        }
        int completedTermsNum = completedTerms.size();

        double termCompletionPercentage = Math.round((double)completedTermsNum / (double)totalTermsNum * 100);

        //get the values for course
        Iterator<Course> iCourse;

        List<Course> coursesInProgress = db.getCourses();
        iCourse = coursesInProgress.iterator();
        while(iCourse.hasNext()){
            Course c = iCourse.next();
            if(!c.getStatus().equals("In Progress")){
                iCourse.remove();
            }
        }
        int coursesInProgressNum = coursesInProgress.size();

        int totalCoursesNum = db.getCourses().size();

        List<Course> completedCourses = db.getCourses();
        iCourse = completedCourses.iterator();
        while(iCourse.hasNext()){
            Course c = iCourse.next();
            if(!c.getStatus().equals("Completed")){
                iCourse.remove();
            }
        }
        int completedCoursesNum = completedCourses.size();

        double courseCompletionPercentage = Math.round((double)completedCoursesNum / (double)totalCoursesNum * 100);

        //populate the labels
        lblTermsInProgress.setText(""+termsInProgressNum);
        lblCompletedTerms.setText(""+completedTermsNum);
        lblTotalTerms.setText(""+totalTermsNum);
        lblTermCompletionProgress.setText(""+termCompletionPercentage+"%");
        lblCoursesInProgress.setText(""+coursesInProgressNum);
        lblCompletedCourses.setText(""+completedCoursesNum);
        lblTotalCourses.setText(""+totalCoursesNum);
        lblCourseCompletionProgress.setText(""+courseCompletionPercentage+"%");
    }

    //I only created this event listener because it's a rubric requirement. I could have assigned it in the layout designer.
    public void initializeNavigation(){
        Button viewTermsButton = (Button)findViewById(R.id.btnViewTerms);
        viewTermsButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                viewTerms();
            }
        });
    }

    public void viewTerms(){
        Intent intent = new Intent(this, TermListActivity.class);
        startActivity(intent);
    }
}
