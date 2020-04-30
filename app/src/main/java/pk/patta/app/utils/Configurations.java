package pk.patta.app.utils;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import pk.patta.app.R;

public class Configurations {
    // ------------------------------------------------
    // MARK: - GLOBAL VARIABLES
    // ------------------------------------------------
    public static String TAG = "log-";
    public static boolean mustReload = false;
    public static List<String> codesArray = new ArrayList<>();
    public static String codeString = "";
    public static boolean openLinks = false;
    public static  boolean beepOnScan = false;
    public static boolean vibrateOnScan = false;
    public static boolean keepHistory = false;

    //-----------------------------------------------
    // MARK - SIMPLE ALERT DIALOG
    //-----------------------------------------------
    public static void simpleAlert(String mess, Context activity) {
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        alert.setMessage(mess)
                .setTitle(R.string.app_name)
                .setPositiveButton("OK", null)
//                .setIcon(R.drawable.logo)
                .create().show();
    }

    // Permission
    public static int MULTIPLE_PERMISSIONS = 10;
    public static String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
            Manifest.permission.CAMERA
    };
}
