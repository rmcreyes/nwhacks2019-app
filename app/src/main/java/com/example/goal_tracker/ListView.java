package com.example.goal_tracker;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListAdapter;
import android.widget.Switch;

import com.example.goal_tracker.RestAPI.Goal;
import com.example.goal_tracker.RestAPI.ID;
import com.example.goal_tracker.RestAPI.RestApi;
import com.example.goal_tracker.RestAPI.Task;
import com.example.goal_tracker.RestAPI.User;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static com.example.goal_tracker.LogIn.getRetro;

public class ListView extends AppCompatActivity {
    public int userID;
    public List<Integer> goals;
    public List<Integer> tasks;
    Switch studySwitch = findViewById(R.id.study);
    boolean study;
    public SharedPreferences mpref = getSharedPreferences("study",0);
    Button addTask = findViewById(R.id.addTaskButton);
    Button addGoal= findViewById(R.id.addGoalsButton);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        WorkManager.getInstance().cancelAllWork();
        startChecking();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userID = extras.getInt("userID");
        }
        final RestApi api = retroSetup();

        Call<User> call2 = api.getUser(userID);
        try {
            Response<User> retval = call2.execute();
            goals = retval.body().getGoals();
            tasks = retval.body().getTasks();
        } catch (IOException e) {
            e.printStackTrace();
        }

        study = mpref.getBoolean("study",true);
        if(study){
            studySwitch.setChecked(true);
            studySwitch.setText("Study");
        }else{
            studySwitch.setChecked(false);
            studySwitch.setText("Exercise");
        }
        studySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                study=studySwitch.getShowText();
                if(study){
                    studySwitch.setText("Study");
                    WorkManager.getInstance().cancelAllWork();
                    startChecking();
                }else{
                    studySwitch.setText("Exercise");
                    WorkManager.getInstance().cancelAllWork();
                    startCheckingNonStudy();
                }
                mpref.edit().putBoolean("study",study);
            }
        });

        List <String> toBePrinted = new ArrayList<String>();
        for( int i = 0 ; i < tasks.size(); i ++ ) {
            Call<Task> getTasks = api.getTasks(tasks.get(i));
            try {
                Response<Task> retval = getTasks.execute();
                toBePrinted.set(i, retval.body().getTask());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for( int k = toBePrinted.size() ; k < (goals.size() + toBePrinted.size()); k ++ ) {
            Call<Goal> getGoals = api.getGoals(goals.get(k));
            try {
                Response<Goal> retval = getGoals.execute();
                toBePrinted.set(k, retval.body().getGoal());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        printTasks(toBePrinted);
        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToAddTask();
            }
        });
        addGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToAddGoal();
            }
        });
    }

    private void changeToAddTask(){
        Intent next = new Intent(this, AddTask.class);
        next.putExtra("userID", userID);
        next.putExtra("change",false);
        next.putExtra("goal",false);
        startActivity(next);
    }

    private void changeToAddGoal(){
        Intent next = new Intent(this, AddGoal.class);
        next.putExtra("userID", userID);
        next.putExtra("change",false);
        startActivity(next);
    }

    private void printTasks(List<String> printMe) {
        List<String> mockList = new ArrayList<>();
        mockList.add("test1");
        mockList.add("test2");
        final ListAdapter goal_adpt = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mockList);
        android.widget.ListView tut_Adaptor_View = (android.widget.ListView) findViewById(R.id.tut_adaptor);
        tut_Adaptor_View.setAdapter(goal_adpt);

        tut_Adaptor_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int g = goals.get(i-tasks.size());
                gotoLists(view, g, userID);
            }
        });

    };


    protected RestApi retroSetup(){
        Retrofit retrofit = getRetro();
        return retrofit.create(RestApi.class);
    }





    private void gotoLists (View view, int goal , int user) {
        Intent gotoTasks = new Intent(this, display_tasks.class);
        getIntent().putExtra("goalID", goal);
        getIntent().putExtra("userID", user);
        startActivity(gotoTasks);

    }

    protected void startChecking(){
        PeriodicWorkRequest.Builder checkBuilder =
                new PeriodicWorkRequest.Builder(CheckWorker.class,
                        5, TimeUnit.MINUTES);
        PeriodicWorkRequest checkWork = checkBuilder.build();
        WorkManager.getInstance().enqueue(checkWork);
    }
    protected void startCheckingNonStudy(){
        PeriodicWorkRequest.Builder checkBuilder =
                new PeriodicWorkRequest.Builder(CheckNonStudyWorker.class,
                        5, TimeUnit.MINUTES);
        PeriodicWorkRequest checkWork = checkBuilder.build();
        WorkManager.getInstance().enqueue(checkWork);
    }

}
