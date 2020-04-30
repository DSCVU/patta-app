package pk.patta.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.facebook.stetho.Stetho;
import com.google.firebase.auth.FirebaseAuth;

import pk.patta.app.activities.DashboardActivity;
import pk.patta.app.activities.LoginActivity;
import pk.patta.app.databinding.ActivitySplashBinding;
import pk.patta.app.viewmodels.SplashViewModel;
import pk.patta.app.worker.CreateDatabaseWorker;

public class SplashActivity extends AppCompatActivity implements LifecycleOwner {

    private ActivitySplashBinding binding;
    private SplashViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        Stetho.initializeWithDefaults(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);
        viewModel = new ViewModelProvider(this).get(SplashViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

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
