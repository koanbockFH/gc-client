package com.example.attendencemonitor.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.attendencemonitor.service.api.ApiAccess;
import com.example.attendencemonitor.service.contract.IActionCallback;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.model.UserModel;

public class AppData
{
    IUserService userService = new UserService();
    public static final String KEY_USER_TOKEN = "user_token";
    private static final String PREF_FILE_NAME = "MonitorPrefs";
    private static AppData instance;
    private UserModel user;

    private AppData()
    {

    }

    public static AppData getInstance()
    {
        if (instance == null)
        {
            synchronized (AppData.class)
            {
                if (instance == null)
                {
                    instance = new AppData();
                }
            }
        }
        return instance;
    }

    public UserModel getUser()
    {
        return user;
    }


    public void createSession(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_FILE_NAME, 0).edit();
        editor.putString(KEY_USER_TOKEN, ApiAccess.getInstance().getAccessToken());
        editor.apply();
    }

    public void closeSession(Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_FILE_NAME, 0).edit();
        editor.putString(KEY_USER_TOKEN, null);
        editor.apply();
    }

    public boolean getSession(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, 0);
        ApiAccess.getInstance().setAccessToken(pref.getString(KEY_USER_TOKEN, null));

        userService.getCurrentUser(new GetUserCallback());
        return ApiAccess.getInstance().getAccessToken() != null;
    }

    private class GetUserCallback implements ICallback<UserModel>
    {
        @Override
        public void onSuccess(UserModel value)
        {
            user = value;
        }

        @Override
        public void onError(Throwable error)
        {
            user = null;
        }
    }
}
