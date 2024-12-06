package vn.edu.stu.leafmusic.api.dto.reponse;

public class LoginResponse {
    private String id_taikhoan;
    private String username;

    public String getId_taikhoan() {
        return id_taikhoan;
    }

    public void setId_taikhoan(String id_taikhoan) {
        this.id_taikhoan = id_taikhoan;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LoginResponse(String username, String id_taikhoan) {
        this.username = username;
        this.id_taikhoan = id_taikhoan;
    }

    public LoginResponse() {
    }
}
