package pk.patta.app.listeners;

import android.graphics.Bitmap;

public interface QRGeneratorListener {
    void generateQRSuccess(Bitmap bitmap);
    void generateQRFailure(String error);
}
