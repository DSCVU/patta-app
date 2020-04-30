package pk.patta.app.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.klimek.scanner.OnCameraErrorCallback;
import de.klimek.scanner.OnDecodedCallback;
import de.klimek.scanner.ScannerView;
import pk.patta.app.R;
import pk.patta.app.activities.MapsActivity;
import pk.patta.app.databinding.FragmentQrscannerBinding;
import pk.patta.app.viewmodels.QRScannerViewModel;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static pk.patta.app.utils.Configurations.MULTIPLE_PERMISSIONS;
import static pk.patta.app.utils.Configurations.beepOnScan;
import static pk.patta.app.utils.Configurations.codeString;
import static pk.patta.app.utils.Configurations.codesArray;
import static pk.patta.app.utils.Configurations.keepHistory;
import static pk.patta.app.utils.Configurations.permissions;
import static pk.patta.app.utils.Configurations.simpleAlert;
import static pk.patta.app.utils.Configurations.vibrateOnScan;

public class QRScannerFragment extends Fragment implements OnDecodedCallback, OnCameraErrorCallback {

    private FragmentQrscannerBinding binding;
    private QRScannerViewModel viewModel;
    private static final int SELECT_IMAGE = 1;
    private ScannerView scannerView;
    private Camera camera;
    private View root;

    /*--- VARIABLES ---*/
    private Context context;
    private boolean qrCodeFound = false;
    private String metadataType = "";
    private String alertMessage = "";
    private boolean permissionGranted = false;
    private boolean isFlash = false;
    private boolean locationDetected = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel =
                new ViewModelProvider(this).get(QRScannerViewModel.class);
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_qrscanner, container, false);
        root = binding.getRoot();
        binding.setViewModel(viewModel);
        context = root.getContext();
        initViews();
        return root;
    }


    //-----------------------------------------------
    // MARK - ON RESUME
    //-----------------------------------------------
    @Override
    public void onResume() {
        super.onResume();
        if (permissionGranted) {
            binding.scannerView.startScanning();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        binding.scannerView.stopScanning();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        qrCodeFound = false;
    }

    @Override
    public void onCameraError(Exception error) {
        simpleAlert(error.getMessage(), context);
    }

    @Override
    public void onDecoded(String decodedData) {
        processCode(decodedData);
    }

    //-----------------------------------------------
    // MARK - CHECK FOR PERMISSIONS
    //-----------------------------------------------
    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(context, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[0]), MULTIPLE_PERMISSIONS );
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MULTIPLE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "ALL PERMISSIONS GRANTED!");
                permissionGranted = true;
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        Bitmap bMap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data.getData());
                        String contents;

                        int[] intArray = new int[bMap.getWidth()*bMap.getHeight()];
                        //copy pixel data from the Bitmap into the 'intArray' array
                        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

                        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
                        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

                        Reader reader = new MultiFormatReader();
                        Result result = reader.decode(bitmap);
                        contents = result.getText();
                        processCode(contents);
                    } catch (IOException | NotFoundException | ChecksumException | FormatException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "QR/Barcode Not Found", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED)  {
                Toast.makeText(context, "Canceled", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initViews() {
        binding.scannerView.setOnDecodedCallback(this);
        binding.scannerView.setOnCameraErrorCallback(this);

        // Get Camera permission for ScannerView
        int cameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA);
        permissionGranted = cameraPermission == PackageManager.PERMISSION_GRANTED;
        if (!permissionGranted) {
            checkPermissions();
        }
        binding.gallery.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
        });
        binding.flash.setOnClickListener(v -> {
            isFlash = !isFlash;
            flash(isFlash);
        });
    }

    private void flash(boolean isFlash){
        if (isFlash){
            camera = binding.scannerView.getCameraInstance();
            android.hardware.Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(parameters);
        }
        else {
            camera = binding.scannerView.getCameraInstance();
            android.hardware.Camera.Parameters parameters = camera.getParameters();
            parameters.setFlashMode(android.hardware.Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(parameters);
        }
    }

    private void processCode(String decodedData){
        if (!qrCodeFound) {
            codeString = decodedData;

            // Play sound
            if (!beepOnScan) {
                try {
                    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    Ringtone r = RingtoneManager.getRingtone(context, notification);
                    r.play();
                } catch (Exception e) { e.printStackTrace(); }
            }
            // Vibrate
            if (!vibrateOnScan) {
                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    v.vibrate(500);
                }
            }


            qrCodeFound = true;
            Log.i(TAG, "CODE STRING FOUND: " + codeString);

            // Check metadataType
            if (codeString.matches("[0-9]+") && codeString.length() > 2) {
                metadataType = "barcode";
            } else {
                metadataType = "qrcode";
            }

            Log.i(TAG, "METADATA TYPE: " + metadataType);


            // QR CODE ------------------------------------------------------------
            if (metadataType.matches("qrcode")) {

                // Location
                if (codeString.contains("https://maps.google.com/") ){
                    alertMessage = "Location detected";
                    locationDetected = true;
                } else {
                    alertMessage = "No valid location detected!";
                    Snackbar.make(root, alertMessage, Snackbar.LENGTH_LONG).show();
                    locationDetected= false;
//                    qrCodeFound = false;
                }



                // BARCODE ------------------------------------------------------------
            } else {
                alertMessage = "Barcode detected";
//                qrCodeFound = false;
            }

            if (locationDetected){
                // Call function
                saveCodeAndFireAlert();
            }
        }
    }

    // ------------------------------------------------
    // MARK: - SAVE CODE AND FIRE ALERT
    // ------------------------------------------------
    @SuppressLint("CommitPrefEdits")
    private void saveCodeAndFireAlert() {

        // Save codeString in codesArray
        String mdtStr = "__" + metadataType;
        codeString = mdtStr + codeString;

        if (!keepHistory) {
            codesArray.add(codeString);

            // Save codesArray
//            TinyDB tdb = new TinyDB(context);
//            tdb.putListString("codesArray", (ArrayList<String>) codesArray);
//            Log.i(TAG, "CODES ARRAY SAVED: " + codesArray);
        }


        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage(alertMessage)
                .setTitle(R.string.app_name)
                .setPositiveButton("View Map", (dialog, which) -> {
                    qrCodeFound = false;

                    Intent i = new Intent(context, MapsActivity.class);
                    Bundle extras = new Bundle();
                    extras.putString("metadataType", metadataType);
                    extras.putString("codeString", codeString);
                    i.putExtras(extras);
                    startActivity(i);
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    qrCodeFound = false;
                    metadataType = "";
                })
                .setCancelable(false)
//                .setIcon(R.drawable.logo)
                .create().show();
    }
}
