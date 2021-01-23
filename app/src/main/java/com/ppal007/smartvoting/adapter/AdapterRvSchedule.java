package com.ppal007.smartvoting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ppal007.smartvoting.R;
import com.ppal007.smartvoting.model.ModelSchedule;

import java.sql.Time;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AdapterRvSchedule extends RecyclerView.Adapter<AdapterRvSchedule.MyViewHolder> {

    private Context context;
    private ArrayList<ModelSchedule> schedule_list;

    public AdapterRvSchedule(Context context, ArrayList<ModelSchedule> schedule_list) {
        this.context = context;
        this.schedule_list = schedule_list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.rv_schedule_custom,parent,false);

        return new MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //set views
        String _positionName = schedule_list.get(position).getPosition();

        //convert start time to milli
        String milli_time = convert_start_time_to_milli(schedule_list.get(position).getStartTime());
        //convert milli to time
        long startTimeMilli = Long.parseLong(milli_time);
        Time startTime = new Time(startTimeMilli);
        @SuppressLint("SimpleDateFormat")
        Format startTimeFormat = new SimpleDateFormat("hh:mm a");
        String _start_time = startTimeFormat.format(startTime);


        //convert end time to milli
        String end_milli_time = convert_start_time_to_milli(schedule_list.get(position).getEndTime());
        //convert milli to time
        long endTimeMilli = Long.parseLong(end_milli_time);
        Time endTime = new Time(endTimeMilli);
        @SuppressLint("SimpleDateFormat")
        Format endTimeFormat = new SimpleDateFormat("hh:mm a");
        String _end_time = endTimeFormat.format(endTime);

        //set text
        holder.textView_position.setText(_positionName+" : "+_start_time+" to "+_end_time);


    }

    private String convert_start_time_to_milli(String tm) {

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String strLong = null;
        try {
            Date date = format.parse(tm);
            assert date != null;
            long milliseconds = date.getTime();
            strLong = Long.toString(milliseconds);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return strLong;
    }

    @Override
    public int getItemCount() {
        return schedule_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView_position;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_position = itemView.findViewById(R.id.tv_rv_schedule_position);

        }
    }


}
