package pk.patta.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.stetho.Stetho;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import pk.patta.app.activities.DashboardActivity;
import pk.patta.app.activities.ForgotPasswordActivity;
import pk.patta.app.activities.LoginActivity;
import pk.patta.app.databinding.ActivitySplashBinding;
import pk.patta.app.viewmodels.SplashViewModel;
import pk.patta.app.worker.CreateDatabaseWorker;

public class SplashActivity extends AppCompatActivity implements LifecycleOwner {

    private ActivitySplashBinding binding;
    private SplashViewModel viewModel;
    private FirebaseAuth firebaseAuth;
    private static int PERMISSION_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        Stetho.initializeWithDefaults(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        checkPermission();
    }

    private void checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_DENIED || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) ==
                    PackageManager.PERMISSION_DENIED){
                String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
                requestPermissions(permission, PERMISSION_CODE);
            } else {
                moveNext();
            }
        } else {
            moveNext();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                moveNext();
            } else {
                Snackbar.make(findViewById(android.R.id.content),
                        "Permission for required action is not given"
                        , Snackbar.LENGTH_LONG).show();
                new Handler().postDelayed(SplashActivity.this::finish, Snackbar.LENGTH_LONG+1000);
            }
        }
    }

    private void moveNext(){
        new Handler().postDelayed(() -> {
            //If Database not exit, create it first.
            viewModel.createDB().observe(SplashActivity.this, provinces -> {
                if (provinces == null || provinces.isEmpty()){
                    OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(CreateDatabaseWorker.class)
                            .build();
                    WorkManager.getInstance(getApplicationContext()).enqueue(request);
                }
                SplashActivity.this.finish();
            });
            if (firebaseAuth.getCurrentUser()!=null){
                startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
            } else {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
            }
        }, 2000);
    }
}
