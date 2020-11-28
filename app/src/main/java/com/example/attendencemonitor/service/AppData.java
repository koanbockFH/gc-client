package com.example.attendencemonitor.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.example.attendencemonitor.service.api.ApiAccess;
import com.example.attendencemonitor.service.contract.ICallback;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.model.UserModel;
import com.example.attendencemonitor.service.model.UserType;

public class AppData
{
    IUserService userService = new UserService();
    public static final String KEY_USER_TOKEN = "user_token";
    public static final String KEY_USER_TYPE = "user_type";
    private static final String PREF_FILE_NAME = "MonitorPrefs";
    private static AppData instance;
    private UserType userType;

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

    public UserType getUserType()
    {
        return userType;
    }


    public void createSession(Context context, UserModel user) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_FILE_NAME, 0).edit();
        editor.putString(KEY_USER_TOKEN, ApiAccess.getInstance().getAccessToken());
        editor.putInt(KEY_USER_TYPE, user.getUserType().getKey());
        editor.apply();
        this.userType = user.getUserType();
    }

    public void closeSession(Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_FILE_NAME, 0).edit();
        editor.putString(KEY_USER_TOKEN, null);
        editor.putInt(KEY_USER_TYPE, -1);
        editor.apply();
    }

    public boolean getSession(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, 0);
        ApiAccess.getInstance().setAccessToken(pref.getString(KEY_USER_TOKEN, null));
        userType = UserType.fromKey(pref.getInt(KEY_USER_TYPE, -1));

        return ApiAccess.getInstance().getAccessToken() != null;
    }


}
