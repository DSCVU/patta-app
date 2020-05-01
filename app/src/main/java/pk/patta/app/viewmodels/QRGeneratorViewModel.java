package pk.patta.app.viewmodels;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import pk.patta.app.listeners.QRGeneratorListener;

public class QRGeneratorViewModel extends ViewModel {

    public QRGeneratorListener QRGeneratorListener;
    private MutableLiveData<String> houseCode;
    private MutableLiveData<String> locationUrl;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;

    public QRGeneratorViewModel() {
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        houseCode = new MutableLiveData<>();
        houseCode.setValue("00000-00000");
        locationUrl = new MutableLiveData<>();
        locationUrl.setValue("__qrcodehttps://maps.google.com/local?q=31.4832209,74.0541978");
    }

    public void generateQR(String qrCodeStr){
        QRGEncoder qrgEncoder = new QRGEncoder(
                qrCodeStr, null, QRGContents.Type.TEXT, 512);
        try {
            Bitmap bitmap = qrgEncoder.encodeAsBitmap();
            QRGeneratorListener.generateQRSuccess(bitmap);
//            qrCodeImg.setImageBitmap(bitmap);
        } catch (WriterException e) {
            QRGeneratorListener.generateQRFailure(e.toString());
        }
    }

    public LiveData<String> getHouseCode(){
        if (firebaseUser!=null){
            firebaseFirestore.collection("users").document(firebaseUser.getUid())
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot!=null && documentSnapshot.get("house_code")!=null){
                                houseCode.setValue(documentSnapshot.get("house_code").toString());
                            }
                        }
                    }).addOnFailureListener(e -> {

                    });
        }
        return houseCode;
    }

    public LiveData<String> getLocationUrl(){
        if (firebaseUser!=null){
            firebaseFirestore.collection("users").document(firebaseUser.getUid())
                    .get().addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot!=null && documentSnapshot.get("location_url")!=null){
                        houseCode.setValue(documentSnapshot.get("location_url").toString());
                    }
                }
            }).addOnFailureListener(e -> {

            });
        }
        return locationUrl;
    }
}