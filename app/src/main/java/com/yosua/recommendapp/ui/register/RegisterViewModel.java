package com.yosua.recommendapp.ui.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yosua.recommendapp.model.User;
import com.yosua.recommendapp.utils.Tree;

public class RegisterViewModel extends AndroidViewModel {

    private IRegisterCallback callback;
    private DatabaseReference dbRef;

    public void setCallback(IRegisterCallback callback) {
        this.callback = callback;
    }

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    public void register(User user) {
        dbRef.child(Tree.USER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isEmailExisting = false;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            User user1 = dataSnapshot.getValue(User.class);
                            if (user1 != null && user1.getEmail().equalsIgnoreCase(user.getEmail())) {
                                isEmailExisting = true;
                                callback.onEmailExisting();
                                break;
                            }
                        }

                        if (!isEmailExisting) {
                            insertToDB(user);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void insertToDB(User user) {
        String path = Tree.USER + "/";
        String userID = dbRef.child(path).push().getKey();
        path += userID;
        user.setUserID(userID);
        dbRef
                .child(path)
                .setValue(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        callback.onSuccess();
                    } else {
                        callback.onFailed("Failed to register user");
                    }
                });
    }
}
