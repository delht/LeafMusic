package vn.edu.stu.leafmusic.model;

import java.util.List;

public class Artist {
    private int idCaSi;
    private String tenCaSi;
    private String urlHinh;
    private List<Song> baiHats;

    public Artist(int idCaSi, String tenCaSi, String urlHinh, List<Song> baiHats) {
        this.idCaSi = idCaSi;
        this.tenCaSi = tenCaSi;
        this.urlHinh = urlHinh;
        this.baiHats = baiHats;
    }

    // Getters và Setters
    public int getIdCaSi() {
        return idCaSi;
    }

    public String getTenCaSi() {
        return tenCaSi;
    }

    public String getUrlHinh() {
        return urlHinh;
    }

    public List<Song> getBaiHats() {
        return baiHats;
    }
} 