package com.example.attendencemonitor.service.api.resolver;

import com.example.attendencemonitor.service.contract.ICallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class ResultResolver<T> extends BaseResolver<T> implements Callback<T>
{
    private final ICallback<T> callback;

    public ResultResolver(ICallback<T> callback)
    {
        this.callback = callback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response)
    {
        if(!response.isSuccessful())
        {
            callback.onError(handleHttpException(response));
        }
        else{
            callback.onSuccess(response.body());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t)
    {
        callback.onError(t);
    }
}
