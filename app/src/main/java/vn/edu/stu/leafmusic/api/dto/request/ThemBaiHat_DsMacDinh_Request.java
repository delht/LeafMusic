package vn.edu.stu.leafmusic.api.dto.request;

import com.google.gson.annotations.SerializedName;

public class ThemBaiHat_DsMacDinh_Request {
    @SerializedName("idDs")
    private String idDs;

    @SerializedName("idBaihat")
    private String idBaihat;

    public String getIdDs() {
        return idDs;
    }

    public void setIdDs(String idDs) {
        this.idDs = idDs;
    }

    public String getIdBaihat() {
        return idBaihat;
    }

    public void setIdBaihat(String idBaihat) {
        this.idBaihat = idBaihat;
    }

    public ThemBaiHat_DsMacDinh_Request(String idDs, String idBaihat) {
        this.idDs = idDs;
        this.idBaihat = idBaihat;
    }

    public ThemBaiHat_DsMacDinh_Request() {
    }
}