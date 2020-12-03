package com.example.attendencemonitor.service.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ModuleStatisticModel extends BaseModel
{
    @Expose
    private int attended;
    @Expose
    private int absent;
    @Expose
    private int totalStudents;
    @Expose
    private int totalTimeslots;
    @Expose
    private List<StudentModuleStatisticModel> students;

    public int getAttended()
    {
        return attended;
    }

    public int getAbsent()
    {
        return absent;
    }

    public int getTotal()
    {
        return totalStudents;
    }

    public int getTimeslots()
    {
        return totalTimeslots;
    }

    public List<StudentModuleStatisticModel> getStudents()
    {
        return students;
    }
}
