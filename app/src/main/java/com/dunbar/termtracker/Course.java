package com.dunbar.termtracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Course {
    private int id;
    private int termId;
    private String title;
    private Date startDate;
    private Date endDate;
    private String status;
    private Boolean alert;

    public Course(){
        super();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTermId() {
        return termId;
    }

    public void setTermId(int termId) {
        this.termId = termId;
    }

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
        return this.title + ": " + formatter.format(this.startDate) + " - " + formatter.format(this.endDate) + " [" + this.status + "]";
    }
}
