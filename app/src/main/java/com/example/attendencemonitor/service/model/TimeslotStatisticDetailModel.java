package com.example.attendencemonitor.service.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class TimeslotStatisticDetailModel extends TimeslotStatisticModel
{
    @Expose
    private List<UserModel> attendees;
    @Expose
    private List<UserModel> absentees;

    public List<UserModel> getAttendees()
    {
        return attendees;
    }

    public List<UserModel> getAbsentees()
    {
        return absentees;
    }
}
