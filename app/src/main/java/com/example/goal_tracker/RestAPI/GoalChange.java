package com.example.goal_tracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GoalChange {

        @SerializedName("goal")
        @Expose
        private String goal;

        public String getGoal() {
            return goal;
        }

        public void setGoal(String goal) {
            this.goal = goal;
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



}
