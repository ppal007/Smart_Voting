package com.ppal007.smartvoting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ppal007.smartvoting.R;
import com.ppal007.smartvoting.interFace.PositionClick;
import com.ppal007.smartvoting.model.ModelCandidatePosition;

import java.util.ArrayList;

public class AdapterRvPositionName extends RecyclerView.Adapter<AdapterRvPositionName.MyViewHolder> {

    private Context context;
    private ArrayList<ModelCandidatePosition> positionArrayList;
    private PositionClick positionClick;

    public AdapterRvPositionName(Context context, ArrayList<ModelCandidatePosition> positionArrayList,PositionClick positionClick) {
        this.context = context;
        this.positionArrayList = positionArrayList;
        this.positionClick = positionClick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.rv_sample_position_name,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //set text
        holder.textView.setText(positionArrayList.get(position).getPositionName());

        //on click
        holder.itemView.setOnClickListener(v -> positionClick.onClickPosition(position,
                positionArrayList.get(position).getPositionName(),
                positionArrayList.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return positionArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView=itemView.findViewById(R.id.tv_rv_sample_position_name);
        }
    }
}
