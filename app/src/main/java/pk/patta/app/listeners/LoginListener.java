package pk.patta.app.listeners;

public interface LoginListener {
    void onLoginStart();
    void onLoginSuccess();
    void onLoginFailure(String error);
}
