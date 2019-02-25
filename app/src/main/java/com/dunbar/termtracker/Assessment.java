package com.dunbar.termtracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Assessment {
    private int id;
    private int courseId;
    private Date startDate;
    private Date dueDate;
    private String type;
    private String notes;
    private Boolean alert;

    public Assessment(){
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getAlert() {
        return alert;
    }

    public void setAlert(Boolean alert) {
        this.alert = alert;
    }

    @Override
    public String toString(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return this.type + " [" + formatter.format(this.dueDate) + "]";
    }
}
