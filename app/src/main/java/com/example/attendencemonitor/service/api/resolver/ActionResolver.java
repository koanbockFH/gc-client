package com.example.attendencemonitor.service.api.resolver;

import com.example.attendencemonitor.service.contract.IActionCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class ActionResolver implements Callback<Void>
{
    private final IActionCallback callback;

    public ActionResolver(IActionCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void onResponse(Call<Void> call, Response<Void> response)
    {
        if(!response.isSuccessful())
        {
            callback.onError(new HttpException(response));
        }
        else{
            callback.onSuccess();
        }
    }

    @Override
    public void onFailure(Call<Void> call, Throwable t)
    {
        callback.onError(t);
    }
}
