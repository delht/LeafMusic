package vn.edu.stu.leafmusic.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoveLIst {
    @SerializedName("id_ds")
    private int idDs;
    @SerializedName("loai_ds")
    private String loaiDs;
    @SerializedName("ten_ds")
    private String tenDs;

    private List<Song> baiHats;

    public List<Song> getBaiHats() {
        return baiHats;
    }

    public void setBaiHats(List<Song> baiHats) {
        this.baiHats = baiHats;
    }

    public LoveLIst(int idDs, String loaiDs, String tenDs, List<Song> baiHats) {
        this.idDs = idDs;
        this.loaiDs = loaiDs;
        this.tenDs = tenDs;
        this.baiHats = baiHats;
    }

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

    public LoveLIst(int idDs, String loaiDs, String tenDs) {
        this.idDs = idDs;
        this.loaiDs = loaiDs;
        this.tenDs = tenDs;
    }

    public LoveLIst() {
    }

    public boolean isDefaultList() {
        return "macdinh".equalsIgnoreCase(loaiDs);
    }

}
