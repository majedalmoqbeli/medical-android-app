package com.majedalmoqbeli.medicalnewapp.helpers;

import android.app.ProgressDialog;
import android.content.Context;

import com.majedalmoqbeli.medicalnewapp.R;


public class MyProgressDialog {
    static ProgressDialog progressDialog = null;
    public static void show(Context context) {
        progressDialog = ProgressDialog.show(context, "", context.getResources().getString(R.string.wait), true);

    }

    public static void hide() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }
}