package pk.patta.app.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import pk.patta.app.models.District;
import pk.patta.app.models.Division;
import pk.patta.app.models.Province;
import pk.patta.app.repositories.SplashRepository;

public class SplashViewModel extends AndroidViewModel {

    private SplashRepository repository;

    public SplashViewModel(@NonNull Application application) {
        super(application);
        repository = SplashRepository.getInstance(application);
    }

    public LiveData<List<Province>> createDB(){
        return repository.createDB();
    }
}
