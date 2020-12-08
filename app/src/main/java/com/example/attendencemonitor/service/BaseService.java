package com.example.attendencemonitor.service;

/**
 * Provides simple access to api, and forces a api service to define the api in use - and forces Single resposibility pattern by only allowing one specific api
 * @param <T> API type
 */
public abstract class BaseService<T>
{
    protected T api;

    protected BaseService(T api){
        this.api = api;
}

}
