package com.yosua.recommendapp.ui.basenavigation.registerdata.filldata;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.yosua.recommendapp.R;
import com.yosua.recommendapp.model.Data;
import com.yosua.recommendapp.model.MasterData;
import com.yosua.recommendapp.ui.basenavigation.registerdata.filldata.adapter.DotAdapter;
import com.yosua.recommendapp.ui.basenavigation.registerdata.filldata.adapter.ParentAdapter;
import com.yosua.recommendapp.ui.basenavigation.registerdata.filldata.dialog.FillDataDialog;
import com.yosua.recommendapp.ui.basenavigation.registerdata.filldata.result.ResultActivity;
import com.yosua.recommendapp.utils.BaseActivity;
import com.yosua.recommendapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class FillDataActivity extends BaseActivity implements View.OnClickListener, ParentAdapter.onItemClickListener, FillDataDialog.onFillData, IFillDataCallback {

    public static final String PROJECT_NAME = "project_name";

    private ParentAdapter parentAdapter;
    private FillDataDialog fillDataDialog;
    private FillDataViewModel viewModel;
    private DotAdapter dotAdapter;

    private String projectName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_data);
        initVar();
    }

    private void initVar() {
        Button btnAddMorePage = findViewById(R.id.btn_add_more_page);
        Button btnCheckResult = findViewById(R.id.btn_result);

        TextView tvProjectName = findViewById(R.id.tv_project_name);

        RecyclerView rvPage = findViewById(R.id.rv_page);
        RecyclerView rvData = findViewById(R.id.rv_data_project);

        parentAdapter = new ParentAdapter(this);
        dotAdapter = new DotAdapter();
        viewModel = new ViewModelProvider(this).get(FillDataViewModel.class);
        viewModel.setCallback(this);
        viewModel.setPrefConfig(prefConfig);

        rvData.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvPage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        snapHelper.attachToRecyclerView(rvData);

        rvData.setAdapter(parentAdapter);
        rvPage.setAdapter(dotAdapter);

        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra(PROJECT_NAME) != null) {
            projectName = intent.getStringExtra(PROJECT_NAME);
            if (projectName != null && !projectName.isEmpty()) {
                tvProjectName.setText(projectName);
            }
        }

        btnAddMorePage.setOnClickListener(this);
        btnCheckResult.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_add_more_page:
                parentAdapter.addNewPage();
                dotAdapter.addCount();
                break;
            case R.id.btn_result:
