package com.example.goal_tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    public int goalID;
    public int userID;
    public int taskID;
    public Goal goal;
    private ArrayList<String> taskListObj;
    private List<Integer> taskIDArray;
    Button addTask;
    Button editGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tasks);

        addTask  = findViewById(R.id.addTasks);
        editGoal = findViewById(R.id.editGoal);
        Intent intent= getIntent();
        goalID = intent.getIntExtra("goalID",-1);
        userID = intent.getIntExtra("userID",-1);
            //The key argument here must match that used in the other activity


        displayTasks();
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToAddTask();
            }
        });
        editGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToEditGoal();
            }
        });
    }

    public void changeToAddTask(){
        Intent intent = new Intent(this,AddTask.class);
        intent.putExtra("userID",userID);
        intent.putExtra("goalID",goalID);
        intent.putExtra("change",false);
        intent.putExtra("goal",true);
        startActivity(intent);
    }
    public void changeToEditGoal(){
        Intent intent = new Intent(this, AddGoal.class);
        intent.putExtra("userID",userID);
        intent.putExtra("goalID",goalID);
        intent.putExtra("change",true);
        intent.putExtra("goal",false);
        startActivity(intent);
    }
    public void displayTasks(){
        final RestApi api = retroSetup();
        //get tasks with goal-id
        Call<Goal> callGoalObj = api.getGoals(goalID);

        try {
            Response<Goal> retval = callGoalObj.execute();
            goal = retval.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(goal.getTasks()!=null) {
            taskIDArray = goal.getTasks();
            for (int i = 0; i < goal.getTasks().size(); i++) {
                //goal.getTasks().get(i);
                Call<Task> ithTask = api.getTasks(goal.getTasks().get(i));
                try {
                    Response<Task> retval = ithTask.execute();
                    String taskobj = retval.body().getTask();
                    taskListObj.add(taskobj);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            if(taskListObj!=null) {
                final ListAdapter task_adaptor = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskListObj);
                android.widget.ListView tut_Adaptor_View = (android.widget.ListView) findViewById(R.id.task_list);
                tut_Adaptor_View.setAdapter(task_adaptor);
                tut_Adaptor_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        taskID = taskIDArray.get(position);
                        changeToEditTask();
                    }
                });
            }
        }
    }

    protected void changeToEditTask(){
        Intent intent = new Intent();
        intent.putExtra("userID",userID);
        intent.putExtra("taskID",taskID);
        intent.putExtra("change",false);
        intent.putExtra("goal",true);
        startActivity(intent);
    }


    protected RestApi retroSetup(){
        Retrofit retrofit = getRetro();
        return retrofit.create(RestApi.class);
    }


}
