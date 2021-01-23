package com.ppal007.smartvoting.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ppal007.smartvoting.R;
import com.ppal007.smartvoting.adapter.AdapterProgressBar;
import com.ppal007.smartvoting.adapter.AdapterRvCandidate;
import com.ppal007.smartvoting.adapter.AdapterRvPositionName;
import com.ppal007.smartvoting.adapter.AdapterRvSchedule;
import com.ppal007.smartvoting.interFace.PositionClick;
import com.ppal007.smartvoting.interFace.RvVoteClick;
import com.ppal007.smartvoting.model.ModelCandidate;
import com.ppal007.smartvoting.model.ModelCandidatePosition;
import com.ppal007.smartvoting.model.ModelSchedule;
import com.ppal007.smartvoting.model.ModelVote;
import com.ppal007.smartvoting.retrofit.ApiClient;
import com.ppal007.smartvoting.retrofit.ApiInterFace;
import com.ppal007.smartvoting.utils.Common;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements RvVoteClick, PositionClick {

    private String full_name,user_name;
    private Button button_details;
    private RecyclerView recyclerView_schedule;
    private AdapterProgressBar adapterProgressBar;
    private String _candidate_title;
    private RecyclerView recyclerView_position;
    private static String _position_str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide title bar
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        setContentView(R.layout.activity_home);

        //init progress bar
        adapterProgressBar = new AdapterProgressBar(HomeActivity.this);

        //get bundle value from login
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            full_name = bundle.getString("ex_full_name");
            user_name = bundle.getString("ex_user_name");
        }


        //find xml
        recyclerView_schedule = findViewById(R.id.rv_schedule);
        button_details = findViewById(R.id.btn_view_details);
        recyclerView_position = findViewById(R.id.rv_home_position);

        //get schedule
        retrieve_schedule();


        //button details click listener
        button_details.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(),DetailsActivity.class);
            intent.putExtra("ex_position_name",_position_str);
            startActivity(intent);
        });


    }

    private void init_precedent(String title) {
        adapterProgressBar.startLoadingDialog();
        //retrieve candidate
        ApiInterFace apiInterFace = ApiClient.getApiClient().create(ApiInterFace.class);
        Call<List<ModelCandidate>> call = apiInterFace.retrieve_candidate(title);
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
                    ArrayList<ModelCandidate> rvModel = new ArrayList<>();
                    for (int i = 0; i < _id.size(); i++){
                        ModelCandidate model_candidate = new ModelCandidate(_id.get(i), _candidate_name.get(i), _candidate_position.get(i));
                        rvModel.add(model_candidate);

                    }

                    //init dialog
                    init_dialog_candidate(title,rvModel);

                }else {
                    Toast.makeText(HomeActivity.this, "something wrong to retrieve candidate", Toast.LENGTH_SHORT).show();
                    adapterProgressBar.dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<List<ModelCandidate>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
                adapterProgressBar.dismissDialog();
            }
        });




    }

    @SuppressLint("SetTextI18n")
    private void init_dialog_candidate(String title, ArrayList<ModelCandidate> rvModel) {
        adapterProgressBar.dismissDialog();

        Dialog dialog = new Dialog(HomeActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_dialog_vote);
        dialog.setCanceledOnTouchOutside(false);

        //find xml
        TextView textView_position_name = dialog.findViewById(R.id.tv_dialog_vote);
        TextView textView_total_candidate = dialog.findViewById(R.id.tv_dialog_total_candidate);
        TextView textView_slogan = dialog.findViewById(R.id.tv_dialog_slogan);
        View view_dialog = dialog.findViewById(R.id.view_dialog_candidate);
        RecyclerView recyclerView_dialog = dialog.findViewById(R.id.rv_dialog_candidare);
        Button button_cancel = dialog.findViewById(R.id.btn_dialog_cancel);

        //init views
        textView_position_name.setText(title);
        textView_total_candidate.setText("Total Candidate : "+rvModel.size());
        textView_slogan.setText("Please Choose Your "+title);
        //init rv
        AdapterRvCandidate adapter = new AdapterRvCandidate(HomeActivity.this,rvModel,this);
        recyclerView_dialog.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView_dialog.setHasFixedSize(true);
        recyclerView_dialog.setAdapter(adapter);

        //cancel button click listener
        button_cancel.setOnClickListener(v -> dialog.dismiss());

        //show dialog
        dialog.show();
    }

    private void retrieve_schedule() {
        adapterProgressBar.startLoadingDialog();
        ApiInterFace apiInterFace = ApiClient.getApiClient().create(ApiInterFace.class);
        Call<List<ModelSchedule>> call = apiInterFace.retrieve_schedule();
        call.enqueue(new Callback<List<ModelSchedule>>() {
            @Override
            public void onResponse(Call<List<ModelSchedule>> call, Response<List<ModelSchedule>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelSchedule> model = response.body();

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(model);

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    List<String> _id = new ArrayList<>();
                    List<String> _position = new ArrayList<>();
                    List<String> _startTime = new ArrayList<>();
                    List<String> _endTime = new ArrayList<>();

                    for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++){
                        try {
                            _id.add(jsonArray.getJSONObject(i).getString("id"));
                            _position.add(jsonArray.getJSONObject(i).getString("position"));
                            _startTime.add(jsonArray.getJSONObject(i).getString("start_time"));
                            _endTime.add(jsonArray.getJSONObject(i).getString("end_time"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //init schedule model
                    ArrayList<ModelSchedule> rvCatModel = new ArrayList<>();
                    for (int i = 0; i < _id.size(); i++){
                        ModelSchedule model_list = new ModelSchedule(_id.get(i),
                                _position.get(i),
                                _startTime.get(i),
                                _endTime.get(i)

                                );
                        rvCatModel.add(model_list);

                    }

                    //init recycler view
                    initRvSchedule(rvCatModel);
                }else {
                    Toast.makeText(HomeActivity.this, "something wrong to retrieve schedule", Toast.LENGTH_SHORT).show();
                    adapterProgressBar.dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<List<ModelSchedule>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
                adapterProgressBar.dismissDialog();
            }
        });
    }

    private void initRvSchedule(ArrayList<ModelSchedule> rvCatModel) {
        AdapterRvSchedule adapter = new AdapterRvSchedule(getApplicationContext(),rvCatModel);
        recyclerView_schedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView_schedule.setHasFixedSize(true);
        recyclerView_schedule.setAdapter(adapter);
//        adapterProgressBar.dismissDialog();

        //get position
        retrieve_position();
    }

    private void retrieve_position() {
        ApiInterFace apiInterFace = ApiClient.getApiClient().create(ApiInterFace.class);
        Call<List<ModelCandidatePosition>> call = apiInterFace.retrieve_candidate_position();
        call.enqueue(new Callback<List<ModelCandidatePosition>>() {
            @Override
            public void onResponse(Call<List<ModelCandidatePosition>> call, Response<List<ModelCandidatePosition>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    List<ModelCandidatePosition> model = response.body();

                    Gson gson = new Gson();
                    String jsonString = gson.toJson(model);

                    _position_str = jsonString;

                    JSONArray jsonArray = null;
                    try {
                        jsonArray = new JSONArray(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    List<String> _id = new ArrayList<>();
                    List<String> _position_name = new ArrayList<>();
                    for(int i = 0; i < Objects.requireNonNull(jsonArray).length(); i++){
                        try {
                            _id.add(jsonArray.getJSONObject(i).getString("id"));
                            _position_name.add(jsonArray.getJSONObject(i).getString("position_name"));


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    //init model
                    ArrayList<ModelCandidatePosition> rvModelCandidatePos = new ArrayList<>();
                    for (int i = 0; i < _id.size(); i++){
                        ModelCandidatePosition model_list = new ModelCandidatePosition(_id.get(i),
                                _position_name.get(i)

                        );
                        rvModelCandidatePos.add(model_list);
                    }

                    //init rv position
                    init_rv_position(rvModelCandidatePos);

                }else {
                    Toast.makeText(HomeActivity.this, "something wrong to retrieve position name", Toast.LENGTH_SHORT).show();
                    adapterProgressBar.dismissDialog();
                }
            }

            @Override
            public void onFailure(Call<List<ModelCandidatePosition>> call, Throwable t) {
                Toast.makeText(HomeActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
                adapterProgressBar.dismissDialog();
            }
        });
    }

    private void init_rv_position(ArrayList<ModelCandidatePosition> rvModelCandidatePos) {
        AdapterRvPositionName adapterRvPositionName = new AdapterRvPositionName(getApplicationContext(),rvModelCandidatePos,this);
        recyclerView_position.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView_position.setHasFixedSize(true);
        recyclerView_position.setAdapter(adapterRvPositionName);
        //dismiss dialog
        adapterProgressBar.dismissDialog();
    }

    @Override
    public void onClick(int position,String candidateId, String candidateName, String candidatePosition) {
//        Toast.makeText(this, ""+candidateName, Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Vote for "+_candidate_title)
                .setCancelable(false)
                .setPositiveButton("Vote", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface _dialog, int id) {
//                        Toast.makeText(HomeActivity.this, ""+user_id, Toast.LENGTH_LONG).show();
                        //vote
                        adapterProgressBar.startLoadingDialog();
                        ApiInterFace apiInterFace = ApiClient.getApiClient().create(ApiInterFace.class);
                        Call<ModelVote> call = apiInterFace.vote(candidateId,user_name,candidateName,candidatePosition,"selected");
                        call.enqueue(new Callback<ModelVote>() {
                            @Override
                            public void onResponse(Call<ModelVote> call, Response<ModelVote> response) {
                                if (response.isSuccessful() && response.body()!=null){
                                    ModelVote model = response.body();
                                    if (model.isSuccess()){
                                        adapterProgressBar.dismissDialog();
                                        _dialog.dismiss();
                                        Toast.makeText(HomeActivity.this, ""+model.getMessage(), Toast.LENGTH_SHORT).show();
                                    }else {
                                        adapterProgressBar.dismissDialog();
                                        _dialog.dismiss();
                                        Toast.makeText(HomeActivity.this, ""+model.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(HomeActivity.this, "something wrong!", Toast.LENGTH_SHORT).show();
                                    adapterProgressBar.dismissDialog();
                                    _dialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<ModelVote> call, Throwable t) {
                                Toast.makeText(HomeActivity.this, ""+t.toString(), Toast.LENGTH_SHORT).show();
                                adapterProgressBar.dismissDialog();
                                _dialog.dismiss();
                            }
                        });

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface _dialog, int id) {
                _dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onClickPosition(int position, String positionName, String positionId) {
        init_precedent(positionName);
        _candidate_title = positionName;
    }
}