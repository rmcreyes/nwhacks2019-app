package com.example.goal_tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.goal_tracker.RestAPI.GoalChange;
import com.example.goal_tracker.RestAPI.ID;
import com.example.goal_tracker.RestAPI.RestApi;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import static com.example.goal_tracker.LogIn.getRetro;

public class AddGoal extends AppCompatActivity {
    EditText destButton = findViewById(R.id.destGoal);
    Switch done = findViewById(R.id.doneGoal);
    Button save = findViewById(R.id.saveGoal);
    int userID;
    int goalID;
    boolean change;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goal);
        Intent intent = getIntent();
        change=intent.getBooleanExtra("change",false);
        userID=intent.getIntExtra("userID",-1);
        if(change)
            goalID= intent.getIntExtra("goalID",-1);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(change){
                    changeGoal();
                }else{
                    addGoal();
                }
                changeTo();
            }
        });



    }
    protected  void changeTo(){
        Intent next = new Intent(this,display_tasks.class);
        next.putExtra("goalID",goalID);
        next.putExtra("userID",userID);
        startActivity(next);
    }
    protected void changeGoal() {
        GoalChange newGoal = new GoalChange();
        newGoal.setDone(done.getShowText());
        newGoal.setGoal(destButton.getText().toString());
        RestApi api = retroSetup();
        Call<ID> call = api.putGoal(goalID,newGoal);
        try {
            call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected void addGoal(){
        GoalChange newGoal = new GoalChange();
        newGoal.setDone(done.getShowText());
        newGoal.setGoal(destButton.getText().toString());
        RestApi api = retroSetup();
        Call<ID> call = api.postGoal(userID,newGoal);
        try {
            Response<ID> retval= call.execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    protected RestApi retroSetup(){
        Retrofit retrofit = getRetro();
        return retrofit.create(RestApi.class);
    }
}
