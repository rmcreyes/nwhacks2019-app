package com.example.goal_tracker.RestAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class User{

    @SerializedName("tasks")
    @Expose
    private List<Integer> tasks;

    public List<Integer> getTasks() {
        return tasks;
    }

    public void setTasks(List<Integer> tasks) {
        this.tasks = tasks;
    }

    @SerializedName("goals")
    @Expose
    private List<Integer> goals;

    public List<Integer> getGoals() {
        return goals;
    }

    public void setGoals(List<Integer> goals) {
        this.goals = goals;
    }

    @SerializedName("goals")
    @Expose
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
