package com.example.goal_tracker;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ID {
    @SerializedName("ID")
    @Expose
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
