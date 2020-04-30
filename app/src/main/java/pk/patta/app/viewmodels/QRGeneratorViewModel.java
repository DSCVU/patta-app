package pk.patta.app.viewmodels;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import pk.patta.app.listeners.QRGeneratorListener;

public class QRGeneratorViewModel extends ViewModel {

    public QRGeneratorListener QRGeneratorListener;
    private MutableLiveData<String> mText;

    public QRGeneratorViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
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
}