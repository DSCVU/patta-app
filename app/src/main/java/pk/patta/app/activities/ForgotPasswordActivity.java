package pk.patta.app.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

import pk.patta.app.R;
import pk.patta.app.databinding.ActivityForgotPasswordBinding;
import pk.patta.app.listeners.ForgotPasswordListener;
import pk.patta.app.viewmodels.ForgotPasswordViewModel;

public class ForgotPasswordActivity extends AppCompatActivity implements ForgotPasswordListener {

    private ActivityForgotPasswordBinding binding;
    private ForgotPasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password);
        viewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);
    }

    @Override
    public void onForgotStart() {
        binding.progressBar.setVisibility(View.VISIBLE);
        binding.materialButton.setEnabled(false);
    }

    @Override
    public void onForgotSuccess() {
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.materialButton.setEnabled(true);
        Snackbar.make(findViewById(android.R.id.content),
                "Verification email is sent to your email account", Snackbar.LENGTH_LONG).show();
        new Handler().postDelayed(ForgotPasswordActivity.this::finish, Snackbar.LENGTH_LONG+1000);
    }

    @Override
    public void onForgotFailure(String error) {
        binding.progressBar.setVisibility(View.INVISIBLE);
        binding.materialButton.setEnabled(true);
        Snackbar.make(findViewById(android.R.id.content),
                error, Snackbar.LENGTH_LONG).show();
    }
}
