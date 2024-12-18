package vn.edu.stu.leafmusic.api.dto.request;

public class ChangePasswordRequest {
    private String idTaiKhoan;
    private String oldPassword;
    private String newPassword;

    public String getIdTaiKhoan() {
        return idTaiKhoan;
    }

    public void setIdTaiKhoan(String idTaiKhoan) {
        this.idTaiKhoan = idTaiKhoan;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public ChangePasswordRequest(String idTaiKhoan, String oldPassword, String newPassword) {
        this.idTaiKhoan = idTaiKhoan;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public ChangePasswordRequest() {
    }
}
