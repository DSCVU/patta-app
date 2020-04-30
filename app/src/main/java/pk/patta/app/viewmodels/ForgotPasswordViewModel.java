package pk.patta.app.viewmodels;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import pk.patta.app.listeners.ForgotPasswordListener;
import pk.patta.app.utils.InternetCheck;

public class ForgotPasswordViewModel extends ViewModel {

    public String email;
    public ForgotPasswordListener listener;

    public void forgotPassword(View view){
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
