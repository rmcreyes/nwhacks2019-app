package com.example.goal_tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import java.lang.reflect.Array;

public class display_tasks extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_tasks);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int goal-id = extras.getInt("goal");
            int user-id = extras.getInt("user-id");
            //get list of goals w/user id
            //get list of tasks w/goal id

            receiveTasks(tasks);
            //The key argument here must match that used in the other activity
        }
    }

    public void receiveTasks(String[] task_arr){
        //get tasks with goal-id
        final ListAdapter task_adaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, task_arr);
        android.widget.ListView tut_Adaptor_View = (android.widget.ListView) findViewById(R.id.task_list);
        tut_Adaptor_View.setAdapter(task_adaptor);
    }
}
