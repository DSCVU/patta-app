package pk.patta.app.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "province")
public class Province {

    @PrimaryKey
    private int id;
    private String province;

    public Province(int id, String province) {
        this.id = id;
        this.province = province;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
