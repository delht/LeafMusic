package vn.edu.stu.leafmusic.api.dto;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import vn.edu.stu.leafmusic.api.dto.reponse.LoginResponse;
import vn.edu.stu.leafmusic.api.dto.request.LoginRequest;

public interface ApiService {

    @POST("api/taikhoan/dangnhap2")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

}
