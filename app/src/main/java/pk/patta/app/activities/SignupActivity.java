package pk.patta.app.activities;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tiper.MaterialSpinner;
//import com.jaredrummler.materialspinner.MaterialSpinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import pk.patta.app.R;
import pk.patta.app.databinding.ActivitySignupBinding;
import pk.patta.app.listeners.SignupListener;
import pk.patta.app.models.District;
import pk.patta.app.models.Division;
import pk.patta.app.models.Province;
import pk.patta.app.viewmodels.SignupViewModel;

public class SignupActivity extends FragmentActivity implements SignupListener, LifecycleOwner, OnMapReadyCallback {

    private ActivitySignupBinding binding;
    private SignupViewModel viewModel;
    private List<Province> provinces;
    private List<Division> divisions;
    private List<District> districts;
    private Province selectedProvince;
    private Division selectedDivision;
    private District selectedDistrict;
    private String tenDigitCode;
    private Location currentLocation;
    private GoogleMap mMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        viewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.listener = this;
        Random random = new Random();
        int num = 9999+random.nextInt(100000-9999+1);
        tenDigitCode = "00000-"+num;
        binding.tenDigitCode.setText(tenDigitCode);
        populateProvinceSpinner();
        binding.signupButton.setOnClickListener(v -> {
            if (isValid()){
                viewModel.signup(
                        binding.email.getText().toString().trim(),
                        binding.password.getText().toString().trim());
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        "Some fields might contain invalid values",
                        Snackbar.LENGTH_LONG).show();
            }
        });
// Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapSignup);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void populateProvinceSpinner() {
        List<String> provinceValues = new ArrayList<>();
        List<String> divisionValues = new ArrayList<>();
        List<String> districtValues = new ArrayList<>();

        viewModel.getProvince().observe(this, provinces -> {
            provinceValues.clear();
            this.provinces = provinces;
            for (Province province: provinces) {
                provinceValues.add(province.getProvince());
            }
//            binding.province.setItems(provinceValues);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, provinceValues);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.province.setAdapter(adapter);
        });

        binding.province.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NonNull MaterialSpinner materialSpinner, @NonNull View view, int i, long l) {
                selectedProvince = provinces.get(i);
//                tenDigitCode = selectedProvince.getId()+"0000-00000";
                StringBuilder myName = new StringBuilder(tenDigitCode);
                int id = selectedProvince.getId();
                myName.setCharAt(0, (char) (id+'0'));
                tenDigitCode = myName.toString();
                binding.tenDigitCode.setText(tenDigitCode);
                viewModel.getDivision(selectedProvince.getId()).observe(SignupActivity.this, divisions -> {
                    divisionValues.clear();
                    SignupActivity.this.divisions = divisions;
                    for (Division division: divisions) {
                        divisionValues.add(division.getDivision());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(SignupActivity.this,
                            android.R.layout.simple_spinner_item, divisionValues);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    binding.division.setAdapter(adapter);
                });
            }

            @Override
            public void onNothingSelected(@NonNull MaterialSpinner materialSpinner) {

            }
        });

        binding.division.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NonNull MaterialSpinner materialSpinner, @NonNull View view, int i, long l) {
                selectedDivision = divisions.get(i);
//                tenDigitCode = String.valueOf(selectedProvince.getId())+String.valueOf(selectedDivision.getDivisionId())+"000-00000";

                StringBuilder myName = new StringBuilder(tenDigitCode);
                int id = selectedDivision.getDivisionId();
                myName.setCharAt(1, (char) (id+'0'));
                tenDigitCode = myName.toString();
                binding.tenDigitCode.setText(tenDigitCode);
                viewModel.getDistrict(selectedProvince.getId(), selectedDivision.getDivisionId())
                        .observe(SignupActivity.this, districts -> {
                            districtValues.clear();
                            SignupActivity.this.districts = districts;
                            for (District district: districts) {
                                districtValues.add(district.getDistrict());
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(SignupActivity.this,
                                    android.R.layout.simple_spinner_item, districtValues);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            binding.district.setAdapter(adapter);
                        });
            }

            @Override
            public void onNothingSelected(@NonNull MaterialSpinner materialSpinner) {

            }
        });
        binding.district.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(@NonNull MaterialSpinner materialSpinner, @NonNull View view, int i, long l) {
                selectedDistrict = districts.get(i);
//                tenDigitCode = String.valueOf(selectedProvince.getId())+String.valueOf(selectedDivision.getDivisionId())+
//                        String.valueOf(selectedDistrict.getDistrictId())+"00-00000";
                StringBuilder myName = new StringBuilder(tenDigitCode);
                int id = selectedDistrict.getDistrictId();
                myName.setCharAt(2, (char) (id+'0'));
                tenDigitCode = myName.toString();
                binding.tenDigitCode.setText(tenDigitCode);
            }

            @Override
            public void onNothingSelected(@NonNull MaterialSpinner materialSpinner) {

            }
        });

        binding.unionCouncil.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                StringBuilder myName = new StringBuilder(tenDigitCode);
                if (s.length()==1){
                    myName.setCharAt(3, s.charAt(0));
                    myName.setCharAt(4, '0');
                } else if (s.length()>1){
                    myName.setCharAt(3, s.charAt(0));
                    myName.setCharAt(4, s.charAt(1));
                } else {
                    myName.setCharAt(3, '0');
                    myName.setCharAt(4, '0');
                }
                tenDigitCode = myName.toString();
                binding.tenDigitCode.setText(tenDigitCode);
            }
        });
    }

    private boolean isValid(){
        boolean valid = true;

        String name = binding.name.getText().toString().trim();
        String email = binding.email.getText().toString().trim();
        String password = binding.password.getText().toString().trim();
        String confirmPassword = binding.confirmPassword.getText().toString().trim();
        String address = binding.address.getText().toString().trim();
        String unionCouncil = binding.unionCouncil.getText().toString().trim();

        if (name.isEmpty() || name.length()<6){
            binding.name.setError("Please enter a valid name");
            valid = false;
        } else {
            binding.name.setError(null);
        }
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            binding.email.setError("Please enter a valid email address");
            valid = false;
        } else {
            binding.email.setError(null);
        }
        if (password.isEmpty() || password.length()<6){
            binding.password.setError("Please enter a valid password");
            valid = false;
        } else {
            binding.password.setError(null);
        }
        if (confirmPassword.isEmpty() || confirmPassword.length()<6){
            binding.confirmPassword.setError("Confirm Password is not valid");
            valid = false;
        } else {
            binding.confirmPassword.setError(null);
            if (password.equals(confirmPassword)){
                binding.confirmPassword.setError(null);
            } else {
                binding.confirmPassword.setError("Password didn't match");
            }
        }
        if (address.isEmpty() || address.length()<3){
            binding.address.setError("Please enter a valid Address");
            valid = false;
        } else {
            binding.address.setError(null);
        }
        if (unionCouncil.isEmpty() || unionCouncil.length()<2){
            binding.unionCouncil.setError("Please enter a valid Union Council number");
            valid = false;
        } else {
            binding.unionCouncil.setError(null);
        }
        return valid;
    }


    private void showCurrentAddress() {
        try {
            Geocoder geocoder = new Geocoder(SignupActivity.this, Locale.getDefault());
            List<Address> addresses = null;

            addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
            if (geocoder.isPresent()) {
                Address returnAddress = addresses.get(0);
                String address = returnAddress.getAddressLine(0);
                String city = returnAddress.getLocality();
                String country = returnAddress.getCountryName();
                String zipCode = returnAddress.getPostalCode();

                // Show Address
//                binding.address.setText(address + " " + city + " " + country + " " + zipCode);
                binding.address.setText(address);

            } else {
                Toast.makeText(getApplicationContext(), "Geocoder not present!", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSignupStart() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.signupButton.setEnabled(false);
    }

    @Override
    public void onSignupSuccess() {
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.signupButton.setEnabled(true);
        Map<String, Object> map = new HashMap<>();
        map.put("name", binding.name.getText().toString().trim());
        map.put("address", binding.address.getText().toString().trim());
        if (binding.province.getSelectedItem()!=null || binding.province.getSelectedItem()
                .toString().equals("")){
            map.put("province", binding.province.getSelectedItem().toString());
        }
        if (binding.division.getSelectedItem() != null || binding.division.getSelectedItem()
                .toString().equals("")){
            map.put("division", binding.division.getSelectedItem().toString());
        }
        if (binding.district.getSelectedItem()!=null || binding.district.getSelectedItem()
                .toString().equals("")){
            map.put("district", binding.district.getSelectedItem().toString());
        }
        map.put("union_council", binding.unionCouncil.getText().toString().trim());
        map.put("house_code", tenDigitCode);
        String url = "__qrcodehttps://maps.google.com/local?q="+currentLocation.getLatitude()+
                ","+currentLocation.getLongitude();
        map.put("location_url", url);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String collectionPath = "users/"+ FirebaseAuth.getInstance().getUid();
        db.collection("users").document(FirebaseAuth.getInstance().getUid()).set(map)
                .addOnSuccessListener(aVoid -> {
                    Snackbar.make(findViewById(android.R.id.content), "Success", Snackbar.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
            Snackbar.make(findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_LONG).show();
                });
        startActivity(new Intent(SignupActivity.this, DashboardActivity.class));
    }

    @Override
    public void onSignupFailure(String error) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.signupButton.setEnabled(true);
        Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        viewModel.getLocation().observe(this, location -> {
            mMap.clear();
            LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
            mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 18.0f));
        });
    }
}

//need to update.
