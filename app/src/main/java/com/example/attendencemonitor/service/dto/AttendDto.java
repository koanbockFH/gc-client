package com.example.attendencemonitor.service.dto;

import com.google.gson.annotations.Expose;

public class AttendDto
{
    @Expose
    int studentId;
    @Expose
    int timeslotId;

    public int getStudentId()
    {
        return studentId;
    }

    public void setStudentId(int studentId)
    {
        this.studentId = studentId;
    }

    public int getTimeslotId()
    {
        return timeslotId;
    }

    public void setTimeslotId(int timeslotId)
    {
        this.timeslotId = timeslotId;
    }
}
