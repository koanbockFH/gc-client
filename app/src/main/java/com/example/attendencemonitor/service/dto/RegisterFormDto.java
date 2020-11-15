package com.example.attendencemonitor.service.dto;

import com.example.attendencemonitor.service.model.UserModel;
import com.google.gson.annotations.Expose;

public class RegisterFormDto extends UserModel
{
    @Expose
    private String password;

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
