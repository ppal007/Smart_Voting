package com.ppal007.smartvoting.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ppal007.smartvoting.R;
import com.ppal007.smartvoting.adapter.AdapterProgressBar;
import com.ppal007.smartvoting.adapter.AdapterRvDetails;
import com.ppal007.smartvoting.model.ModelCandidate;
import com.ppal007.smartvoting.model.ModelCandidatePosition;
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

public class DetailsActivity extends AppCompatActivity {

    private AdapterProgressBar adapterProgressBar;
    private RecyclerView recyclerView_details;
    private String _position_json_string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // hide title bar
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags = WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        setContentView(R.layout.activity_details);

        //init progress bar
        adapterProgressBar = new AdapterProgressBar(DetailsActivity.this);

        //get bundle value from home
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            _position_json_string = bundle.getString("ex_position_name");
        }

        //find xml
        recyclerView_details = findViewById(R.id.rv_details);

        //convert json string to list
        convert_json_to_list();


    }

    private void convert_json_to_list() {
        adapterProgressBar.startLoadingDialog();
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(_position_json_string);
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

        //init parent rv
        init_parent_rv(rvModelCandidatePos);
    }

    private void init_parent_rv(ArrayList<ModelCandidatePosition> rvModelCandidatePos) {
        AdapterRvDetails adapter = new AdapterRvDetails(getApplicationContext(),rvModelCandidatePos);
        recyclerView_details.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView_details.setHasFixedSize(true);
        recyclerView_details.setAdapter(adapter);
        //dismiss dialog
        adapterProgressBar.dismissDialog();
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}