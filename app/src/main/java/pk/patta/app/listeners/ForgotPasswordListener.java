package pk.patta.app.listeners;

public interface ForgotPasswordListener {
    void onForgotStart();
    void onForgotSuccess();
    void onForgotFailure(String error);
}
