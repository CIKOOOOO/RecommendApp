package com.yosua.recommendapp.ui.basenavigation.registerdata.filldata.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.yosua.recommendapp.R;
import com.yosua.recommendapp.model.Data;
import com.yosua.recommendapp.model.MasterData;

import java.util.ArrayList;
import java.util.List;

public class ParentAdapter extends RecyclerView.Adapter<ParentAdapter.Holder> implements ChildAdapter.onItemClickListener {

    private List<MasterData> masterDataList;
    private onItemClickListener onItemClickListener;
    private List<ChildAdapter> childAdapterList;

    public ParentAdapter(ParentAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        masterDataList = new ArrayList<>();
        childAdapterList = new ArrayList<>();
    }

    public List<MasterData> getMasterDataList() {
        return masterDataList;
    }

    @Override
    public void onItemDelete(Data data, int pagePosition, int dataPosition) {
        if (null != masterDataList.get(pagePosition)) {
            MasterData masterData = masterDataList.get(pagePosition);
            List<Data> dataList = masterData.getDataList();
            dataList.remove(dataPosition);
            masterData.setDataList(dataList);
            masterDataList.set(pagePosition, masterData);

            ChildAdapter childAdapter = childAdapterList.get(pagePosition);
            childAdapter.setData(dataList);
            childAdapterList.set(pagePosition, childAdapter);
        }
    }

    @Override
    public void onItemEdit(Data data, int pagePosition, int dataPosition) {
        onItemClickListener.onEditData(data, pagePosition, dataPosition);
    }

    public interface onItemClickListener {
        void onAddMore(int pos);

        void onEditData(Data data, int pagePosition, int dataPosition);
    }

    public void addNewPage() {
        MasterData masterData = new MasterData();
        masterData.setPageName("");
        masterDataList.add(masterData);

        ChildAdapter childAdapter = new ChildAdapter(this, masterDataList.size() - 1);
        childAdapterList.add(childAdapter);

        notifyItemChanged(masterDataList.size() - 1);
    }

    public void addNewData(Data data, int pagePosition) {
        if (null != masterDataList.get(pagePosition) && null != childAdapterList.get(pagePosition)) {
            MasterData masterData = masterDataList.get(pagePosition);
            if (null == masterData.getDataList()) {
                masterData.setDataList(new ArrayList<>());
            }
            masterData.getDataList().add(data);
            masterData.setDataList(masterData.getDataList());
            masterDataList.set(pagePosition, masterData);
            childAdapterList.get(pagePosition).setData(masterData.getDataList());
        }
    }

    public void editData(Data data, int pagePosition, int dataPosition) {
        if (null != masterDataList.get(pagePosition) && null != childAdapterList.get(pagePosition)) {
            MasterData masterData = masterDataList.get(pagePosition);
            masterData.getDataList().get(dataPosition).setData(data);
            masterData.setDataList(masterData.getDataList());
            masterDataList.set(pagePosition, masterData);
            childAdapterList.get(pagePosition).setData(masterData.getDataList());
        }
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_parent_data, parent, false);
        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        ChildAdapter childAdapter = childAdapterList.get(position);

        holder.rvChild.setLayoutManager(new LinearLayoutManager(holder.itemView.getContext()));
        holder.rvChild.setAdapter(childAdapter);

        holder.etPageName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                masterDataList.get(position).setPageName(editable.toString());
            }
        });

        holder.btnAddMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onAddMore(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return masterDataList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        private EditText etPageName;
        private RecyclerView rvChild;
        private Button btnAddMore;

        public Holder(@NonNull View itemView) {
            super(itemView);
            etPageName = itemView.findViewById(R.id.rv_et_input_page_name);
            rvChild = itemView.findViewById(R.id.rv_product_data);
            btnAddMore = itemView.findViewById(R.id.rv_btn_add_more);
        }
    }

}
