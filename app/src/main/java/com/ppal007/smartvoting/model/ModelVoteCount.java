package com.ppal007.smartvoting.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelVoteCount {

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("count")
    @Expose
    private String Count;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public String getCount() {
        return Count;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
