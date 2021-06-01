package com.yosua.recommendapp.ui.basenavigation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yosua.recommendapp.model.ProjectData;
import com.yosua.recommendapp.utils.Tree;

import java.util.ArrayList;
import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private IHistoryCallback callback;
    private DatabaseReference dbRef;

    public void setCallback(IHistoryCallback callback) {
        this.callback = callback;
    }

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    public void loadData(String UID) {
        String path = Tree.HISTORY + "/" + UID;

        dbRef.child(path)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<ProjectData> projectDataList = new ArrayList<>();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            projectDataList.add(dataSnapshot.getValue(ProjectData.class));
                        }

                        callback.onLoadData(projectDataList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}
