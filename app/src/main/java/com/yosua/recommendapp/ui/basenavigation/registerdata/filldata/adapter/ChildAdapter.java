package com.yosua.recommendapp.ui.basenavigation.registerdata.filldata.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yosua.recommendapp.R;
import com.yosua.recommendapp.model.Data;
import com.yosua.recommendapp.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.Holder> {

    private List<Data> dataList;
    private onItemClickListener onItemClickListener;
    private int pagePosition;

    public interface onItemClickListener {
        void onItemDelete(Data data, int pagePosition, int dataPosition);

        void onItemEdit(Data data, int pagePosition, int dataPosition);
    }

    public ChildAdapter(onItemClickListener onItemClickListener, int pagePosition) {
        dataList = new ArrayList<>();
        this.pagePosition = pagePosition;
        this.onItemClickListener = onItemClickListener;
    }

    public void addData(Data data) {
        dataList.add(data);
        notifyDataSetChanged();
    }

    public void setData(List<Data> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_child_data, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Data data = dataList.get(position);
        holder.tvVendorName.setText("Vendor Name : " + data.getVendorName());
        holder.tvDataName.setText(data.getName());
        holder.tvPrice.setText("Vendor Price : IDR " + Utils.priceFormat(data.getPrice()));
        holder.tvRating.setText("Vendor Rate : " + data.getRate());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemDelete(data, pagePosition, position);
            }
        });

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemEdit(data, pagePosition, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        private TextView tvVendorName;
        private TextView tvDataName;
        private TextView tvPrice;
        private TextView tvRating;
        private Button btnEdit;
        private Button btnDelete;

        public Holder(@NonNull View itemView) {
            super(itemView);
            tvVendorName = itemView.findViewById(R.id.rv_tv_vendor_name);
            tvDataName = itemView.findViewById(R.id.rv_tv_data_name);
            tvPrice = itemView.findViewById(R.id.rv_tv_vendor_price);
            tvRating = itemView.findViewById(R.id.rv_tv_rating);
            btnEdit = itemView.findViewById(R.id.rv_btn_edit);
            btnDelete = itemView.findViewById(R.id.rv_btn_delete);
        }
    }
}
