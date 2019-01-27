package com.example.goal_tracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.goal_tracker.RestAPI.ID;
import com.example.goal_tracker.RestAPI.Ical;
import com.example.goal_tracker.RestAPI.RestApi;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;

import static com.example.goal_tracker.LogIn.getRetro;

public class AddIcal extends AppCompatActivity {
    int userID;
    Button save;
    EditText link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ical);
        save = findViewById(R.id.saveIcal);
        link = findViewById(R.id.icalInput);
        Intent intent = getIntent();
        userID = intent.getIntExtra("userID",-1);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postIcal();
                changeTo();
            }
        });
    }
    protected void changeTo(){
        Intent next = new Intent(this,ListView.class);
        next.putExtra("userID",userID);
        startActivity(next);
    }
    protected RestApi retroSetup(){
        Retrofit retrofit = getRetro();
        return retrofit.create(RestApi.class);
    }
    protected void postIcal(){
        RestApi api = retroSetup();
        Ical  ical = new Ical();
        ical.setIcal(link.getText().toString());
        Call<ID> call = api.postIcal(userID,ical);
        try {
            call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
