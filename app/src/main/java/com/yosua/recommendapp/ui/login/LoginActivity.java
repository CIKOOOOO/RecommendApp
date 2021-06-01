package com.yosua.recommendapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.ViewModelProvider;

import com.google.firebase.FirebaseApp;
import com.yosua.recommendapp.R;
import com.yosua.recommendapp.model.User;
import com.yosua.recommendapp.ui.basenavigation.HistoryActivity;
import com.yosua.recommendapp.ui.register.RegisterActivity;
import com.yosua.recommendapp.utils.BaseActivity;

public class LoginActivity extends BaseActivity implements ILoginCallback, View.OnClickListener {

    private EditText etEmail, etPassword;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initVar();
    }

    private void initVar() {

        Button btnRegister = findViewById(R.id.btn_register);
        Button btnLogin = findViewById(R.id.btn_login);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);

        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        viewModel.setCallback(this);

        FirebaseApp.initializeApp(this);

        btnRegister.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onSuccess(User user) {
        removeCustomLoading();
        prefConfig.setLogin(true, user);
        startActivity(new Intent(this, HistoryActivity.class));
    }

    @Override
    public void onFailed(String msg) {
        removeCustomLoading();
        showSnackBar(msg);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btn_login:
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                if (email.isEmpty() || password.isEmpty()) {
                    showSnackBar("Email or password is empty");
                } else {
                    customLoading.show(getSupportFragmentManager(), "");
                    viewModel.login(email, password);
                }
                break;
        }
    }
}