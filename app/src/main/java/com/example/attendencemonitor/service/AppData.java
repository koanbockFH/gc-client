package com.example.attendencemonitor.service;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.attendencemonitor.service.api.ApiAccess;
import com.example.attendencemonitor.service.contract.IUserService;
import com.example.attendencemonitor.service.model.UserModel;
import com.example.attendencemonitor.service.model.UserType;

/**
 * handles user Session and is used as abstraction layer between ApiAccess and presentaiton layer (session Activity)
 */
public class AppData
{
    public static final String KEY_USER_TOKEN = "user_token";
    public static final String KEY_USER_TYPE = "user_type";
    private static final String PREF_FILE_NAME = "MonitorPrefs";
    private static AppData instance;
    private UserType userType;

    private AppData()
    {

    }

    //singleton implementation
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


    /**
     * Create session and save details to shared preferences, to allow for permanent login (until token expires)
     * @param context context to save into shared preferences
     * @param user user information
     */
    public void createSession(Context context, UserModel user) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_FILE_NAME, 0).edit();
        //save token
        editor.putString(KEY_USER_TOKEN, ApiAccess.getInstance().getAccessToken());
        //save usertype - for faster access on appstart
        editor.putInt(KEY_USER_TYPE, user.getUserType().getKey());
        editor.apply();
        this.userType = user.getUserType();
    }

    /**
     * close session and save details to shared preferences, to remove for permanent login
     * @param context context to save into shared preferences
     */
    public void closeSession(Context context)
    {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREF_FILE_NAME, 0).edit();
        editor.putString(KEY_USER_TOKEN, null);
        editor.putInt(KEY_USER_TYPE, -1);
        editor.apply();
    }

    /**
     * get session status from shared preferences, to allow for permanent login (until token expires)
     * @param context context to read from shared preferences
     */
    public boolean getSession(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_FILE_NAME, 0);
        ApiAccess.getInstance().setAccessToken(pref.getString(KEY_USER_TOKEN, null));
        userType = UserType.fromKey(pref.getInt(KEY_USER_TYPE, -1));

        return ApiAccess.getInstance().getAccessToken() != null;
    }


}
