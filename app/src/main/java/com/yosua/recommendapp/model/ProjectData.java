package com.yosua.recommendapp.model;

import java.util.List;

public class ProjectData {
    private String project_name;
    private String project_id;
    private List<Data> data;

    public ProjectData() {
    }

    public String getProject_name() {
        return project_name;
    }

    public String getProject_id() {
        return project_id;
    }

    public List<Data> getData() {
        return data;
    }
}
