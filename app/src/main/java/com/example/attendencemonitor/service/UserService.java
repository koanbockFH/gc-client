package com.example.attendencemonitor.service;

import android.content.Context;

import com.example.attendencemonitor.service.api.resolver.ActionResolver;
import com.example.attendencemonitor.service.api.resolver.BaseResolver;
import com.example.attendencemonitor.service.api.resolver.ResultResolver;
import com.example.attendencemonitor.service.api.ApiAccess;
import com.example.attendencemonitor.service.api.IUserApi;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.dto.AuthResponseDto;
import com.example.attendencemonitor.service.dto.LoginFormDto;
import com.example.attendencemonitor.service.dto.Pagination;
import com.example.attendencemonitor.service.dto.RegisterFormDto;
import com.example.attendencemonitor.service.dto.StandardExceptionDto;
import com.example.attendencemonitor.service.dto.UserSearchDto;
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
    public void login(Context context, LoginFormDto dto, IActionCallback callback)
    {
        api.login(dto).enqueue(new LoginResolver(callback, context));
    }

    @Override
    public void getCurrentUser(ICallback<UserModel> callback)
    {
        api.getCurrentUser().enqueue(new ResultResolver<>(callback));
    }

    @Override
    public void logout(Context context, IActionCallback callback)
    {
        api.logout().enqueue(new LogoutResolver(callback, context));
    }

    @Override
    public void getUserList(UserSearchDto dto, ICallback<Pagination<UserModel>> callback)
    {
        api.getUserList(dto.getPage(), dto.getTake(), dto.getSearch(), dto.getType()).enqueue(new ResultResolver<>(callback));
    }

    //Custom Resolver for Login
    private class LoginResolver extends BaseResolver<AuthResponseDto> implements Callback<AuthResponseDto>
    {
        private final IActionCallback callback;
        private final Context context;

        private LoginResolver(IActionCallback callback, Context context)
        {
            this.callback = callback;
            this.context = context;
        }

        @Override
        public void onResponse(Call<AuthResponseDto> call, Response<AuthResponseDto> response)
        {
            if(!response.isSuccessful())
            {
                callback.onError(handleHttpException(response));
            }
            else{
                AuthResponseDto dto = response.body();
                if(dto == null)
                {
                    callback.onError(new IllegalArgumentException("Response was empty, but Call was successful!"));
                    return;
                }
                ApiAccess.getInstance().setAccessToken(dto.getAccessToken());
                getCurrentUser(new GetUserCallback());
            }
        }

        @Override
        public void onFailure(Call<AuthResponseDto> call, Throwable t)
        {
            callback.onError(t);
        }

        private class GetUserCallback implements ICallback<UserModel>
        {
            @Override
            public void onSuccess(UserModel value)
            {
                AppData.getInstance().createSession(context, value);
                callback.onSuccess();
            }

            @Override
            public void onError(Throwable error)
            {
                ApiAccess.getInstance().setAccessToken(null);
                callback.onError(error);
            }
        }
    }

    //Custom Resolver for Logout
    private static class LogoutResolver extends BaseResolver<Void> implements  Callback<Void>
    {
        private final IActionCallback callback;
        private final Context context;

        private LogoutResolver(IActionCallback callback, Context context)
        {
            this.callback = callback;
            this.context = context;
        }

        @Override
        public void onResponse(Call<Void> call, Response<Void> response)
        {
            if(response.isSuccessful())
            {
                AppData.getInstance().closeSession(context);
                ApiAccess.getInstance().setAccessToken(null);
                callback.onSuccess();
            }
            else{
                callback.onError(handleHttpException(response));
            }
        }

        @Override
        public void onFailure(Call<Void> call, Throwable t)
        {
            callback.onError(t);
        }
    }
}
