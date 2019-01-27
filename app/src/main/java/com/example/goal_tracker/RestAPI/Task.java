package com.example.goal_tracker.RestAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Task {

    @SerializedName("task")
    @Expose
    private String task;

    public String getTask() {
        return task;
    }

    public void setTask(String tasks) {
        this.task = task;
    }

    @SerializedName("done")
    @Expose
    private Boolean done;

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @SerializedName("deadline")
    @Expose
    private String deadline;

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
}
