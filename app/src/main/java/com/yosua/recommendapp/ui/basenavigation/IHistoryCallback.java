package com.yosua.recommendapp.ui.basenavigation;

import com.yosua.recommendapp.model.ProjectData;

import java.util.List;

public interface IHistoryCallback {
    void onLoadData(List<ProjectData> projectDataList);
}
