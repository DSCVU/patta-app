package pk.patta.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import pk.patta.app.R;
import pk.patta.app.databinding.ActivityLoginBinding;
import pk.patta.app.listeners.LoginListener;
import pk.patta.app.viewmodels.LoginViewModel;

public class LoginActivity extends AppCompatActivity implements LoginListener, LifecycleOwner {

    private ActivityLoginBinding binding;
    private LoginViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);
        viewModel.listener = this;
        binding.createAccount.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, SignupActivity.class)));
        binding.forgotPassword.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class)));
    }

    @Override
    public void onLoginStart() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.loginButton.setEnabled(false);
    }

    @Override
    public void onLoginSuccess() {
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.loginButton.setEnabled(true);
        startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
    }

    @Override
    public void onLoginFailure(String error) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.loginButton.setEnabled(true);
        Snackbar.make(findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG).show();
    }
}
