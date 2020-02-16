package com.majedalmoqbeli.medicalnewapp.control;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.majedalmoqbeli.medicalnewapp.constents.UserKey;
import com.majedalmoqbeli.medicalnewapp.ui.activities.MainActivity;
import com.majedalmoqbeli.medicalnewapp.ui.activities.WelcomeActivity;


public class SaveSetting {
    private Context context;
    private SharedPreferences sharedPreferences;
    private static final String MyPREFERENCES = "MyPrefs";
    public static String USER_ID = "0";
    public static String USER_TYPE = "0";
    public static String USER_NAME = "0";
    public static String ServerURL = "http://192.168.101.1:8080/Madcare/";


    public SaveSetting(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
    }

    public void saveData() {
        try {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(UserKey.USER_ID_KEY, String.valueOf(USER_ID));
            editor.putString(UserKey.USER_NAME_KEY, String.valueOf(USER_NAME));
            editor.putString(UserKey.USER_TYPE_KEY, String.valueOf(USER_TYPE));
            editor.apply();
            loadData();
        } catch (Exception e) {
        }
    }

    public void loadData() {
        String TempUserID = sharedPreferences.getString(UserKey.USER_ID_KEY, "0");
        String TempUserName = sharedPreferences.getString(UserKey.USER_NAME_KEY, "0");
        String TempUSERTYPE = sharedPreferences.getString(UserKey.USER_TYPE_KEY, "0");

        if (!TempUserID.equals("0")) {
            USER_ID = TempUserID;
            USER_NAME = TempUserName;
            USER_TYPE = TempUSERTYPE;

            if (!USER_ID.equals("0")) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);

            } else {
                Intent intent = new Intent(context, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        } else {
            Intent intent = new Intent(context, WelcomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void deleteData() {
        USER_ID = "0";
        USER_NAME = "0";
        USER_TYPE = "0";
        saveData();
    }

}