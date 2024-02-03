package com.example.appdemo.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.appdemo.R;

public class IndicatorDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // dialog = new AlertDialog.Builder(this);
//        dialog.setTitle("This is Dialog");
//        dialog.setCancelable(false);
//        dialog.setView(R.layout.indicator_layout);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("");
        builder.setCancelable(false);
        builder.setView(R.layout.indicator_layout);

        return builder.create();
    }
}
