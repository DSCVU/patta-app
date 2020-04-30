package pk.patta.app.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import pk.patta.app.R;
import pk.patta.app.activities.MapsActivity;
import pk.patta.app.databinding.FragmentQrgeneratorBinding;
import pk.patta.app.listeners.QRGeneratorListener;
import pk.patta.app.utils.InternetCheck;
import pk.patta.app.viewmodels.QRGeneratorViewModel;

public class QRGeneratorFragment extends Fragment implements QRGeneratorListener {

    private QRGeneratorViewModel viewModel;
    private FragmentQrgeneratorBinding binding;
    private View root;
    private AppCompatImageView qRImage;
    private String locationUrl = "__qrcodehttps://maps.google.com/local?q=31.4832209,74.0541978";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(QRGeneratorViewModel.class);
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_qrgenerator, container, false);
        root = binding.getRoot();
        binding.setViewModel(viewModel);
        viewModel.QRGeneratorListener = this;
        viewModel.generateQR(locationUrl);
        binding.shareButton.setOnClickListener(v -> {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = locationUrl;
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Location");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));
        });
        binding.viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MapsActivity.class);
            intent.putExtra("metadataType", "qrcode");
            intent.putExtra("codeString", locationUrl);
            startActivity(intent);
        });

//        final TextView textView = root.findViewById(R.id.text_dashboard);
//        qrGeneratorViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void generateQRSuccess(Bitmap bitmap) {
        binding.generatedQr.setImageBitmap(bitmap);
    }

    @Override
    public void generateQRFailure(String error) {
        Snackbar.make(root.findViewById(android.R.id.content), error, Snackbar.LENGTH_LONG).show();
    }
}
