package pk.patta.app.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import pk.patta.app.models.Division;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface DivisionDAO {

    @Insert(onConflict = REPLACE)
    void insert(Division division);

    @Insert(onConflict = REPLACE)
    void insert(Division... division);

    @Insert(onConflict = REPLACE)
    void insert(List<Division> division);

    @Update
    void update(Division division);

    @Delete
    void delete(Division division);

    @Query("SELECT * FROM division")
    LiveData<List<Division>> getDivision();

    @Query("SELECT * FROM division WHERE provinceId=:provinceId")
    LiveData<List<Division>> getDivision(int provinceId);
}
