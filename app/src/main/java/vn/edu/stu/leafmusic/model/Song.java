package vn.edu.stu.leafmusic.model;

public class Song {
    private String idBaiHat;
    private String tenBaiHat;
    private String urlHinh;

    public String getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(String idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getUrlHinh() {
        return urlHinh;
    }

    public void setUrlHinh(String urlHinh) {
        this.urlHinh = urlHinh;
    }

    public Song(String idBaiHat, String tenBaiHat, String urlHinh) {
        this.idBaiHat = idBaiHat;
        this.tenBaiHat = tenBaiHat;
        this.urlHinh = urlHinh;
    }

    public Song() {
    }
}
