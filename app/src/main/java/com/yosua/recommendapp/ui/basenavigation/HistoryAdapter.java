package com.yosua.recommendapp.ui.basenavigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yosua.recommendapp.R;
import com.yosua.recommendapp.model.ProjectData;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.Holder> {

    private List<ProjectData> projectDataList;
    private onItemClickListener onItemClickListener;

    public HistoryAdapter(HistoryAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        projectDataList = new ArrayList<>();
    }

    public interface onItemClickListener {
        void onItemClick(ProjectData projectData);
    }

    public void setProjectDataList(List<ProjectData> projectDataList) {
        this.projectDataList = projectDataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_history, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ProjectData projectData = projectDataList.get(position);
        if (null != projectData) {
            holder.tvProjectName.setText(projectData.getProject_name());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(projectData);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return projectDataList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {
        private TextView tvProjectName;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvProjectName = itemView.findViewById(R.id.rv_tv_project_name);
        }
    }
}
