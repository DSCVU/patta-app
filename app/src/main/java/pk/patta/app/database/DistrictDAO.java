package pk.patta.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pk.patta.app.models.District;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DistrictDAO {
    @Insert(onConflict = REPLACE)
    void insert(District district);

    @Insert(onConflict = REPLACE)
    void insert(District... district);

    @Insert(onConflict = REPLACE)
    void insert(List<District> district);

    @Update
    void update(District district);

    @Delete
    void delete(District district);

    @Query("SELECT * FROM district")
    LiveData<List<District>> getDistrict();

    @Query("SELECT * FROM district WHERE provinceId=:provinceId AND divisionId=:divisionId")
    LiveData<List<District>> getDistrict(int provinceId, int divisionId);
}
