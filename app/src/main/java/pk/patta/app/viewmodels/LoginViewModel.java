package pk.patta.app.viewmodels;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import pk.patta.app.listeners.LoginListener;
import pk.patta.app.utils.InternetCheck;

public class LoginViewModel extends ViewModel {
    public String email, password;
    public LoginListener listener;

    public void login(View view){
        listener.onLoginStart();
        if (email != null && password != null){
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            if (InternetCheck.netCheck()){
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()){
                        listener.onLoginSuccess();
                    }
                }).addOnFailureListener(e -> listener.onLoginFailure(e.getMessage()));
            } else {
                listener.onLoginFailure("Connection Error! No Internet Connection");
            }
        } else {
            listener.onLoginFailure("Either Email or Password field is not valid");
        }
    }
}
