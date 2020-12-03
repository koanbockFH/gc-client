package com.example.attendencemonitor.service.model;

import com.google.gson.annotations.Expose;

public class TimeslotStatisticModel extends TimeslotModel
{
    @Expose
    private int attended;
    @Expose
    private int absent;
    @Expose
    private int totalStudents;

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
}
