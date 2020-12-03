package com.example.attendencemonitor.service.model;

import com.google.gson.annotations.Expose;

public class ModuleStatisticModelBase extends BaseModel
{
    @Expose
    private int attended;
    @Expose
    private int absent;
    @Expose
    private int totalStudents;
    @Expose
    private int totalTimeslots;

    public int getAttended()
    {
        return attended;
    }

    public int getAbsent()
    {
        return absent;
    }

    public int getTotalStudents()
    {
        return totalStudents;
    }

    public int getTimeslots()
    {
        return totalTimeslots;
    }

}
