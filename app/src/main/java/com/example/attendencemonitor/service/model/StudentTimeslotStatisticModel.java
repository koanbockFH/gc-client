package com.example.attendencemonitor.service.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class StudentTimeslotStatisticModel
{
    @Expose
    private List<TimeslotModel> attended;
    @Expose
    private List<TimeslotModel> absent;

    public List<TimeslotModel> getAttended()
    {
        return attended;
    }

    public List<TimeslotModel> getAbsent()
    {
        return absent;
    }
}
