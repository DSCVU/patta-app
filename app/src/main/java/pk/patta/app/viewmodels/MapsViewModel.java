package pk.patta.app.viewmodels;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapsViewModel extends ViewModel {
    private double latitude;
    private double longitude;
    private Location location;

    public void setLocation(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
        location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
    }

    public LiveData<Location> getLocation(){
        MutableLiveData<Location> locationData = new MutableLiveData<>();
        locationData.setValue(location);
        return locationData;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
        location.setLatitude(latitude);
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
        location.setLongitude(longitude);
    }
}
