package vn.edu.stu.leafmusic.model;

public class Album {

    private String idAlbum;
    private String tenAlbum;
    private String urlHinh;

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

    public String getUrlHinh() {
        return urlHinh;
    }

    public void setUrlHinh(String urlHinh) {
        this.urlHinh = urlHinh;
    }

    public Album(String idAlbum, String tenAlbum, String urlHinh) {
        this.idAlbum = idAlbum;
        this.tenAlbum = tenAlbum;
        this.urlHinh = urlHinh;
    }

    public Album() {
    }
}
