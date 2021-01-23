package com.ppal007.smartvoting.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.ppal007.smartvoting.R;

public class AdapterProgressBar {
    private Context context;
    private AlertDialog dialog;

    public AdapterProgressBar(Context context) {
        this.context = context;
    }

    @SuppressLint("InflateParams")
    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setView(inflater.inflate(R.layout.custom_progress_dialog,null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog(){
        dialog.dismiss();
    }
}
