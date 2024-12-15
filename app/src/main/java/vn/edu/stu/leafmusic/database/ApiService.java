package vn.edu.stu.leafmusic.database;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;

import retrofit2.http.POST;
import retrofit2.http.Path;
import vn.edu.stu.leafmusic.model.Song;

public interface ApiService {
    @GET("baihat")
    Call<List<Song>> getFavoriteSongs();

    @POST("baihat")
    Call<Void> saveFavoriteSong(@Body Song song);
}

