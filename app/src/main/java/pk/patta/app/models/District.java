package pk.patta.app.models;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "district")
public class District {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private int districtId;
    private String district;
    private int divisionId;
    private int provinceId;

    @Ignore
    public District(int districtId, String district, int divisionId, int provinceId) {
        this.districtId = districtId;
        this.district = district;
        this.divisionId = divisionId;
        this.provinceId = provinceId;
    }

    public District(int id, int districtId, String district, int divisionId, int provinceId) {
        this.id = id;
        this.districtId = districtId;
        this.district = district;
        this.divisionId = divisionId;
        this.provinceId = provinceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDistrictId() {
        return districtId;
    }

    public void setDistrictId(int districtId) {
        this.districtId = districtId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public int getDivisionId() {
        return divisionId;
    }

    public void setDivisionId(int divisionId) {
        this.divisionId = divisionId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }
}
