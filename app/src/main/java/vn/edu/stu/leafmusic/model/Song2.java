package vn.edu.stu.leafmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Song2 implements Parcelable {
    private int idBaiHat;
    private String tenBaiHat;
    private String caSi;
    private String theLoai;
    private String album;
    private String khuVucNhac;
    private String urlHinh;
    private String urlFile;
    private int[] ngayPhatHanh;

    public Song2() {
    }

    public Song2(int idBaiHat, String tenBaiHat, String caSi, String theLoai, String album, String khuVucNhac, String urlHinh, String urlFile, int[] ngayPhatHanh) {
        this.idBaiHat = idBaiHat;
        this.tenBaiHat = tenBaiHat;
        this.caSi = caSi;
        this.theLoai = theLoai;
        this.album = album;
        this.khuVucNhac = khuVucNhac;
        this.urlHinh = urlHinh;
        this.urlFile = urlFile;
        this.ngayPhatHanh = ngayPhatHanh;
    }

    // Constructor cho Parcelable
    protected Song2(Parcel in) {
        idBaiHat = in.readInt();
        tenBaiHat = in.readString();
        caSi = in.readString();
        theLoai = in.readString();
        album = in.readString();
        khuVucNhac = in.readString();
        urlHinh = in.readString();
        urlFile = in.readString();
        ngayPhatHanh = in.createIntArray();
    }

    // Implement Parcelable
    public static final Creator<Song2> CREATOR = new Creator<Song2>() {
        @Override
        public Song2 createFromParcel(Parcel in) {
            return new Song2(in);
        }

        @Override
        public Song2[] newArray(int size) {
            return new Song2[size];
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
        dest.writeString(theLoai);
        dest.writeString(album);
        dest.writeString(khuVucNhac);
        dest.writeString(urlHinh);
        dest.writeString(urlFile);
        dest.writeIntArray(ngayPhatHanh);
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

    public String getTheLoai() {
        return theLoai;
    }

    public void setTheLoai(String theLoai) {
        this.theLoai = theLoai;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
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

    public int[] getNgayPhatHanh() {
        return ngayPhatHanh;
    }

    public void setNgayPhatHanh(int[] ngayPhatHanh) {
        this.ngayPhatHanh = ngayPhatHanh;
    }

    // Thêm phương thức để chuyển đổi ngày phát hành thành chuỗi
    public String getFormattedReleaseDate() {
        if (ngayPhatHanh != null && ngayPhatHanh.length >= 3) {
            return ngayPhatHanh[0] + "-" + ngayPhatHanh[1] + "-" + ngayPhatHanh[2]; // Định dạng: YYYY-MM-DD
        }
        return "Không có thông tin"; // Trả về thông báo nếu không có dữ liệu
    }

    @Override
    public String toString() {
        return "Song{" +
                "idBaiHat=" + idBaiHat +
                ", tenBaiHat='" + tenBaiHat + '\'' +
                ", caSi='" + caSi + '\'' +
                ", theLoai='" + theLoai + '\'' +
                ", album='" + album + '\'' +
                ", khuVucNhac='" + khuVucNhac + '\'' +
                ", urlHinh='" + urlHinh + '\'' +
                ", urlFile='" + urlFile + '\'' +
                ", ngayPhatHanh=" + Arrays.toString(ngayPhatHanh) +
                '}';
    }
} 