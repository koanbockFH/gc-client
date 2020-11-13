package com.example.attendencemonitor.service.contract;

public interface IActionCallback
{
    void onSuccess();

    void onError(Throwable error);
}
