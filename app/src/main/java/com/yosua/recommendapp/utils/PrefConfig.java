package com.yosua.recommendapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.yosua.recommendapp.R;
import com.yosua.recommendapp.model.User;


public class PrefConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public PrefConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
    }

    public void setLogin(boolean login, User user) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_login_status), login);
        editor.putString(context.getString(R.string.pref_name), user.getName());
        editor.putString(context.getString(R.string.pref_email), user.getEmail());
        editor.putString(context.getString(R.string.pref_uid), user.getUserID());
        editor.apply();
    }

    public void logOut() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_name), Constant.DATA_NOT_FOUND);
        editor.putString(context.getString(R.string.pref_email), Constant.DATA_NOT_FOUND);
        editor.putBoolean(context.getString(R.string.pref_login_status), false);
        editor.apply();
    }

    public boolean isLogin() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_login_status), false);
    }

    public String getEmail() {
        return sharedPreferences.getString(context.getString(R.string.pref_email), Constant.DATA_NOT_FOUND);
    }

    public String getUID() {
        return sharedPreferences.getString(context.getString(R.string.pref_uid), Constant.DATA_NOT_FOUND);
    }

    public String getName() {
        return sharedPreferences.getString(context.getString(R.string.pref_name), Constant.DATA_NOT_FOUND);
    }
}
