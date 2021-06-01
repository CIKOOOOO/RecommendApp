package com.yosua.recommendapp.ui.basenavigation.registerdata.filldata;

import com.yosua.recommendapp.model.Data;

import java.util.List;

public interface IFillDataCallback {
    void onLoadData(List<Data> dataList);

    void onFailed(String msg);
}
