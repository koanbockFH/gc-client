package com.example.attendencemonitor.service.api;

import com.example.attendencemonitor.service.dto.AuthResponseDto;
import com.example.attendencemonitor.service.dto.LoginFormDto;
import com.example.attendencemonitor.service.dto.RegisterFormDto;
import com.example.attendencemonitor.service.model.UserModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface IUserApi
{
    @POST("auth/register")
    Call<Void> register(@Body RegisterFormDto dto);

    @POST("auth/login")
    Call<AuthResponseDto> login(@Body LoginFormDto dto);

    @GET("auth/profile")
    Call<UserModel> getCurrentUser();

    @GET("auth/logout")
    Call<Void> logout();
}