//                if (parentAdapter.getMasterDataList().size() < 3) {
//                    showSnackBar("Minimum page is 3");
//                } else {
//                    boolean isError = false;
//                    for (MasterData masterData : parentAdapter.getMasterDataList()) {
//                        if (null == masterData.getDataList() || masterData.getDataList().size() == 0) {
//                            showSnackBar("Minimum data each page is 1");
//                            isError = true;
//                            break;
//                        }
//                    }
//                    if (!isError) {
//                        customLoading.show(getSupportFragmentManager(), "");
//                        viewModel.checkForResult(parentAdapter.getMasterDataList(), projectName);
//                    }
//                }
                viewModel.checkForResult(sampleData(), projectName);
                break;
        }
    }

    @Override
    public void onAddMore(int pos) {
        fillDataDialog = new FillDataDialog();
        fillDataDialog.setOnFillData(this);
        fillDataDialog.setStatus(FillDataDialog.NEW, pos);
        fillDataDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onEditData(Data data, int pagePosition, int dataPosition) {
        fillDataDialog = new FillDataDialog();
        fillDataDialog.setOnFillData(this);
        fillDataDialog.setStatus(FillDataDialog.EDIT, data, pagePosition, dataPosition);
        fillDataDialog.show(getSupportFragmentManager(), "");
    }

    @Override
    public void onSaveData(Data data, int pagePosition) {
        parentAdapter.addNewData(data, pagePosition);
        if (null != fillDataDialog && null != fillDataDialog.getTag()) {
            fillDataDialog.dismiss();
        }
        Utils.hideSoftKeyboard(this);
    }

    @Override
    public void onSaveData(Data data, int pagePosition, int dataPosition) {
        parentAdapter.editData(data, pagePosition, dataPosition);
        if (null != fillDataDialog && null != fillDataDialog.getTag()) {
            fillDataDialog.dismiss();
        }
        Utils.hideSoftKeyboard(this);
    }

    @Override
    public void onFailed(String msg) {
        showSnackBar(msg);
    }

    LinearSnapHelper snapHelper = new LinearSnapHelper() {
        @Override
        public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
            View centerView = findSnapView(layoutManager);
            if (centerView == null)
                return RecyclerView.NO_POSITION;

            int position = layoutManager.getPosition(centerView);
            int targetPosition = -1;
            if (layoutManager.canScrollHorizontally()) {
                if (velocityX < 0) {
                    targetPosition = position - 1;
                } else {
                    targetPosition = position + 1;
                }
            }

            if (layoutManager.canScrollVertically()) {
                if (velocityY < 0) {
                    targetPosition = position - 1;
                } else {
                    targetPosition = position + 1;
                }
            }

            final int firstItem = 0;
            final int lastItem = layoutManager.getItemCount() - 1;
            targetPosition = Math.min(lastItem, Math.max(targetPosition, firstItem));
            return targetPosition;
        }
    };

    private List<MasterData> sampleData() {
        Data data = new Data("Pisang", "Buah Segar", 20000, (float) 1.5);
        Data data1 = new Data("Apel", "Buah Tidak Segar", 10000, (float) 2);
        Data data2 = new Data("Mangga", "Toko Buah", 40000, (float) 1);
        Data data3 = new Data("Anggur", "Buah Toko", 35000, (float) 4);

        List<Data> buahList = new ArrayList<>();
        buahList.add(data);
        buahList.add(data1);
        buahList.add(data2);
        buahList.add(data3);

        MasterData masterData = new MasterData();
        masterData.setPageName("Buah");
        masterData.setDataList(buahList);

        Data data4 = new Data("Samsung S21", "Samsung", 17000, (float) 4);
        Data data5 = new Data("Iphone XS", "Apple", 15000, (float) 4);
        Data data6 = new Data("Black Shark II", "Xiaomi", 9000, (float) 2.5);

        List<Data> handphoneList = new ArrayList<>();
        handphoneList.add(data4);
        handphoneList.add(data5);
        handphoneList.add(data6);

        MasterData masterData2 = new MasterData();
        masterData2.setPageName("Handphone");
        masterData2.setDataList(handphoneList);

        Data data7 = new Data("Macbook M1", "Apple", 20000, (float) 4.5);
        Data data8 = new Data("Gl503GE", "Asus", 17000, (float) 4.9);
        Data data9 = new Data("GF35", "MSI", 12000, (float) 4);
        Data data10 = new Data("Legion Y701", "Lenovo", 19000, (float) 4.2);

        List<Data> laptopList = new ArrayList<>();
        laptopList.add(data7);
        laptopList.add(data8);
        laptopList.add(data9);
        laptopList.add(data10);

        MasterData masterData3 = new MasterData();
        masterData3.setPageName("Laptop");
        masterData3.setDataList(laptopList);

        Data data11 = new Data("G102", "Logitech", 300, (float) 4.3);
        Data data12 = new Data("Rival 100", "Steel Series", 600, (float) 4.5);

        List<Data> mouseList = new ArrayList<>();
        mouseList.add(data11);
        mouseList.add(data12);

        MasterData masterData4 = new MasterData();
        masterData4.setPageName("Mouse");
        masterData4.setDataList(mouseList);

        List<MasterData> masterDataList = new ArrayList<>();
        masterDataList.add(masterData);
        masterDataList.add(masterData2);
        masterDataList.add(masterData3);
        masterDataList.add(masterData4);

        return masterDataList;
    }

    @Override
    public void onLoadData(List<Data> dataList) {
        removeCustomLoading();
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putParcelableArrayListExtra(ResultActivity.RESULT_DATA, (ArrayList<? extends Parcelable>) dataList);
        intent.putExtra(ResultActivity.PROJECT_NAME, projectName);
        startActivity(intent);
    }
}