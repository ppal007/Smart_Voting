package com.ppal007.smartvoting.retrofit;

import com.ppal007.smartvoting.model.ModelCandidate;
import com.ppal007.smartvoting.model.ModelCandidatePosition;
import com.ppal007.smartvoting.model.ModelSchedule;
import com.ppal007.smartvoting.model.ModelUser;
import com.ppal007.smartvoting.model.ModelVote;
import com.ppal007.smartvoting.model.ModelVoteCount;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterFace {

    //insert
    @FormUrlEncoded
    @POST("android/insert_user_data.php")
    Call<ModelUser> insert_user_data(
            @Field("full_name") String FullName,
            @Field("user_name") String UserName,
            @Field("device_id") String DeviceId
    );

    //login
    @FormUrlEncoded
    @POST("android/login_user.php")
    Call<ModelUser> login_user(
            @Field("device_id") String DeviceId
    );

    //get schedule
    @GET("android/retrieve_time_schedule.php")
    Call<List<ModelSchedule>> retrieve_schedule();

    //get candidate position
    @GET("android/retrieve_candidate_position.php")
    Call<List<ModelCandidatePosition>> retrieve_candidate_position();

    //get candidate
    @FormUrlEncoded
    @POST("android/retrieve_candidate.php")
    Call<List<ModelCandidate>> retrieve_candidate(
            @Field("candidate_position") String CandidatePosition
    );

    //for vote
    @FormUrlEncoded
    @POST("android/vote.php")
    Call<ModelVote> vote(
            @Field("candidate_id") String CandidateId,
            @Field("user_name") String UserName,
            @Field("candidate_name") String CandidateName,
            @Field("candidate_position") String CandidatePosition,
            @Field("status") String Status
    );

    //vote count
    @FormUrlEncoded
    @POST("android/count_vote.php")
    Call<ModelVoteCount> vote_count(
            @Field("id") String Id
    );
}
