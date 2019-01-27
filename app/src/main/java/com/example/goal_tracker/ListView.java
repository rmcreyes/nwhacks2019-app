package com.example.goal_tracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class ListView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        String[] foods = {"a","b","c","d", "e"};
        ListAdapter tut_adaptor = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, foods);
        android.widget.ListView tut_Adaptor_View = (android.widget.ListView) findViewById(R.id.tut_adaptor);
        tut_Adaptor_View.setAdapter(tut_adaptor);
    }
}
