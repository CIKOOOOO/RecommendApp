package com.yosua.recommendapp.ui.register;

public interface IRegisterCallback {
    void onSuccess();

    void onEmailExisting();

    void onFailed(String msg);
}
