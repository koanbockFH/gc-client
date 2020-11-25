package com.example.attendencemonitor.service.api;

import com.example.attendencemonitor.service.model.TimeslotModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ITimeslotApi
{
    @DELETE("module/timeslot/{id}")
    Call<Void> delete(@Path("id") int id);

    @POST("module/{moduleId}/timeslot")
    Call<TimeslotModel> create(@Path("moduleId") int moduleId, @Body TimeslotModel model);

    @PUT("module/{moduleId}/timeslot")
    Call<TimeslotModel> update(@Path("moduleId") int moduleId, @Body TimeslotModel model);

    @GET("module/timeslot/{id}")
    Call<TimeslotModel> getById(@Path("id") int id);

    @GET("module/{moduleId}/timeslot")
    Call<TimeslotModel[]> getAll(@Path("moduleId") int moduleId);
}
