package com.example.goal_tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.goal_tracker.RestAPI.ID;
import com.example.goal_tracker.RestAPI.RestApi;
import com.example.goal_tracker.RestAPI.User;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LogIn extends AppCompatActivity {

    private static final String URL = "";
    private int userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                switchToActivity(ListView.class);
            }
        });
    }


    public void switchToActivity(Class nextClass){
        Intent next = new Intent(this,nextClass);
        next.putExtra("userID",userID);
        startActivity(next);
    }
    protected void makeAccount(){
        String userName;
        EditText userNameInput = findViewById(R.id.userNameInput);
        userName = userNameInput.getText().toString();
        RestApi api = retroSetup();
        Call<ID> call = api.postUser(userName);
        try {
            Response<ID> retval = call.execute();
            userID = retval.body().getID();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected RestApi retroSetup(){
        Retrofit retrofit = getRetro();
        return retrofit.create(RestApi.class);
    }
    protected void login(){
        String userName;
        EditText userNameInput = findViewById(R.id.userNameInput);
        userName = userNameInput.getText().toString();
        RestApi api = retroSetup();

        Call<ID> call = api.getUserID(userName);
        try {
            Response<ID> retval = call.execute();
            if(retval.code() ==404){
                makeAccount();
            }else{
                userID = retval.body().getID();
            }
        } catch (IOException e) {

        }

    }
    protected static Retrofit getRetro(){
        return new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }
}
