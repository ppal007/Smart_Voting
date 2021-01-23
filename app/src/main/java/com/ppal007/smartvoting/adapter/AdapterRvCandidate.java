package com.ppal007.smartvoting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ppal007.smartvoting.R;
import com.ppal007.smartvoting.interFace.RvVoteClick;
import com.ppal007.smartvoting.model.ModelCandidate;

import java.util.ArrayList;

public class AdapterRvCandidate extends RecyclerView.Adapter<AdapterRvCandidate.MyViewHolder> {

    private Context context;
    private ArrayList<ModelCandidate> candidate_list;
    private RvVoteClick rvVoteClick;

    public AdapterRvCandidate(Context context, ArrayList<ModelCandidate> candidate_list, RvVoteClick rvVoteClick) {
        this.context = context;
        this.candidate_list = candidate_list;
        this.rvVoteClick = rvVoteClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.rv_sample_candidate,parent,false);

        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //set views
        holder.textView.setText(candidate_list.get(position).getCandidateName());

        //on click
        holder.itemView.setOnClickListener(v -> rvVoteClick.onClick(
                position,candidate_list.get(position).getId(),
                candidate_list.get(position).getCandidateName(),
                candidate_list.get(position).getCandidatePosition())
        );
    }

    @Override
    public int getItemCount() {
        return candidate_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.tv_rv_candidate);
        }
    }
}
