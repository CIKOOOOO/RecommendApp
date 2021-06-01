package com.yosua.recommendapp.ui.basenavigation.registerdata.filldata.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.yosua.recommendapp.R;
import com.yosua.recommendapp.model.Data;

public class FillDataDialog extends DialogFragment implements View.OnClickListener {

    public static final String EDIT = "edit";
    public static final String NEW = "new";

    private EditText etDataName, etVendorName, etVendorPrice, etRate;
    private String status;
    private onFillData onFillData;
    private int pagePosition, dataPosition;
    private Data data;

    public void setOnFillData(FillDataDialog.onFillData onFillData) {
        this.onFillData = onFillData;
    }

    public void setStatus(String status, int pagePosition) {
        this.status = status;
        this.pagePosition = pagePosition;
    }

    public void setStatus(String status, Data data, int pagePosition, int dataPosition) {
        this.status = status;
        this.data = data;
        this.dataPosition = dataPosition;
        this.pagePosition = pagePosition;
    }

    public interface onFillData {
        void onSaveData(Data data, int pagePosition);

        void onSaveData(Data data, int pagePosition, int dataPosition);

        void onFailed(String msg);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (dialog.getWindow() != null) {
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_fill_data, container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button btnSave = view.findViewById(R.id.btn_save);

        etDataName = view.findViewById(R.id.et_data_name);
        etVendorName = view.findViewById(R.id.et_vendor_name);
        etVendorPrice = view.findViewById(R.id.et_price);
        etRate = view.findViewById(R.id.et_rate);

        if (status.equals(EDIT)) {
            etDataName.setText(data.getName());
            etVendorName.setText(data.getVendorName());
            etVendorPrice.setText(String.valueOf(data.getPrice()));
            etRate.setText(String.valueOf(data.getRate()));
        }

        btnSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                Data data;
                String dataName = etDataName.getText().toString();
                String vendorName = etVendorName.getText().toString();
                String vendorPrice = etVendorPrice.getText().toString();
                String rate = etRate.getText().toString();

                if (dataName.isEmpty()) {
                    onFillData.onFailed("Data name cannot be empty");
                } else if (vendorName.isEmpty()) {
                    onFillData.onFailed("Vendor name cannot be empty");
                } else if (vendorPrice.isEmpty()) {
                    onFillData.onFailed("Vendor price cannot be empty");
                } else if (rate.isEmpty()) {
                    onFillData.onFailed("Rate cannot be empty");
                } else if (Float.parseFloat(rate) > 5 || Float.parseFloat(rate) < 0) {
                    onFillData.onFailed("Minimum rate is 0 and Maximum rate is 5");
                } else {
                    data = new Data(dataName, vendorName, Double.parseDouble(vendorPrice), Float.parseFloat(rate));

                    if (status.equals(EDIT)) {
                        onFillData.onSaveData(data, pagePosition, dataPosition);
                    } else {
                        onFillData.onSaveData(data, pagePosition);
                    }

                    try {

                    } catch (Exception e) {
                        onFillData.onFailed("Failed to save data, please try again later");
                    }
                }
                break;
        }
    }
}
