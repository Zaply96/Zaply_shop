package com.crzaor.zaply_shop.util;



import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.crzaor.zaply_shop.R;

public class LoadingDialog {
    private View view;
    private AlertDialog dialog;

    public LoadingDialog(View view){
        this.view = view;
    }

    public void startLoading(){
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

        LayoutInflater inflater = (LayoutInflater) view.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        builder.setView(inflater.inflate(R.layout.loader, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dissmissLoader(){
        dialog.dismiss();
    }
}
