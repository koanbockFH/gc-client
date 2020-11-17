package com.example.attendencemonitor.service.model;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class TimeslotModel extends BaseModel
{
    @Expose
    private String name;
    @Expose
    private Date startDate;
    @Expose
    private Date endDate;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public Date getStartDate()
    {
        return startDate;
    }

    public void setStartDate(Date startDate)
    {
        this.startDate = startDate;
    }

    public Date getEndDate()
    {
        return endDate;
    }

    public void setEndDate(Date endDate)
    {
        this.endDate = endDate;
    }

}
