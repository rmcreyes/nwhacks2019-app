package com.example.goal_tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import com.example.goal_tracker.RestAPI.ID;
import com.example.goal_tracker.RestAPI.RestApi;
import com.example.goal_tracker.RestAPI.Task;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;

import retrofit2.Retrofit;

import static com.example.goal_tracker.LogIn.getRetro;


public class AddTask extends AppCompatActivity {


    EditText taskDes;
    EditText dayInput;
    EditText monthInput;
    EditText yearInput ;
    Button submitButton;
    Switch done;
    int userID;
    int goalID;
    int taskID;
    boolean change;
    boolean underGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        taskDes = findViewById(R.id.taskDescription);
         dayInput = (EditText)findViewById(R.id.day);
        monthInput = (EditText)findViewById(R.id.month);
        yearInput = (EditText)findViewById(R.id.year);
        submitButton= findViewById(R.id.saveButton);
        done = findViewById(R.id.doneTask);
        Intent intent = getIntent();
        if(intent != null) {
            intent.getIntExtra("userID", -1);
            intent.getBooleanExtra("change", false);
            if(change){
                intent.getIntExtra("taskID", -1);
            }
        }
        if(change) {
            taskDes.setText(intent.getExtras().getString("Description"));
            SimpleDateFormat df = new SimpleDateFormat("YYYY-MM-dd");
            java.util.Date date = null;
            try {
                date = df.parse(intent.getExtras().getString("deadline"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            dayInput.setText(cal.get(Calendar.DAY_OF_MONTH));
            monthInput.setText(cal.get(Calendar.MONTH));
            yearInput.setText(cal.get(Calendar.YEAR));
        }
        underGoal= intent.getBooleanExtra("goal",false);
        if(underGoal){
            intent.getIntExtra("goalID",-1);
        }
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(change) {
                    addTask(underGoal);
                }else{
                    changeTask();
                }
                if(underGoal){
                    switchToListActivity();
                }else{
                    switchToGoalActivity();
                }
            }
        });
    }

    public void switchToListActivity(){
        Intent next = new Intent(this,ListView.class);
        next.putExtra("userID",userID);
        startActivity(next);
    }
    public void switchToGoalActivity(){
        Intent next = new Intent(this,display_tasks.class);
        next.putExtra("userID",userID);
        next.putExtra("goalID",goalID);
        startActivity(next);
    }
    protected void addTask(boolean underGoal){
        RestApi api = retroSetup();
        Intent intent = getIntent();

        Task newTask = new Task();
        newTask.setDeadline(taskDes.getText().toString());
        Calendar.getInstance();
        String deadline = dayInput.getText().toString()+"-"+ monthInput.getText().toString()+"-"+
        yearInput.getText().toString();
        newTask.setDeadline(deadline);
        newTask.setDone(done.getShowText());
        if(underGoal) {
            Call<ID> call = api.postGoalTask(intent.getExtras().getInt("userID"),
                    intent.getExtras().getInt("goalID"),
                    newTask);
            try {
                call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else{
            Call<ID> call = api.postTask(intent.getExtras().getInt("userID"),
                    newTask);
            try {
                call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    protected void changeTask(){
        RestApi api = retroSetup();

        Task newTask = new Task();
        newTask.setDeadline(taskDes.getText().toString());
        String deadline = dayInput.getText().toString()+"-"+ monthInput.getText().toString()+"-"+
                yearInput.getText().toString();
        newTask.setDeadline(deadline);
        newTask.setDone(false);
        Call<ID> call = api.putTask(taskID,newTask);
            try {
                call.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

    }
    protected RestApi retroSetup(){
        Retrofit retrofit = getRetro();
        return retrofit.create(RestApi.class);
    }
}
