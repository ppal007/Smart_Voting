package com.ppal007.smartvoting.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelCandidatePosition {

    @SerializedName("id")
    @Expose
    private String Id;

    @SerializedName("position_name")
    @Expose
    private String PositionName;

    public ModelCandidatePosition(String id, String positionName) {
        Id = id;
        PositionName = positionName;
    }

    public String getId() {
        return Id;
    }

    public String getPositionName() {
        return PositionName;
    }
}
