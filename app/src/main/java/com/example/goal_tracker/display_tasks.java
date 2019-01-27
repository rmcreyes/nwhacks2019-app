package com.example.goal_tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.example.goal_tracker.RestAPI.Goal;
import com.example.goal_tracker.RestAPI.RestApi;
import com.example.goal_tracker.RestAPI.Task;
import com.example.goal_tracker.RestAPI.User;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static com.example.goal_tracker.LogIn.getRetro;

public class display_tasks extends AppCompatActivity {
    public int goalId;
    public int userId;
    public Goal goal;
    private static final String URL = "";
    private ArrayList<String> taskListObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tasks);
        /*Bundle extras = getIntent().getExtras();
        if (extras != null) {
            goalId = extras.getInt("goalID");
            userId = extras.getInt("userID");
            //The key argument here must match that used in the other activity
        }

        displayTasks(goalId);*/
    }

    public void displayTasks(int goalId){ /*
        final RestApi api = retroSetup();
        //get tasks with goal-id
        Call<Goal> callGoalObj = api.getGoals(goalId);

        try {
            Response<Goal> retval = callGoalObj.execute();
            goal = retval.body();
        } catch (IOException e) {
            e.printStackTrace();
        }


        for( int i = 0 ; i < goal.getTasks().size(); i ++ ) {
            //goal.getTasks().get(i);
            Call<Task> ithTask = api.getTasks(goal.getTasks().get(i));
            try {
                Response<Task> retval = ithTask.execute();
                String taskobj = retval.body().getTask();
                taskListObj.add(taskobj);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } */

        taskListObj.add("a");
        taskListObj.add("a");
        taskListObj.add("a");
        taskListObj.add("a");
        taskListObj.add("a");
        taskListObj.add("a");
        taskListObj.add("a");
        taskListObj.add("a");

        final ListAdapter task_adaptor = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskListObj);
        android.widget.ListView tut_Adaptor_View = (android.widget.ListView) findViewById(R.id.task_list);
        tut_Adaptor_View.setAdapter(task_adaptor);
    }


    protected RestApi retroSetup(){
        Retrofit retrofit = getRetro();
        return retrofit.create(RestApi.class);
    }


}
