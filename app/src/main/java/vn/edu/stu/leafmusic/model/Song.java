package vn.edu.stu.leafmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {
    private int idBaiHat;
    private String tenBaiHat;
    private String caSi;
    private String ngayPhatHanh;
    private String urlHinh;
    private String urlFile;
    private String album;
    private String genre;

    public Song() {
    }

    public Song(int idBaiHat, String tenBaiHat, String caSi, String ngayPhatHanh, String urlHinh, String urlFile, String album, String genre) {
        this.idBaiHat = idBaiHat;
        this.tenBaiHat = tenBaiHat;
        this.caSi = caSi;
        this.ngayPhatHanh = ngayPhatHanh;
        this.urlHinh = urlHinh;
        this.urlFile = urlFile;
        this.album = album;
        this.genre = genre;
    }

    // Constructor cho Parcelable
    protected Song(Parcel in) {
        idBaiHat = in.readInt();
        tenBaiHat = in.readString();
        caSi = in.readString();
        ngayPhatHanh = in.readString();
        urlHinh = in.readString();
        urlFile = in.readString();
        album = in.readString();
        genre = in.readString();
    }

    // Implement Parcelable
    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idBaiHat);
        dest.writeString(tenBaiHat);
        dest.writeString(caSi);
        dest.writeString(ngayPhatHanh);
        dest.writeString(urlHinh);
        dest.writeString(urlFile);
        dest.writeString(album);
        dest.writeString(genre);
    }

    // Getters và Setters
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

    public String getCaSi() {
        return caSi;
    }

    public void setCaSi(String caSi) {
        this.caSi = caSi;
    }

    public String getNgayPhatHanh() {
        return ngayPhatHanh;
    }

    public void setNgayPhatHanh(String ngayPhatHanh) {
        this.ngayPhatHanh = ngayPhatHanh;
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

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Song{" +
                "idBaiHat=" + idBaiHat +
                ", tenBaiHat='" + tenBaiHat + '\'' +
                ", caSi='" + caSi + '\'' +
                ", ngayPhatHanh='" + ngayPhatHanh + '\'' +
                ", urlHinh='" + urlHinh + '\'' +
                ", urlFile='" + urlFile + '\'' +
                ", album='" + album + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
} 