package com.example.goal_tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import retrofit2.http.GET;

public class ListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        int userId;
        @GET
        //make api call w/user id for list of goals
        String[] goals = {"a","b","c","d", "e"};
        final ListAdapter tut_adaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, goals);
        android.widget.ListView tut_Adaptor_View = (android.widget.ListView) findViewById(R.id.tut_adaptor);
        tut_Adaptor_View.setAdapter(tut_adaptor);

        tut_Adaptor_View.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gotoLists(view, tut_adaptor.getItem(i).id , userId);
            }
        });
    }

    private void gotoLists (View view, int goal , int user) {
        Intent gotoTasks = new Intent(this, display_tasks.class);
        getIntent().putExtra("goal", goal);
        getIntent().putExtra("user-id", user-id)
        startActivity(gotoTasks);

    }
}
