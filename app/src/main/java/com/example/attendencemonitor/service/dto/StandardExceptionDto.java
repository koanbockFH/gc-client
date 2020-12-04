package com.example.attendencemonitor.service.dto;

import com.google.gson.annotations.Expose;

public class StandardExceptionDto
{
    @Expose
    private int statusCode;
    @Expose
    private String message;

    public int getStatusCode()
    {
        return statusCode;
    }

    public String getMessage()
    {
        return message;
    }
}
