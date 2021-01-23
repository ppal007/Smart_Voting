package com.ppal007.smartvoting.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ppal007.smartvoting.R;
import com.ppal007.smartvoting.model.ModelCandidate;
import com.ppal007.smartvoting.model.ModelVoteCount;
import com.ppal007.smartvoting.retrofit.ApiClient;
import com.ppal007.smartvoting.retrofit.ApiInterFace;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterRvChild extends RecyclerView.Adapter<AdapterRvChild.MyViewHolder> {

    private Context context;
    ArrayList<ModelCandidate> rvModel_child_list;
    private int parent_position;

    public AdapterRvChild(Context context, ArrayList<ModelCandidate> rvModel_child_list, int parent_position) {
        this.context = context;
        this.rvModel_child_list = rvModel_child_list;
        this.parent_position = parent_position;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.rv_sample_child_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textView_candidate_id.setText(rvModel_child_list.get(position).getId());
        holder.textView_name.setText(rvModel_child_list.get(position).getCandidateName());
        String _candidate_id = rvModel_child_list.get(position).getId();
        vote_counter(_candidate_id,holder.textView_vote_counter);


    }

    private void vote_counter(String id, TextView textView_count) {

        ApiInterFace apiInterFace = ApiClient.getApiClient().create(ApiInterFace.class);
        Call<ModelVoteCount> modelVoteCountCall = apiInterFace.vote_count(id);
        modelVoteCountCall.enqueue(new Callback<ModelVoteCount>() {
            @Override
            public void onResponse(Call<ModelVoteCount> call, Response<ModelVoteCount> response) {
                if (response.isSuccessful() && response.body()!=null){
                    ModelVoteCount model = response.body();
                    if (model.isSuccess()){
                        textView_count.setText(model.getCount());
                    }else {
                        textView_count.setText(model.getMessage());
                    }
                }else {
                    Log.d("MyTag", "onResponse: "+"something wrong vote count!");
                }
            }


            @Override
            public void onFailure(Call<ModelVoteCount> call, Throwable t) {
                Log.d("MyTag", "onFailure: "+t.toString());
            }
        });
    }


    @Override
    public int getItemCount() {
        return rvModel_child_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView_name,textView_vote_counter,textView_candidate_id;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_name = itemView.findViewById(R.id.tv_child_name);
            textView_vote_counter = itemView.findViewById(R.id.tv_child_vote_counter);
            textView_candidate_id = itemView.findViewById(R.id.tv_rv_candidate_id);

        }
    }
}
