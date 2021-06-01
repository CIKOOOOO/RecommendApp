package com.yosua.recommendapp.ui.register;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.yosua.recommendapp.R;
import com.yosua.recommendapp.model.User;
import com.yosua.recommendapp.utils.AESCrypt;
import com.yosua.recommendapp.utils.BaseActivity;
import com.yosua.recommendapp.utils.CustomLoading;
import com.yosua.recommendapp.utils.Utils;

public class RegisterActivity extends BaseActivity implements View.OnClickListener, IRegisterCallback {

    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initVar();
    }

    private void initVar() {

        Button btnRegister = findViewById(R.id.btn_register);

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        viewModel.setCallback(this);

        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                Utils.hideSoftKeyboard(this);

                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();

                if (email.isEmpty()) {
                    showSnackBar("Email is empty");
                } else if (Utils.isEmailNotValid(email)) {
                    showSnackBar("Email is not valid, please try another");
                } else if (name.isEmpty()) {
                    showSnackBar("Please insert name");
                } else if (name.length() < 3) {
                    showSnackBar("Length of name should be more than 3 characters");
                } else if (password.length() < 6) {
                    showSnackBar("Length of password should be more than 6 characters");
                } else if (!password.equals(confirmPassword)) {
                    showSnackBar("Password and confirm password is not match");
                } else {
                    try {
                        customLoading.show(getSupportFragmentManager(), "");
                        User user = new User(name, email, AESCrypt.encrypt(password));
                        viewModel.register(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                        showSnackBar("There is error in our internal server, Try Again Later.");
                    }
                }
                break;
        }
    }

    @Override
    public void onSuccess() {
        removeCustomLoading();
        Toast.makeText(this, "Register success", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    @Override
    public void onEmailExisting() {
        removeCustomLoading();
        showSnackBar("Email is already registered");
    }

    @Override
    public void onFailed(String msg) {
        removeCustomLoading();
        showSnackBar(msg);
    }
}