package com.example.goal_tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.example.goal_tracker.RestAPI.ID;
import com.example.goal_tracker.RestAPI.RestApi;
import com.example.goal_tracker.RestAPI.Task;

import retrofit2.Call;
import retrofit2.Retrofit;

import static com.example.goal_tracker.LogIn.getRetro;

public class AddTask extends AppCompatActivity {
    EditText taskDes = findViewById(R.id.taskDescription);
    EditText dayInput = (EditText)findViewById(R.id.day);
    EditText month = (EditText)findViewById(R.id.day);
    EditText dayInput = (EditText)findViewById(R.id.day);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Intent intent = getIntent();
        taskDes.setText(intent.getExtras().getString("Description"));
        deadline.setText(intent.getExtras().getString("deadline"));
    }

    protected void addTask(boolean underGoal){
        RestApi api = retroSetup();
        Intent intent = getIntent();
        Task newTask = new Task();
        newTask.setDeadline(taskDes.getText().toString());
        newTask.setTask(deadline.getText().toString());
        newTask.setDone(false);
        if(underGoal) {
            Call<ID> call = api.postGoalTask(intent.getExtras().getInt("userID"),
                    intent.getExtras().getInt("goalID"),
                    newTask);

        }
    }
    protected RestApi retroSetup(){
        Retrofit retrofit = getRetro();
        return retrofit.create(RestApi.class);
    }
}
