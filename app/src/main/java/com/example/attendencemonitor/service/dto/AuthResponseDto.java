package com.example.attendencemonitor.service.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AuthResponseDto
{
    @Expose
    @SerializedName("access_token")
    private String accessToken;

    public String getAccessToken()
    {
        return accessToken;
    }
}
