package com.ppal007.smartvoting.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelSchedule {

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("position")
    @Expose
    private String Position;

    @SerializedName("start_time")
    @Expose
    private String StartTime;

    @SerializedName("end_time")
    @Expose
    private String EndTime;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public ModelSchedule(String id, String position, String startTime, String endTime) {
        Id = id;
        Position = position;
        StartTime = startTime;
        EndTime = endTime;
    }

    public String getId() {
        return Id;
    }

    public String getPosition() {
        return Position;
    }

    public String getStartTime() {
        return StartTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
