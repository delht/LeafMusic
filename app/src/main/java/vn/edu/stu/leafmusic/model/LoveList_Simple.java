package vn.edu.stu.leafmusic.model;

import com.google.gson.annotations.SerializedName;

public class LoveList_Simple {

    @SerializedName("id_ds")
    private int idDs;

    @SerializedName("loai_ds")
    private String loaiDs;

    @SerializedName("ten_ds")
    private String tenDs;

    public int getIdDs() {
        return idDs;
    }

    public void setIdDs(int idDs) {
        this.idDs = idDs;
    }

    public String getLoaiDs() {
        return loaiDs;
    }

    public void setLoaiDs(String loaiDs) {
        this.loaiDs = loaiDs;
    }

    public String getTenDs() {
        return tenDs;
    }

    public void setTenDs(String tenDs) {
        this.tenDs = tenDs;
    }

    public LoveList_Simple(int idDs, String loaiDs, String tenDs) {
        this.idDs = idDs;
        this.loaiDs = loaiDs;
        this.tenDs = tenDs;
    }

    public LoveList_Simple() {
    }

    @Override
    public String toString() {
        return tenDs;
    }
}
