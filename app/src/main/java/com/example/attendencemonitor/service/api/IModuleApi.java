package com.example.attendencemonitor.service.api;

import com.example.attendencemonitor.service.model.ModuleModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IModuleApi
{
    @POST("module/")
    Call<ModuleModel> create(@Body ModuleModel model);

    @PUT("module")
    Call<ModuleModel> update(@Body ModuleModel model);

    @DELETE("module/{id}")
    Call<Void> delete(@Path("id") int id);

    @GET("module")
    Call<ModuleModel[]> getAll();

    @GET("module/{id}")
    Call<ModuleModel> getById(@Path("id") int id);
}
