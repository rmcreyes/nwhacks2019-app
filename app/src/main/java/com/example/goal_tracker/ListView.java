package com.example.goal_tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.example.goal_tracker.RestAPI.Goal;
import com.example.goal_tracker.RestAPI.ID;
import com.example.goal_tracker.RestAPI.RestApi;
import com.example.goal_tracker.RestAPI.User;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ListView extends AppCompatActivity {
    private static final String URL = "";
    public int userID;
    public List goals; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int userID = extras.getInt("userID");
        }
        final RestApi api = retroSetup();

        Call<User> call2 = api.getUser(userID);
        try {
            Response<User> retval = call2.execute();
            goals = retval.body().getGoals();
        } catch (IOException e) {
            e.printStackTrace();
        }

        final ListAdapter goal_adpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, goals);
        android.widget.ListView tut_Adaptor_View = (android.widget.ListView) findViewById(R.id.tut_adaptor);
        tut_Adaptor_View.setAdapter(goal_adpt);

        tut_Adaptor_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goals.get(i);
            }
        });
    }


    protected RestApi retroSetup(){
        Retrofit retrofit = getRetro();
        return retrofit.create(RestApi.class);
    }

    protected static Retrofit getRetro(){
        return new Retrofit.Builder().baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }


    private void gotoLists (View view, int goal , int user) {
        Intent gotoTasks = new Intent(this, display_tasks.class);
        getIntent().putExtra("goal", goal);
        getIntent().putExtra("user-id", user);
        startActivity(gotoTasks);

    }
}
