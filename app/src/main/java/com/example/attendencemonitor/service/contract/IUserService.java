package com.example.attendencemonitor.service.contract;

import com.example.attendencemonitor.service.dto.LoginFormDto;
import com.example.attendencemonitor.service.dto.RegisterFormDto;
import com.example.attendencemonitor.service.model.UserModel;

public interface IUserService
{
    void register(RegisterFormDto dto, IActionCallback callback);
    void login(LoginFormDto dto, IActionCallback callback);
    void getCurrentUser(ICallback<UserModel> callback);
    void logout(IActionCallback callback);
}
