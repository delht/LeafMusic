package vn.edu.stu.leafmusic.api.dto.request;

public class ResetPasswordRequest {
    private String code;
    private String newPassword;

    public ResetPasswordRequest() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public ResetPasswordRequest(String code, String newPassword) {
        this.code = code;
        this.newPassword = newPassword;
    }
}
