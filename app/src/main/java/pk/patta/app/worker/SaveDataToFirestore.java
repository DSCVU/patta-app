package pk.patta.app.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Operation;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class SaveDataToFirestore extends Worker {

    public SaveDataToFirestore(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String collectionPath = "users/"+ FirebaseAuth.getInstance().getUid();
        /*String name = getInputData().getString("name");
        String address = getInputData().getString("address");
        String province = getInputData().getString("province");
        String division = getInputData().getString("division");
        String district = getInputData().getString("district");
        String union_council = getInputData().getString("union_council");*/
        Map<String, Object> map = getInputData().getKeyValueMap();
        db.collection(collectionPath).add(map).addOnSuccessListener(documentReference -> {

        }).addOnFailureListener(e -> {

        });
        return Result.success();
    }
}
