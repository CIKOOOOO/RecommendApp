package com.yosua.recommendapp.model;

import java.util.List;

public class MasterData {

    private String pageName;

    private List<Data> dataList;

    public MasterData() {
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public void setDataList(List<Data> dataList) {
        this.dataList = dataList;
    }

    public String getPageName() {
        return pageName;
    }

    public List<Data> getDataList() {
        return dataList;
    }
}
