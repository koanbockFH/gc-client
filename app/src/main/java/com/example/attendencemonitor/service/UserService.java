package com.example.attendencemonitor.service;

import com.example.attendencemonitor.service.api.resolver.ActionResolver;
import com.example.attendencemonitor.service.api.resolver.ResultResolver;
import com.example.attendencemonitor.service.api.ApiAccess;
import com.example.attendencemonitor.service.api.IUserApi;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.dto.AuthResponseDto;
import com.example.attendencemonitor.service.dto.LoginFormDto;
import com.example.attendencemonitor.service.dto.RegisterFormDto;
import com.example.attendencemonitor.service.model.UserModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.HttpException;
import retrofit2.Response;

public class UserService extends BaseService<IUserApi> implements IUserService
{
    public UserService()
    {
        super(ApiAccess.getInstance().getRetrofit().create(IUserApi.class));
    }

    @Override
    public void register(RegisterFormDto dto, IActionCallback callback)
    {
        api.register(dto).enqueue(new ActionResolver(callback));
    }

    @Override
    public void login(LoginFormDto dto, IActionCallback callback)
    {
        api.login(dto).enqueue(new AuthenticationResolver(callback));
    }

    @Override
    public void getCurrentUser(ICallback<UserModel> callback)
    {
        api.getCurrentUser().enqueue(new ResultResolver<>(callback));
    }

    @Override
    public void logout(IActionCallback callback)
    {
        ApiAccess.getInstance().setAccessToken(null);
        //TODO Backend does not yet have a logout function we "fix" it by deleting the active token in the frontend
    }

    //Custom Handler for Login
    private static class AuthenticationResolver implements Callback<AuthResponseDto>
    {
        private final IActionCallback callback;

        private AuthenticationResolver(IActionCallback callback)
        {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<AuthResponseDto> call, Response<AuthResponseDto> response)
        {
            if(!response.isSuccessful())
            {
                callback.onError(new HttpException(response));
            }
            else{
                AuthResponseDto dto = response.body();
                if(dto == null)
                {
                    callback.onError(new IllegalArgumentException("Response was empty, but Call was successful!"));
                    return;
                }
                ApiAccess.getInstance().setAccessToken(dto.getAccessToken());
                callback.onSuccess();
            }
        }

        @Override
        public void onFailure(Call<AuthResponseDto> call, Throwable t)
        {
            callback.onError(t);
        }
    }
}
