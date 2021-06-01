package com.yosua.recommendapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

public class BaseActivity extends AppCompatActivity {

    public PrefConfig prefConfig;
    public CustomLoading customLoading;

    private View view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = findViewById(android.R.id.content);
        prefConfig = new PrefConfig(this);
        customLoading = new CustomLoading();

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(getResources().getColor(R.color.sherpa_blue_palette));
//        }

        Utils.setupUI(view, this);
    }

    public void removeCustomLoading(){
        if (customLoading != null && customLoading.getTag() != null) {
            customLoading.dismiss();
        }
    }

    public void showSnackBar(String msg) {
        if (msg.trim().isEmpty())
            msg = "Mohon periksa jaringan Anda";
        Snackbar snackbar = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG)
                .setBackgroundTint(getResources().getColor(android.R.color.black))
                .setTextColor(getResources().getColor(android.R.color.white))
                .setDuration(3000);
        snackbar.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (!isNetworkAvailable()) {
//            customDialog = new CustomDialog(getString(R.string.internet_down), getString(R.string.close)
//                    , ContextCompat.getDrawable(this, R.drawable.ic_close_red), this);
//            customDialog.show(getSupportFragmentManager(), "");
//        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
