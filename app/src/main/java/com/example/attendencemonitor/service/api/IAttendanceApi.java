package com.example.attendencemonitor.service.api;

import com.example.attendencemonitor.service.dto.AttendDto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IAttendanceApi
{
    @POST("attendance")
    Call<Void> attend(@Body AttendDto dto);
}
