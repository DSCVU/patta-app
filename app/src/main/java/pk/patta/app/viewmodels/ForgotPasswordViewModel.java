package pk.patta.app.viewmodels;

import android.view.View;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;

import pk.patta.app.listeners.ForgotPasswordListener;
import pk.patta.app.utils.InternetCheck;

public class ForgotPasswordViewModel extends ViewModel {

    public ForgotPasswordListener listener;

    public void forgotPassword(String email){
        listener.onForgotStart();
        if (InternetCheck.netCheck()){
            FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            listener.onForgotSuccess();
                        }
                    }).addOnFailureListener(e -> listener.onForgotFailure(e.getMessage()));
        } else {
            listener.onForgotFailure("Connection error! No Internet Connection");
        }
    }
}
