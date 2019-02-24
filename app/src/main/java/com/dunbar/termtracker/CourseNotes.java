package com.dunbar.termtracker;

public class CourseNotes {
    private int id;
    private int courseId;
    private String notes;

    public CourseNotes(){
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString(){
        int length = this.notes.length();
        if(length < 30){
            return this.notes.substring(0,length);
        }else{
            return this.notes.substring(0,30);
        }
    }
}
