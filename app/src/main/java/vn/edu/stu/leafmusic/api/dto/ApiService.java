package vn.edu.stu.leafmusic.api.dto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import vn.edu.stu.leafmusic.api.dto.reponse.LoginResponse;
import vn.edu.stu.leafmusic.api.dto.request.LoginRequest;
import vn.edu.stu.leafmusic.api.dto.request.RegisterRequest;
import vn.edu.stu.leafmusic.model.Album;

public interface ApiService {

    @POST("api/taikhoan/dangnhap2")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/taikhoan/tao")
    Call<Void> register(@Body RegisterRequest request);

    @GET("api/album/all")
    Call<List<Album>> getAllAlbums();
}
