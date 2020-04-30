package pk.patta.app.listeners;

public interface SignupListener {
    void onSignupStart();
    void onSignupSuccess();
    void onSignupFailure(String error);
}
