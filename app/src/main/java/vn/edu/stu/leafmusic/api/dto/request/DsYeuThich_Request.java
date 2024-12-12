package vn.edu.stu.leafmusic.api.dto.request;

public class DsYeuThich_Request {

    private String tenDs;
    private int idTaiKhoan;

    public String getTenDs() {
        return tenDs;
    }

    public void setTenDs(String tenDs) {
        this.tenDs = tenDs;
    }

    public int getIdTaiKhoan() {
        return idTaiKhoan;
    }

    public void setIdTaiKhoan(int idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    public DsYeuThich_Request(String tenDs, int idTaiKhoan) {
        this.tenDs = tenDs;
        this.idTaiKhoan = idTaiKhoan;
    }

    public DsYeuThich_Request() {
    }
}
