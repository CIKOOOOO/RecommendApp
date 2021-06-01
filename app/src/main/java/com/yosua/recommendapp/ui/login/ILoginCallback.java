package com.yosua.recommendapp.ui.login;

import com.yosua.recommendapp.model.User;

public interface ILoginCallback {

    void onSuccess(User user);

    void onFailed(String msg);
}
