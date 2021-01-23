package com.ppal007.smartvoting.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.ppal007.smartvoting.R;
import com.ppal007.smartvoting.model.ModelCandidate;
import com.ppal007.smartvoting.model.ModelCandidatePosition;
import com.ppal007.smartvoting.retrofit.ApiClient;
import com.ppal007.smartvoting.retrofit.ApiInterFace;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdapterRvDetails extends RecyclerView.Adapter<AdapterRvDetails.MyViewHolder> {

    private Context context;
    private ArrayList<ModelCandidatePosition> rvModelCandidatePos;


    public AdapterRvDetails(Context context, ArrayList<ModelCandidatePosition> rvModelCandidatePos) {
        this.context = context;
        this.rvModelCandidatePos = rvModelCandidatePos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.rv_sample_details,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //set view
        String _position_name = rvModelCandidatePos.get(position).getPositionName();
        holder.textView_parent.setText(_position_name);

        //get candidate name
        retrieve_candidate_name(_position_name,position,holder.recyclerView_child);

    }

    private void retrieve_candidate_name(String position_name,int parent_position,RecyclerView recyclerView_child) {
        ApiInterFace apiInterFace = ApiClient.getApiClient().create(ApiInterFace.class);
        Call<List<ModelCandidate>> call = apiInterFace.retrieve_candidate(position_name);
        call.enqueue(new Callback<List<ModelCandidate>>() {
            @Override
            public void onResponse(Call<List<ModelCandidate>> call, Response<List<ModelCandidate>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelCandidate> model = response.body();

                    Gson gson = new Gson();
                    String json_str = gson.toJson(model);

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(json_str);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ArrayList<String> _id = new ArrayList<>();
                    ArrayList<String> _candidate_name = new ArrayList<>();
                    ArrayList<String> _candidate_position = new ArrayList<>();
                    for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++){
                        try {
                            _id.add(jsonArray.getJSONObject(i).getString("id"));
                            _candidate_name.add(jsonArray.getJSONObject(i).getString("candidate_name"));
                            _candidate_position.add(jsonArray.getJSONObject(i).getString("candidate_position"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //init model
                    ArrayList<ModelCandidate> rvModel_precedent = new ArrayList<>();
                    for (int i = 0; i < _id.size(); i++){
                        ModelCandidate model_candidate = new ModelCandidate(_id.get(i), _candidate_name.get(i), _candidate_position.get(i));
                        rvModel_precedent.add(model_candidate);

                    }

                    //init rv child
                    init_child_rv(recyclerView_child,rvModel_precedent,parent_position);

                }else {
                    Toast.makeText(context, "something wrong to retrieve candidate", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ModelCandidate>> call, Throwable t) {
                Toast.makeText(context, ""+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init_child_rv(RecyclerView recyclerView_child,ArrayList<ModelCandidate> rvModel_precedent,int parent_position) {
        AdapterRvChild adapterRvChild = new AdapterRvChild(context,rvModel_precedent,parent_position);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context,
                RecyclerView.HORIZONTAL,
                false);
        recyclerView_child.setLayoutManager(linearLayoutManager);
        recyclerView_child.setItemAnimator(new DefaultItemAnimator());
        recyclerView_child.setAdapter(adapterRvChild);

    }

    @Override
    public int getItemCount() {
        return rvModelCandidatePos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView_parent;
        private RecyclerView recyclerView_child;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_parent = itemView.findViewById(R.id.tv_sample_details);
            recyclerView_child = itemView.findViewById(R.id.rv_child_details);
        }
    }
}
