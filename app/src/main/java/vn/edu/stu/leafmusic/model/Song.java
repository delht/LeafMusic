package vn.edu.stu.leafmusic.model;

import java.util.List;

public class Song {

    private int idBaiHat;  // idBaiHat là kiểu int
    private String tenBaiHat;
    private String theLoai;
    private String khuVucNhac;
    private String urlHinh;
    private String urlFile;
    private List<Integer> ngayPhatHanh;  // Ngày phát hành là List<Integer>

    // Getter & Setter
    public int getIdBaiHat() {
        return idBaiHat;
    }

    public void setIdBaiHat(int idBaiHat) {
        this.idBaiHat = idBaiHat;
    }

    public String getTenBaiHat() {
        return tenBaiHat;
    }

    public void setTenBaiHat(String tenBaiHat) {
        this.tenBaiHat = tenBaiHat;
    }

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getKhuVucNhac() {
        return khuVucNhac;
    }

    public void setKhuVucNhac(String khuVucNhac) {
        this.khuVucNhac = khuVucNhac;
    }

    public String getUrlHinh() {
        return urlHinh;
    }

    public void setUrlHinh(String urlHinh) {
        this.urlHinh = urlHinh;
    }

    public String getUrlFile() {
        return urlFile;
    }

    public void setUrlFile(String urlFile) {
        this.urlFile = urlFile;
    }

    public List<Integer> getNgayPhatHanh() {
        return ngayPhatHanh;
    }

    public void setNgayPhatHanh(List<Integer> ngayPhatHanh) {
        this.ngayPhatHanh = ngayPhatHanh;
    }
}
