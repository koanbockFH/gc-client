package com.example.attendencemonitor.service.model;

import com.google.gson.annotations.Expose;

public abstract class BaseModel
{
    @Expose
    protected int id;

    public int getId()
    {
        return id;
    }
}
