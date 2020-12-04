package com.example.attendencemonitor.service.contract;

import android.content.Context;

import com.example.attendencemonitor.service.dto.LoginFormDto;
import com.example.attendencemonitor.service.dto.Pagination;
import com.example.attendencemonitor.service.dto.RegisterFormDto;
import com.example.attendencemonitor.service.dto.UserSearchDto;
import com.example.attendencemonitor.service.model.UserModel;

public interface IUserService
{
    void register(RegisterFormDto dto, IActionCallback callback);
    void login(Context context, LoginFormDto dto, IActionCallback callback);
    void getCurrentUser(ICallback<UserModel> callback);
    void logout(Context context, IActionCallback callback);
    void getUserList(UserSearchDto dto, ICallback<Pagination<UserModel>> callback);
}
