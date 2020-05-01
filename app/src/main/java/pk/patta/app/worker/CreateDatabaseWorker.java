package pk.patta.app.worker;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.ArrayList;
import java.util.List;

import pk.patta.app.database.AppDatabase;
import pk.patta.app.models.District;
import pk.patta.app.models.Division;
import pk.patta.app.models.Province;

public class CreateDatabaseWorker extends Worker {

    public CreateDatabaseWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        AppDatabase database = AppDatabase.getInstance(getApplicationContext());
        List<Province> provinces = new ArrayList<>();
        provinces.add(new Province(1, "KPK"));
        /*provinces.add(new Province(2, "FATA"));
        provinces.add(new Province(3, "PUNJAB"));
        provinces.add(new Province(4, "SIDTH"));
        provinces.add(new Province(5, "BALOCHISTAN"));
        provinces.add(new Province(6, "ISLAMABAD"));
        provinces.add(new Province(7, "GILGIT BALTISTAN"));*/
        database.provinceDAO().insert(provinces);

        List<Division> divisions = new ArrayList<>();
        divisions.add(new Division(0, "BANNU", 1));
        divisions.add(new Division(1, "DERA ISMAIL KHAN", 1));
        divisions.add(new Division(2, "HAZARA", 1));
        divisions.add(new Division(3, "KOHAT", 1));
        divisions.add(new Division(4, "MALAKAND", 1));
        divisions.add(new Division(5, "MARDAN", 1));
        divisions.add(new Division(6, "PESHAWAR", 1));

        /*divisions.add(new Division(0, "BAJAUR AGENCY", 2));
        divisions.add(new Division(1, "MOHMAND AGENCY", 2));
        divisions.add(new Division(2, "KHYBER AGENCY", 2));
        divisions.add(new Division(3, "KHURRAM AGENCY", 2));
        divisions.add(new Division(4, "ORAKZAI AGENCY", 2));
        divisions.add(new Division(5, "N. WAZIRISTAN AGENCY", 2));
        divisions.add(new Division(6, "S. WAZIRISTAN AGENCY", 2));

        divisions.add(new Division(0, "BAHAWALPUR", 3));
        divisions.add(new Division(1, "DERA GHAZI KHAN", 3));
        divisions.add(new Division(2, "FAISALABAD", 3));
        divisions.add(new Division(3, "GUJRANWALA", 3));
        divisions.add(new Division(4, "LAHORE", 3));
        divisions.add(new Division(5, "MULTAN", 3));
        divisions.add(new Division(6, "RAWALPINDI", 3));
        divisions.add(new Division(7, "SAHIWAL", 3));
        divisions.add(new Division(8, "SARGODHA", 3));
        divisions.add(new Division(9, "SHEIKHUPURA", 3));

        divisions.add(new Division(0, "BHANBHORE DIVISION", 4));
        divisions.add(new Division(1, "HYDRABAD DIVISION", 4));
        divisions.add(new Division(2, "KARACHI DIVISION", 4));
        divisions.add(new Division(3, "LARKANA DIVISION", 4));
        divisions.add(new Division(4, "MIRPUR KHAS DIVISION", 4));
        divisions.add(new Division(5, "SUKKUR DIVISION", 4));
        divisions.add(new Division(6, "SHAHEED BENAZIR ABAD DIVISION", 4));

        divisions.add(new Division(0, "KALAT DIVISION", 5));
        divisions.add(new Division(1, "MAKRAN DIVISION", 5));
        divisions.add(new Division(2, "NISARABAD DIVISION", 5));
        divisions.add(new Division(3, "QUETTA DIVISION", 5));
        divisions.add(new Division(4, "SIBI DIVISION", 5));
        divisions.add(new Division(5, "ZHOB DIVISION", 5));

        divisions.add(new Division(1, "BALTISTAN DIVISION", 6));
        divisions.add(new Division(2, "DIAMER DIVISION", 6));
        divisions.add(new Division(3, "GILGIT DIVISION", 6));*/

        database.divisionDAO().insert(divisions);

        List<District> districts = new ArrayList<>();
        districts.add(new District(1, "BANNU", 0, 1));
        districts.add(new District(2, "LAKKI MARWAT", 0, 1));

        districts.add(new District(1, "DERA ISMAIL KHAN", 1, 1));
        districts.add(new District(2, "TANK", 1, 1));

        districts.add(new District(1, "ABOTTABAD", 2, 1));
        districts.add(new District(2, "BATTAGRAM", 2, 1));
        districts.add(new District(3, "HARIPUR", 2, 1));
        districts.add(new District(4, "LOWER KOHISTAN", 2, 1));
        districts.add(new District(5, "MANSEHRA", 2, 1));
        districts.add(new District(6, "TORGHAR", 2, 1));
        districts.add(new District(7, "UPPER KOHISTAN", 2, 1));
        districts.add(new District(8, "KOLAI PALAS", 2, 1));

        districts.add(new District(1, "HANGU", 3, 1));
        districts.add(new District(2, "KARAK", 3, 1));
        districts.add(new District(3, "KOHAT", 3, 1));

        districts.add(new District(1, "BUNER", 4, 1));
        districts.add(new District(2, "LOWER CHITRAL", 4, 1));
        districts.add(new District(3, "UPPER CHITRAL", 4, 1));
        districts.add(new District(4, "MALAKAND", 4, 1));
        districts.add(new District(5, "LOWER DIR", 4, 1));
        districts.add(new District(6, "SHANGLA", 4, 1));
        districts.add(new District(7, "SAWAT", 4, 1));
        districts.add(new District(8, "UPPER DIR", 4, 1));

        districts.add(new District(1, "MARDAN", 5, 1));
        districts.add(new District(2, "SWABI", 5, 1));

        districts.add(new District(1, "CHARSADDA", 6, 1));
        districts.add(new District(2, "NOSHEHRA", 6, 1));
        districts.add(new District(3, "PESHAWAR", 6, 1));

        database.districtDAO().insert(districts);

        return null;
    }
}
