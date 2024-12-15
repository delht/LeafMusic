package vn.edu.stu.leafmusic.model;

public class Artist {

    private String idCaSi;
    private String tenCaSi;
    private String urlHinh;

    public String getIdCaSi() {
        return idCaSi;
    }

    public void setIdCaSi(String idCaSi) {
        this.idCaSi = idCaSi;
    }

    public String getTenCaSi() {
        return tenCaSi;
    }

    public void setTenCaSi(String tenCaSi) {
        this.tenCaSi = tenCaSi;
    }

    public String getUrlHinh() {
        return urlHinh;
    }

    public void setUrlHinh(String urlHinh) {
        this.urlHinh = urlHinh;
    }

    public Artist(String idCaSi, String tenCaSi, String urlHinh) {
        this.idCaSi = idCaSi;
        this.tenCaSi = tenCaSi;
        this.urlHinh = urlHinh;
    }

    public Artist() {
    }
}
