package com.ppal007.smartvoting.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelCandidate {

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("candidate_name")
    @Expose
    private String CandidateName;

    @SerializedName("candidate_position")
    @Expose
    private String CandidatePosition;

    @SerializedName("success")
    @Expose
    private boolean success;

    @SerializedName("message")
    @Expose
    private String message;

    public ModelCandidate(String id, String candidateName, String candidatePosition) {
        Id = id;
        CandidateName = candidateName;
        CandidatePosition = candidatePosition;
    }

    public String getId() {
        return Id;
    }

    public String getCandidateName() {
        return CandidateName;
    }

    public String getCandidatePosition() {
        return CandidatePosition;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}
