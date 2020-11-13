package com.example.attendencemonitor.service;

public abstract class BaseService<T>
{
    protected T api;

    protected BaseService(T api){
        this.api = api;
}

}
