package vn.edu.stu.leafmusic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Artist implements Parcelable {
    private int idCaSi;
    private String tenCaSi;
    private String urlHinh;
    private List<Song> baiHats;

    public Artist() {
    }

    protected Artist(Parcel in) {
        idCaSi = in.readInt();
        tenCaSi = in.readString();
        urlHinh = in.readString();
        baiHats = in.createTypedArrayList(Song.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idCaSi);
        dest.writeString(tenCaSi);
        dest.writeString(urlHinh);
        dest.writeTypedList(baiHats);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Artist> CREATOR = new Creator<Artist>() {
        @Override
        public Artist createFromParcel(Parcel in) {
            return new Artist(in);
        }

        @Override
        public Artist[] newArray(int size) {
            return new Artist[size];
        }
    };

    // Getters và Setters
    public int getIdCaSi() {
        return idCaSi;
    }

    public void setIdCaSi(int idCaSi) {
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

    public List<Song> getBaiHats() {
        return baiHats;
    }

    public void setBaiHats(List<Song> baiHats) {
        this.baiHats = baiHats;
    }
} 