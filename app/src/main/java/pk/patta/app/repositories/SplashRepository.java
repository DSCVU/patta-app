package pk.patta.app.repositories;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import java.util.List;

import pk.patta.app.database.AppDatabase;
import pk.patta.app.models.District;
import pk.patta.app.models.Division;
import pk.patta.app.models.Province;
import pk.patta.app.worker.CreateDatabaseWorker;

public class SplashRepository {

    private Application application;
    private AppDatabase database;

    private SplashRepository(Application application){
        this.application = application;
        database = AppDatabase.getInstance(application);
    }

    public static SplashRepository getInstance(Application application){
        return new SplashRepository(application);
    }

    public LiveData<List<Province>> createDB(){
        AppDatabase database = AppDatabase.getInstance(application);
        return database.provinceDAO().getProvince();
    }
}
