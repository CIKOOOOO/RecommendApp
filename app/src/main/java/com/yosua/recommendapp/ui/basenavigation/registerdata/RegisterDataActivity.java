package com.yosua.recommendapp.ui.basenavigation.registerdata;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.yosua.recommendapp.R;
import com.yosua.recommendapp.ui.basenavigation.registerdata.filldata.FillDataActivity;
import com.yosua.recommendapp.utils.BaseActivity;

public class RegisterDataActivity extends BaseActivity implements View.OnClickListener {

    private EditText etProjectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_data);
        initVar();
    }

    private void initVar() {
        Button btnSubmit = findViewById(R.id.btn_submit_project);

        etProjectName = findViewById(R.id.et_project_name);

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_project:
                String projectName = etProjectName.getText().toString();
//                if (projectName.isEmpty()) {
//                    showSnackBar("Project name cannot be empty");
//                } else if (projectName.length() < 3) {
//                    showSnackBar("Project name characters should be more than 3 characters");
//                } else {
                    Intent intent = new Intent(this, FillDataActivity.class);
                    intent.putExtra(FillDataActivity.PROJECT_NAME, projectName);
                    startActivity(intent);
//                }
                break;
        }
    }
}