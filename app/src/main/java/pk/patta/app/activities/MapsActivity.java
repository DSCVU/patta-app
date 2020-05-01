package pk.patta.app.activities;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Arrays;
import java.util.List;

import pk.patta.app.R;
import pk.patta.app.databinding.ActivityMapsBinding;
import pk.patta.app.viewmodels.MapsViewModel;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String metadataType = "";
    private String codeString = "";
    private double latitude = -34;
    private double longitude = 151;
    private MapsViewModel viewModel;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps);
        viewModel = new ViewModelProvider(this).get(MapsViewModel.class);
        binding.setViewModel(viewModel);
        viewModel.setLocation(latitude, longitude);

        // Get Data
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        metadataType = extras.getString("metadataType");
        codeString = extras.getString("codeString");
        // Remove the metadataType string
        assert codeString != null;
        codeString = codeString.replace("__" + metadataType, "");
        if (metadataType.matches("qrcode") ){
            Log.d("TestLatLng", "codeString");
            if (codeString.contains("https://maps.google.com/")) {
                String aStr = codeString.replace("https://maps.google.com/local?q=", "");

                List<String> coords = Arrays.asList(aStr.split(","));
                latitude = Double.parseDouble(coords.get(0));
                longitude = Double.parseDouble(coords.get(1));
                viewModel.setLocation(latitude, longitude);
            }
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng reqLocation = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions().position(reqLocation).title("Marker in Required Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(reqLocation, 12.0f));
        /*viewModel.getLocation().observe(this, location -> {
            // Add a marker in Required Location and move the camera
            LatLng reqLocation = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(reqLocation).title("Marker in Required Location"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(reqLocation));
        });*/
    }
}
