package pk.patta.app.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.facebook.stetho.Stetho;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tiper.MaterialSpinner;
//import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pk.patta.app.R;
import pk.patta.app.databinding.ActivitySignupBinding;
import pk.patta.app.listeners.SignupListener;
import pk.patta.app.models.District;
import pk.patta.app.models.Division;
import pk.patta.app.models.Province;
import pk.patta.app.viewmodels.SignupViewModel;
import pk.patta.app.worker.SaveDataToFirestore;

public class SignupActivity extends AppCompatActivity implements SignupListener, LifecycleOwner {

    private ActivitySignupBinding binding;
    private SignupViewModel viewModel;
    private List<Province> provinces;
    private List<Division> divisions;
    private List<District> districts;
    private Province selectedProvince;
    private Division selectedDivision;
    private District selectedDistrict;
    private String tenDigitCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        viewModel = new ViewModelProvider(this).get(SignupViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.listener = this;
        tenDigitCode = "00000-00000";
        populateProvinceSpinner();
        binding.signupButton.setOnClickListener(v -> {
            if (isValid()){
                viewModel.signup(
                        binding.email.getText().toString().trim(),
                        binding.password.getText().toString().trim());
            }
        });

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
                tenDigitCode = selectedProvince.getId()+"0000-00000";
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
                tenDigitCode = selectedProvince.getId()+selectedDivision.getDivisionId()+"000-00000";
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
            }

            @Override
            public void onNothingSelected(@NonNull MaterialSpinner materialSpinner) {

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
        if (address.isEmpty() || address.length()<6){
            binding.address.setError("Please enter a valid Address");
            valid = false;
        } else {
            binding.address.setError(null);
        }
        return valid;
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
        map.put("province", binding.province.getSelectedItem().toString());
        map.put("division", binding.division.getSelectedItem().toString());
        map.put("district", binding.district.getSelectedItem().toString());
        map.put("union_council", binding.unionCouncil.getText().toString().trim());
        FirebaseFirestore db = FirebaseFirestore.getInstance();
//        String collectionPath = "users/"+ FirebaseAuth.getInstance().getUid();
        db.collection("users").document(FirebaseAuth.getInstance().getUid()).set(map)
                .addOnSuccessListener(aVoid -> {
                    Snackbar.make(findViewById(android.R.id.content), "Success", Snackbar.LENGTH_LONG).show();
                }).addOnFailureListener(e -> {
            Snackbar.make(findViewById(android.R.id.content), e.getMessage(), Snackbar.LENGTH_LONG).show();
                });
        /*Data data = new Data.Builder()
                .putAll(map)
                .build();
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(SaveDataToFirestore.class)
                .setInputData(data)
                .build();
        WorkManager.getInstance(getApplicationContext()).enqueue(request);*/
        startActivity(new Intent(SignupActivity.this, DashboardActivity.class));
    }

    @Override
    public void onSignupFailure(String error) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.signupButton.setEnabled(true);
        Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG).show();
    }
}
