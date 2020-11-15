package com.example.attendencemonitor.service.api;

import com.example.attendencemonitor.service.api.adapter.UserTypeAdapter;
import com.example.attendencemonitor.service.model.UserType;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiAccess
{
    private String accessToken;
    private Retrofit retrofit;

    private static ApiAccess instance;

    public static ApiAccess getInstance()
    {
        if (instance == null)
        {
            synchronized (Retrofit.class)
            {
                if (instance == null)
                {
                    instance = new ApiAccess();
                }
            }
        }
        return instance;
    }

    private ApiAccess(){
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(chain -> {
            if(getAccessToken() == null)
            {
                return chain.proceed(chain.request());
            }

            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + getAccessToken())
                    .build();
            return chain.proceed(newRequest);
        }).build();

        Gson gson = new GsonBuilder()
            .registerTypeAdapter(UserType.class, new UserTypeAdapter())
            .create();
        retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://attendance.jloferer.de/api/v1/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public String getAccessToken()
    {
        return accessToken;
    }

    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }

    public Retrofit getRetrofit()
    {
        return retrofit;
    }
}
