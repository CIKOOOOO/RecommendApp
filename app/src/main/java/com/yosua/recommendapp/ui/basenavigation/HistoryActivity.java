package com.yosua.recommendapp.ui.basenavigation;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yosua.recommendapp.R;
import com.yosua.recommendapp.model.ProjectData;
import com.yosua.recommendapp.ui.basenavigation.registerdata.RegisterDataActivity;
import com.yosua.recommendapp.ui.basenavigation.registerdata.filldata.result.ResultActivity;
import com.yosua.recommendapp.utils.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends BaseActivity implements View.OnClickListener, IHistoryCallback, HistoryAdapter.onItemClickListener {

    private HistoryViewModel viewModel;
    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initVar();
    }

    private void initVar() {
        FloatingActionButton fabCreateNew = findViewById(R.id.fab_history);
        RecyclerView rvHistory = findViewById(R.id.rv_history);

        historyAdapter = new HistoryAdapter(this);

        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(historyAdapter);

        viewModel = new ViewModelProvider(this).get(HistoryViewModel.class);
        viewModel.setCallback(this);
        viewModel.loadData(prefConfig.getUID());

        fabCreateNew.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_history:
                startActivity(new Intent(this, RegisterDataActivity.class));
                break;
        }
    }

    @Override
    public void onLoadData(List<ProjectData> projectDataList) {
        historyAdapter.setProjectDataList(projectDataList);
    }

    @Override
    public void onItemClick(ProjectData projectData) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putParcelableArrayListExtra(ResultActivity.RESULT_DATA, (ArrayList<? extends Parcelable>) projectData.getData());
        intent.putExtra(ResultActivity.PROJECT_NAME, projectData.getProject_name());
        startActivity(intent);
    }
}