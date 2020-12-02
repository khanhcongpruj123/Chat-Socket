package com.phong413.ichat;

import java.util.concurrent.Executor;

public interface LoginView {

    void showLoading();
    void hideLoading();
    void goToMainView();
    void showError(String message);
}
