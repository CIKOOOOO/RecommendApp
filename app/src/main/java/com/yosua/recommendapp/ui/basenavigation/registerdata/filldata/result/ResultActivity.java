package com.yosua.recommendapp.ui.basenavigation.registerdata.filldata.result;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yosua.recommendapp.R;
import com.yosua.recommendapp.model.Data;
import com.yosua.recommendapp.ui.basenavigation.HistoryActivity;
import com.yosua.recommendapp.utils.BaseActivity;

import java.util.List;

public class ResultActivity extends BaseActivity {

    public static final String RESULT_DATA = "result_data";
    public static final String PROJECT_NAME = "project_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initVar();
    }

    private void initVar() {
        ResultAdapter resultAdapter = new ResultAdapter();

        TextView tvProjectName = findViewById(R.id.tv_project_name);
        RecyclerView rvResult = findViewById(R.id.rv_result);

        rvResult.setLayoutManager(new LinearLayoutManager(this));
        rvResult.setAdapter(resultAdapter);

        Intent intent = getIntent();
        if (null != intent && null != intent.getParcelableArrayListExtra(RESULT_DATA) && intent.hasExtra(PROJECT_NAME)) {
            List<Data> dataList = intent.getParcelableArrayListExtra(RESULT_DATA);
            String projectName = intent.getStringExtra(PROJECT_NAME);
            if (null != dataList && null != projectName) {
                tvProjectName.setText(projectName);
                resultAdapter.setData(dataList);
            }
        } else {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HistoryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}