package com.ppal007.smartvoting.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelVote {

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("candidate_id")
    @Expose
    private String CandidateId;

    @SerializedName("user_name")
    @Expose
    private String UserName;

    @SerializedName("candidate_name")
    @Expose
    private String CandidateName;

    @SerializedName("candidate_position")
    @Expose
    private String CandidatePosition;

    @SerializedName("status")
    @Expose
    private String Status;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;


    public String getId() {
        return Id;
    }

    public String getCandidateId() {
        return CandidateId;
    }

    public String getUserName() {
        return UserName;
    }

    public String getCandidateName() {
        return CandidateName;
    }

    public String getCandidatePosition() {
        return CandidatePosition;
    }

    public String getStatus() {
        return Status;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
