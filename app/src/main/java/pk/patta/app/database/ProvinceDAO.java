package pk.patta.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pk.patta.app.models.Province;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProvinceDAO {

    @Insert(onConflict = REPLACE)
    void insert(Province province);

    @Insert(onConflict = REPLACE)
    void insert(Province... province);

    @Insert(onConflict = REPLACE)
    void insert(List<Province> province);

    @Update
    void update(Province province);

    @Delete
    void delete(Province province);

    @Query("SELECT * FROM province")
    LiveData<List<Province>> getProvince();
}
