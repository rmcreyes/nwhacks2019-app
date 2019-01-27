package com.example.goal_tracker.RestAPI;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ical {
    @SerializedName("ical")
    @Expose
    private String ical;

    public String getIcal() {
        return ical;
    }

    public void setIcal(String ID) {
        this.ical = ical;
    }
}
