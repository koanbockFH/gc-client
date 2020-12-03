package com.example.attendencemonitor.service.model;

import com.google.gson.annotations.Expose;

public class StudentModuleStatisticModel extends UserModel
{
    @Expose
    private int attended;
    @Expose
    private int absent;
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

    public int getTotalTimeslots()
    {
        return totalTimeslots;
    }
}
