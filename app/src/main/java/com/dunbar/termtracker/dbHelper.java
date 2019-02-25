package com.dunbar.termtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class dbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "scheduler.db";

    public dbHelper(Context context) {
        super(context, DATABASE_NAME, null, 6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE TERM(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title TEXT, " +
                "startDate DATE, " +
                "endDate DATE" +
                ")");
        db.execSQL("CREATE TABLE COURSE(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "termId INTEGER, " +
                "title TEXT, " +
                "startDate DATE, " +
                "endDate DATE," +
                "status TEXT," +
                "alert INTEGER NOT NULL DEFAULT 0," +
                "FOREIGN KEY(termId) REFERENCES TERM(ID)" +
                ")");
        db.execSQL("CREATE TABLE ASSESSMENT(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "courseId INTEGER, " +
                "startDate date, " +
                "dueDate date, " +
                "type TEXT, " +
                "notes TEXT, " +
                "alert INTEGER NOT NULL DEFAULT 0," +
                "FOREIGN KEY(courseId) REFERENCES COURSE(ID)" +
                ")");
        db.execSQL("CREATE TABLE MENTOR(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "courseId INTEGER, " +
                "fullName TEXT," +
                "phone TEXT," +
                "email TEXT," +
                "FOREIGN KEY(courseId) REFERENCES COURSE(ID)" +
                ")");
        db.execSQL("CREATE TABLE COURSENOTES(" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "courseId INTEGER, " +
                "notes TEXT," +
                "FOREIGN KEY(courseId) REFERENCES COURSE(ID)" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS COURSENOTES");
        db.execSQL("DROP TABLE IF EXISTS MENTOR");
        db.execSQL("DROP TABLE IF EXISTS ASSESSMENT");
        db.execSQL("DROP TABLE IF EXISTS COURSE");
        db.execSQL("DROP TABLE IF EXISTS TERM");
        onCreate(db);
    }

    public Term getTerm(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TERM WHERE id = " + id,null);

        Term termToReturn = new Term();
        termToReturn.setId(id);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            try{
                termToReturn.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                termToReturn.setStartDate((new SimpleDateFormat("MM/dd/yyyy")).parse(cursor.getString(cursor.getColumnIndex("startDate"))));
                termToReturn.setEndDate((new SimpleDateFormat("MM/dd/yyyy")).parse(cursor.getString(cursor.getColumnIndex("endDate"))));
            }catch (ParseException e){
                System.out.println(e.getMessage());
            }
        }
        return termToReturn;
    }
    public ArrayList<Term> getTerms(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM TERM",null);

        ArrayList<Term> termsToReturn = new ArrayList<>();

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                try{
                    Term term = new Term();
                    term.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                    term.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    term.setStartDate(new SimpleDateFormat("MM/dd/yyyy").parse(cursor.getString(cursor.getColumnIndex("startDate"))));
                    term.setEndDate(new SimpleDateFormat("MM/dd/yyyy").parse(cursor.getString(cursor.getColumnIndex("endDate"))));
                    termsToReturn.add(term);
                }catch(ParseException e){
                    System.out.println(e.getMessage());
                }
            }
        }
        return termsToReturn;
    }
    public Boolean insertTerm(Term term){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", term.getTitle());
        values.put("startDate",(new SimpleDateFormat("MM/dd/yyyy")).format(term.getStartDate()));
        values.put("endDate",(new SimpleDateFormat("MM/dd/yyyy")).format(term.getEndDate()));
        long result = db.insert("TERM",null,values);
        if(result != -1){
            return true;
        }else{
            return false;
        }
    }
    public boolean updateTerm(Term term){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",term.getId());
        values.put("title", term.getTitle());
        values.put("startDate",(new SimpleDateFormat("MM/dd/yyyy")).format(term.getStartDate()));
        values.put("endDate",(new SimpleDateFormat("MM/dd/yyyy")).format(term.getEndDate()));
        long result = db.update("TERM",values,"ID = ?",new String[] { Integer.toString(term.getId()) });
        if(result != -1){
            return true;
        }else{
            return false;
        }
    }
    public Integer deleteTerm(int termId){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("TERM","ID = ?", new String[] {Integer.toString(termId)});
    }
    public Course getCourse(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM COURSE WHERE id = " + id,null);

        Course courseToReturn = new Course();
        courseToReturn.setId(id);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            try{
                courseToReturn.setTermId(cursor.getInt(cursor.getColumnIndex("termId")));
                courseToReturn.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                courseToReturn.setStartDate((new SimpleDateFormat("MM/dd/yyyy")).parse(cursor.getString(cursor.getColumnIndex("startDate"))));
                courseToReturn.setEndDate((new SimpleDateFormat("MM/dd/yyyy")).parse(cursor.getString(cursor.getColumnIndex("endDate"))));
                courseToReturn.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                int alertFlag = cursor.getInt(cursor.getColumnIndex("alert"));
                if(alertFlag == 0){
                    courseToReturn.setAlert(false);
                }else{
                    courseToReturn.setAlert(true);
                }
            }catch (ParseException e){
                System.out.println(e.getMessage());
            }
        }
        return courseToReturn;
    }
    public ArrayList<Course> getCourses(int termId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM COURSE WHERE termId = " + termId,null);

        ArrayList<Course> coursesToReturn = new ArrayList<>();

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                try{
                    Course course = new Course();
                    course.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                    course.setTermId(termId);
                    course.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    course.setStartDate(new SimpleDateFormat("MM/dd/yyyy").parse(cursor.getString(cursor.getColumnIndex("startDate"))));
                    course.setEndDate(new SimpleDateFormat("MM/dd/yyyy").parse(cursor.getString(cursor.getColumnIndex("endDate"))));
                    course.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                    int alertFlag = cursor.getInt(cursor.getColumnIndex("alert"));
                    if(alertFlag == 0){
                        course.setAlert(false);
                    }else{
                        course.setAlert(true);
                    }
                    coursesToReturn.add(course);
                }catch(ParseException e){
                    System.out.println(e.getMessage());
                }
            }
        }
        return coursesToReturn;
    }
    public ArrayList<Course> getCourses(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM COURSE",null);

        ArrayList<Course> coursesToReturn = new ArrayList<>();

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                try{
                    Course course = new Course();
                    course.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                    course.setTermId(cursor.getInt(cursor.getColumnIndex("termId")));
                    course.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                    course.setStartDate(new SimpleDateFormat("MM/dd/yyyy").parse(cursor.getString(cursor.getColumnIndex("startDate"))));
                    course.setEndDate(new SimpleDateFormat("MM/dd/yyyy").parse(cursor.getString(cursor.getColumnIndex("endDate"))));
                    course.setStatus(cursor.getString(cursor.getColumnIndex("status")));
                    int alertFlag = cursor.getInt(cursor.getColumnIndex("alert"));
                    if(alertFlag == 0){
                        course.setAlert(false);
                    }else{
                        course.setAlert(true);
                    }
                    coursesToReturn.add(course);
                }catch(ParseException e){
                    System.out.println(e.getMessage());
                }
            }
        }
        return coursesToReturn;
    }
    public Boolean insertCourse(Course course){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("termId",course.getTermId());
        values.put("title",course.getTitle());
        values.put("startDate",(new SimpleDateFormat("MM/dd/yyyy")).format(course.getStartDate()));
        values.put("endDate",(new SimpleDateFormat("MM/dd/yyyy")).format(course.getEndDate()));
        values.put("status",course.getStatus());

        int alertFlag;
        if(course.getAlert()){
            alertFlag = 1;
        }else{
            alertFlag = 0;
        }
        values.put("alert",alertFlag);
        long result = db.insert("COURSE",null,values);
        if(result != -1){
            return true;
        }else{
            return false;
        }
    }
    public Boolean updateCourse(Course course){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",course.getId());
        values.put("termId",course.getTermId());
        values.put("title",course.getTitle());
        values.put("startDate",(new SimpleDateFormat("MM/dd/yyyy")).format(course.getStartDate()));
        values.put("endDate",(new SimpleDateFormat("MM/dd/yyyy")).format(course.getEndDate()));
        values.put("status",course.getStatus());

        int alertFlag;
        if(course.getAlert()){
            alertFlag = 1;
        }else{
            alertFlag = 0;
        }
        values.put("alert",alertFlag);
        long result = db.update("COURSE",values,"ID = ?", new String[] { Integer.toString(course.getId()) } );
        if(result != -1){
            return true;
        }else{
            return false;
        }
    }
    public Integer deleteCourse(int courseId){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("COURSE","ID = ?", new String[] {Integer.toString(courseId)});
    }
    public Assessment getAssessment(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ASSESSMENT WHERE id = " + id,null);

        Assessment assessmentToReturn = new Assessment();
        assessmentToReturn.setId(id);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            try{
                assessmentToReturn.setType(cursor.getString(cursor.getColumnIndex("type")));
                assessmentToReturn.setCourseId(cursor.getInt(cursor.getColumnIndex("courseId")));
                assessmentToReturn.setStartDate((new SimpleDateFormat("MM/dd/yyyy")).parse(cursor.getString(cursor.getColumnIndex("startDate"))));
                assessmentToReturn.setDueDate((new SimpleDateFormat("MM/dd/yyyy")).parse(cursor.getString(cursor.getColumnIndex("dueDate"))));
                assessmentToReturn.setNotes(cursor.getString(cursor.getColumnIndex("notes")));
                int alertFlag = cursor.getInt(cursor.getColumnIndex("alert"));
                if(alertFlag == 0){
                    assessmentToReturn.setAlert(false);
                }else{
                    assessmentToReturn.setAlert(true);
                }
            }catch (ParseException e){
                System.out.println(e.getMessage());
            }
        }
        return assessmentToReturn;
    }
    public ArrayList<Assessment> getAssessments(int courseId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM ASSESSMENT WHERE courseId = " + courseId,null);

        ArrayList<Assessment> assessmentsToReturn = new ArrayList<>();

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                try{
                    Assessment assessment = new Assessment();
                    assessment.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                    assessment.setType(cursor.getString(cursor.getColumnIndex("type")));
                    assessment.setCourseId(cursor.getInt(cursor.getColumnIndex("courseId")));
                    assessment.setStartDate((new SimpleDateFormat("MM/dd/yyyy")).parse(cursor.getString(cursor.getColumnIndex("startDate"))));
                    assessment.setDueDate((new SimpleDateFormat("MM/dd/yyyy")).parse(cursor.getString(cursor.getColumnIndex("dueDate"))));
                    assessment.setNotes(cursor.getString(cursor.getColumnIndex("notes")));
                    int alertFlag = cursor.getInt(cursor.getColumnIndex("alert"));
                    if(alertFlag == 0){
                        assessment.setAlert(false);
                    }else{
                        assessment.setAlert(true);
                    }
                    assessmentsToReturn.add(assessment);
                }catch(ParseException e){
                    System.out.println(e.getMessage());
                }
            }
        }
        return assessmentsToReturn;
    }
    public Boolean insertAssessment(Assessment assessment){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("courseId",assessment.getCourseId());
        values.put("type", assessment.getType());
        values.put("startDate",(new SimpleDateFormat("MM/dd/yyyy")).format(assessment.getStartDate()));
        values.put("dueDate",(new SimpleDateFormat("MM/dd/yyyy")).format(assessment.getDueDate()));
        values.put("notes", assessment.getNotes());
        int alertFlag;
        if(assessment.getAlert()){
            alertFlag = 1;
        }else{
            alertFlag = 0;
        }
        values.put("alert",alertFlag);
        long result = db.insert("ASSESSMENT",null,values);
        if(result != -1){
            return true;
        }else{
            return false;
        }
    }
    public Boolean updateAssessment(Assessment assessment){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",assessment.getId());
        values.put("courseId",assessment.getCourseId());
        values.put("type", assessment.getType());
        values.put("startDate",(new SimpleDateFormat("MM/dd/yyyy")).format(assessment.getStartDate()));
        values.put("dueDate",(new SimpleDateFormat("MM/dd/yyyy")).format(assessment.getDueDate()));
        values.put("notes", assessment.getNotes());
        int alertFlag;
        if(assessment.getAlert()){
            alertFlag = 1;
        }else{
            alertFlag = 0;
        }
        values.put("alert",alertFlag);
        long result = db.update("ASSESSMENT",values,"ID = ?",new String[] { Integer.toString(assessment.getId()) });
        if(result != -1){
            return true;
        }else{
            return false;
        }
    }
    public Integer deleteAssessment(int assessmentId){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("ASSESSMENT","ID = ?", new String[] {Integer.toString(assessmentId)});
    }
    public Mentor getMentor(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MENTOR WHERE id = " + id,null);

        Mentor mentorToReturn = new Mentor();
        mentorToReturn.setId(id);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            mentorToReturn.setCourseId(cursor.getInt(cursor.getColumnIndex("courseId")));
            mentorToReturn.setFullName(cursor.getString(cursor.getColumnIndex("fullName")));
            mentorToReturn.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            mentorToReturn.setEmail(cursor.getString(cursor.getColumnIndex("email")));
        }
        return mentorToReturn;
    }
    public ArrayList<Mentor> getMentors(int courseId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM MENTOR WHERE courseId = " + courseId,null);

        ArrayList<Mentor> mentorsToReturn = new ArrayList<>();

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                Mentor mentor = new Mentor();
                mentor.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                mentor.setCourseId(cursor.getInt(cursor.getColumnIndex("courseId")));
                mentor.setFullName(cursor.getString(cursor.getColumnIndex("fullName")));
                mentor.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                mentor.setEmail(cursor.getString(cursor.getColumnIndex("email")));
                mentorsToReturn.add(mentor);
            }
        }
        return mentorsToReturn;
    }
    public Boolean insertMentor(Mentor mentor){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("courseId",mentor.getCourseId());
        values.put("fullName",mentor.getFullName());
        values.put("phone", mentor.getPhone());
        values.put("email",mentor.getEmail());
        long result = db.insert("MENTOR",null,values);
        if(result != -1){
            return true;
        }else{
            return false;
        }
    }
    public Boolean updateMentor(Mentor mentor){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",mentor.getId());
        values.put("courseId",mentor.getCourseId());
        values.put("fullName",mentor.getFullName());
        values.put("phone", mentor.getPhone());
        values.put("email",mentor.getEmail());
        long result = db.update("MENTOR",values,"ID = ?",new String[]{ Integer.toString(mentor.getId()) });
        if(result != -1){
            return true;
        }else{
            return false;
        }
    }
    public Integer deleteMentor(int mentorId){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("MENTOR","ID = ?", new String[] {Integer.toString(mentorId)});
    }
    public CourseNotes getCourseNotes(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM COURSENOTES WHERE id = " + id,null);

        CourseNotes courseNotesToReturn = new CourseNotes();
        courseNotesToReturn.setId(id);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            courseNotesToReturn.setCourseId(cursor.getInt(cursor.getColumnIndex("courseId")));
            courseNotesToReturn.setNotes(cursor.getString(cursor.getColumnIndex("notes")));
        }
        return courseNotesToReturn;
    }
    public ArrayList<CourseNotes> getCourseNotesByCourseId(int courseId){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM COURSENOTES WHERE courseId = " + courseId,null);

        ArrayList<CourseNotes> notesToReturn = new ArrayList<>();

        if(cursor.getCount() > 0){
            while(cursor.moveToNext()){
                CourseNotes note = new CourseNotes();
                note.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                note.setCourseId(cursor.getInt(cursor.getColumnIndex("courseId")));
                note.setNotes(cursor.getString(cursor.getColumnIndex("notes")));
                notesToReturn.add(note);
            }
        }
        return notesToReturn;
    }
    public Boolean insertCourseNotes(CourseNotes courseNotes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("courseId",courseNotes.getCourseId());
        values.put("notes",courseNotes.getNotes());
        long result = db.insert("COURSENOTES",null,values);
        if(result != -1){
            return true;
        }else{
            return false;
        }
    }
    public boolean updateCourseNotes(CourseNotes courseNotes){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",courseNotes.getId());
        values.put("courseId",courseNotes.getCourseId());
        values.put("notes",courseNotes.getNotes());
        long result = db.update("COURSENOTES",values,"ID = ?",new String[]{ Integer.toString(courseNotes.getId()) });
        if(result != -1){
            return true;
        }else{
            return false;
        }
    }
    public Integer deleteCourseNotes(int courseNotesId){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("COURSENOTES","ID = ?", new String[] {Integer.toString(courseNotesId)});
    }
}
