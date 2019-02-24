package com.dunbar.termtracker;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Term {
    private int id;
    private String title;
    private Date startDate;
    private Date endDate;

    public Term(){
        super();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
        return this.title + " [" + formatter.format(this.startDate) + " - " + formatter.format(this.endDate) + "]";
    }
}
