package com.example.attendencemonitor.service.contract;

public interface ICallback<T>
{
    void onSuccess(T value);

    void onError(Throwable error);
}
