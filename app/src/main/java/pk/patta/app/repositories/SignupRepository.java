package pk.patta.app.repositories;

import android.app.Application;
import android.location.Location;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
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
    private MutableLiveData<Location> locationMutableLiveData = new MutableLiveData<>();
    private FusedLocationProviderClient fusedLocationClient;

    private SignupRepository(Application application){
        database = AppDatabase.getInstance(application);
        firebaseAuth = FirebaseAuth.getInstance();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(application);
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

    public LiveData<Location> getLocation() {
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location!=null){
                        locationMutableLiveData.setValue(location);
                    } else {
                        LocationRequest locationRequest = new LocationRequest();
                        LocationCallback locationCallback;
                        locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(LocationResult locationResult) {
                                if (locationResult == null) {
                                    return;
                                }
                                List<Location> locations = locationResult.getLocations();
                                if (locations.size()>0){
                                    Location location = locations.get(locations.size() - 1);
                                    locationMutableLiveData.setValue(location);
                                }
                            }
                        };
                        fusedLocationClient.requestLocationUpdates(locationRequest,
                                locationCallback,
                                Looper.getMainLooper());
                    }
                });
        return locationMutableLiveData;
    }
}
