package com.yosua.recommendapp.ui.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yosua.recommendapp.model.User;
import com.yosua.recommendapp.utils.AESCrypt;
import com.yosua.recommendapp.utils.Tree;

public class LoginViewModel extends AndroidViewModel {

    private DatabaseReference dbRef;
    private ILoginCallback callback;

    public void setCallback(ILoginCallback callback) {
        this.callback = callback;
    }

    public LoginViewModel(@NonNull Application application) {
        super(application);
        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    public void login(String email, String password) {
        dbRef.child(Tree.USER)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean isEmailExisting = false;
                        User user = null;
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            user = dataSnapshot.getValue(User.class);
                            try {
                                if (user != null && user.getEmail().equalsIgnoreCase(email) && user.getPassword().equals(AESCrypt.encrypt(password))) {
                                    isEmailExisting = true;
                                    break;
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        if (isEmailExisting) {
                            callback.onSuccess(user);
                        } else {
                            callback.onFailed("Email or password is incorrect");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}
