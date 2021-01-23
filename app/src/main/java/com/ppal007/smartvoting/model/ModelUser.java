package com.ppal007.smartvoting.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelUser {

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("full_name")
    @Expose
    private String FullName;

    @SerializedName("user_name")
    @Expose
    private String UserName;

    @SerializedName("device_id")
    @Expose
    private String DeviceId;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public String getId() {
        return Id;
    }

    public String getFullName() {
        return FullName;
    }

    public String getUserName() {
        return UserName;
    }


    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
