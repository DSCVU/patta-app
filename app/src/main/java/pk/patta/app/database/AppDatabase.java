package pk.patta.app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import pk.patta.app.models.District;
import pk.patta.app.models.Division;
import pk.patta.app.models.Province;

@Database(entities = {Province.class, Division.class, District.class}, version = 1, exportSchema = false)
public abstract class AppDatabase  extends RoomDatabase {
    private static AppDatabase instance;
    public abstract ProvinceDAO provinceDAO();
    public abstract DivisionDAO divisionDAO();
    public abstract DistrictDAO districtDAO();

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null){
            instance = Room.databaseBuilder(context, AppDatabase.class, "pattadotpk.db")
                    .build();
        }
        return instance;
    }
}
