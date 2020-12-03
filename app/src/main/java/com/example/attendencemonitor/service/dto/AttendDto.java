package com.example.attendencemonitor.service.dto;

import com.google.gson.annotations.Expose;

public class AttendDto
{
    @Expose
    String studentCode;
    @Expose
    int timeslotId;

    public String getStudentCode()
    {
        return studentCode;
    }

    public void setStudentCode(String studentCode)
    {
        this.studentCode = studentCode;
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
