package com.example.goal_tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.goal_tracker.RestAPI.RestApi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogIn extends AppCompatActivity {

    private static final String URL = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    protected boolean login(){
        Retrofit retrofit = getRetro();
        RestApi api = retrofit.create(RestApi.class);

    }
    protected static Retrofit getRetro(){
        return new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}
