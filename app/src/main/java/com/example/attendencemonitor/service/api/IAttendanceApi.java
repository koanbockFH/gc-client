package com.example.attendencemonitor.service.api;

import com.example.attendencemonitor.service.dto.AttendDto;
import com.example.attendencemonitor.service.model.ModuleStatisticModel;
import com.example.attendencemonitor.service.model.ModuleStatisticModelBase;
import com.example.attendencemonitor.service.model.StudentTimeslotStatisticModel;
import com.example.attendencemonitor.service.model.TimeslotStatisticModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface IAttendanceApi
{
    @POST("attendance")
    Call<Void> attend(@Body AttendDto dto);

    @GET("statistics/module/{id}")
    Call<ModuleStatisticModel> getModuleStats(@Path("id") int id);

    @GET("statistics/module/{id}/timeslot")
    Call<List<TimeslotStatisticModel>> getAllTimeslotStats(@Path("id") int moduleId);

    @GET("statistics/module/{moduleId}/student/{studentId}")
    Call<StudentTimeslotStatisticModel> getStudentTimeslotStats(@Path("moduleId") int moduleId, @Path("studentId") int studentId);

    @GET("statistics/module/student/{studentId}")
    Call<List<ModuleStatisticModelBase>> getStudentModuleStats(@Path("studentId") int studentId);
}
