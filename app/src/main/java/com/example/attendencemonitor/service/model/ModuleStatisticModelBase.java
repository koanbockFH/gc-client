package com.example.attendencemonitor.service.model;

import com.google.gson.annotations.Expose;

public class ModuleStatisticModelBase extends BaseModel
{
    @Expose
    private String name;
    @Expose
    private String code;
    @Expose
    private String description;
    @Expose
    private UserModel teacher;
    @Expose
    private int attended;
    @Expose
    private int absent;
    @Expose
    private int totalStudents;
    @Expose
    private int totalTimeslots;

    public String getName()
    {
        return name;
    }

    public String getCode()
    {
        return code;
    }

    public String getDescription()
    {
        return description;
    }

    public UserModel getTeacher()
    {
        return teacher;
    }

    public int getTotalTimeslots()
    {
        return totalTimeslots;
    }

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
