package pk.patta.app.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "division")
public class Division {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int divisionId;
    private String division;
    private int provinceId;

    @Ignore
    public Division(int divisionId, String division, int provinceId) {
        this.divisionId = divisionId;
        this.division = division;
        this.provinceId = provinceId;
    }

    public Division(int id, int divisionId, String division, int provinceId) {
        this.id = id;
        this.divisionId = divisionId;
        this.division = division;
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public String getDivision() {
        return division;
    }

    public void setDivision(String division) {
        this.division = division;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
