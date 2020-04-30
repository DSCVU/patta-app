package pk.patta.app.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pk.patta.app.listeners.SignupListener;
import pk.patta.app.models.District;
import pk.patta.app.models.Division;
import pk.patta.app.models.Province;
import pk.patta.app.repositories.SignupRepository;
import pk.patta.app.utils.InternetCheck;

public class SignupViewModel extends AndroidViewModel {

    private SignupRepository repository;
    public SignupListener listener;

    public SignupViewModel(@NonNull Application application) {
        super(application);
        repository = SignupRepository.getInstance(application);
    }

    public LiveData<List<Province>> getProvince(){
        return repository.getProvince();
    }

    public LiveData<List<Division>> getDivision(int provinceId){
        return repository.getDivision(provinceId);
    }

    public LiveData<List<District>> getDistrict(int provinceId, int divisionId){
        return repository.getDistrict(provinceId, divisionId);
    }

    public void signup(String email, String password) {
        listener.onSignupStart();
        if (InternetCheck.netCheck()){
            repository.signup(listener, email, password);
        } else {
            listener.onSignupFailure("Connection Error! No Internet Connection");
        }
    }
}
