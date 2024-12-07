package vn.edu.stu.leafmusic.model;

import java.util.List;

public class Album {

    private String idAlbum;  // idAlbum là kiểu int
    private String tenAlbum;
    private String tenCaSi;
    private String idCaSi;
    private String urlHinh;
    private List<Integer> ngayPhatHanh;  // Ngày phát hành là List<Integer>
    private List<Song> baiHats;

    // Getter & Setter
    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getTenAlbum() {
        return tenAlbum;
    }

    public void setTenAlbum(String tenAlbum) {
        this.tenAlbum = tenAlbum;
    }

    public String getTenCaSi() {
        return tenCaSi;
    }

    public void setTenCaSi(String tenCaSi) {
        this.tenCaSi = tenCaSi;
    }

    public String getIdCaSi() {
        return idCaSi;
    }

    public void setIdCaSi(String idCaSi) {
        this.idCaSi = idCaSi;
    }

    public String getUrlHinh() {
        return urlHinh;
    }

    public void setUrlHinh(String urlHinh) {
        this.urlHinh = urlHinh;
    }

    public List<Integer> getNgayPhatHanh() {
        return ngayPhatHanh;
    }

    public void setNgayPhatHanh(List<Integer> ngayPhatHanh) {
        this.ngayPhatHanh = ngayPhatHanh;
    }

    public List<Song> getBaiHats() {
        return baiHats;
    }

    public void setBaiHats(List<Song> baiHats) {
        this.baiHats = baiHats;
    }
}
