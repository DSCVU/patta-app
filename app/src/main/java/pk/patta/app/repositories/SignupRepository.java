package pk.patta.app.repositories;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import pk.patta.app.database.AppDatabase;
import pk.patta.app.listeners.SignupListener;
import pk.patta.app.models.District;
import pk.patta.app.models.Division;
import pk.patta.app.models.Province;

public class SignupRepository {

    private AppDatabase database;
    private FirebaseAuth firebaseAuth;

    private SignupRepository(Application application){
        database = AppDatabase.getInstance(application);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    public static SignupRepository getInstance(Application application){
        return new SignupRepository(application);
    }

    public LiveData<List<Province>> getProvince(){
        return database.provinceDAO().getProvince();
    }

    public LiveData<List<Division>> getDivision(int provinceId){
        return database.divisionDAO().getDivision(provinceId);
    }

    public LiveData<List<District>> getDistrict(int provinceId, int divisionId){
        return database.districtDAO().getDistrict(provinceId, divisionId);
    }

    public void signup(SignupListener listener, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                listener.onSignupSuccess();
            }
        }).addOnFailureListener(e -> {
            listener.onSignupFailure(e.getMessage());
        });
    }
}
