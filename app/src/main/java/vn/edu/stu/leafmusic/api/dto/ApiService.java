package vn.edu.stu.leafmusic.api.dto;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import vn.edu.stu.leafmusic.api.dto.reponse.LoginResponse;
import vn.edu.stu.leafmusic.api.dto.request.DsYeuThich_Request;
import vn.edu.stu.leafmusic.api.dto.request.LoginRequest;
import vn.edu.stu.leafmusic.api.dto.request.RegisterRequest;
import vn.edu.stu.leafmusic.api.dto.request.ThemBaiHat_DsMacDinh_Request;
import vn.edu.stu.leafmusic.model.Album;
import vn.edu.stu.leafmusic.model.Artist;
import vn.edu.stu.leafmusic.model.LoveLIst;
import vn.edu.stu.leafmusic.model.LoveList_Simple;
import vn.edu.stu.leafmusic.model.Song;
import vn.edu.stu.leafmusic.model.Song2;

public interface ApiService {

    @POST("api/taikhoan/dangnhap2")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("api/taikhoan/tao")
    Call<Void> register(@Body RegisterRequest request);

    @GET("api/album/all")
    Call<List<Album>> getAllAlbums();

    @GET("api/casi/all")
    Call<List<Artist>> getAllArtists();

//    @GET("api/baihat/all")
//    Call<List<Song>> getAllSongs();

    @GET("api/baihat/randombaihat5")
    Call<List<Song>> getRandom5Songs();


    @GET("/api/album/get/id={id}")
    Call<Album> getSongsByAlbum(@Path("id") String albumId);

//    =====================================================

//    @GET("api/casi/album/id={artistId}")
//    Call<Album> getAlbumById(@Path("albumId") String albumId);

    @GET("api/casi/baihat/id={artistId}")
    Call<List<Song>> getSongByArtistId(@Path("artistId") String songId);

    @GET("api/casi/album/id={artistId}")
    Call<List<Album>> getAlbumsByArtistId(@Path("artistId") String artistId);

//    ============================================

    @GET("api/dsyeuthich/custom/id={id}")
    Call<List<LoveLIst>> getLoveList(@Path("id") String id); //custom

    @GET("api/dsyeuthich/macdinh/id={id}")
    Call<List<LoveLIst>> getDefaultLoveList(@Path("id") String id); //mac dinh


    @GET("api/dsyeuthich/custom/id={id}")
    Call<List<LoveList_Simple>> getLoveListSimple(@Path("id") String id); //custom simple

//    ================================================

    @DELETE("api/dsyeuthich/delete/id={id}")
    Call<Void> deleteLoveList(@Path("id") int id);

    @PUT("api/dsyeuthich/update2/id={id}")
    Call<Void> renameLoveList(@Path("id") int id, @Body String newName);

    @POST("/api/dsyeuthich/create")
    Call<LoveLIst> createLoveList(@Body DsYeuThich_Request loveList);


//    ========================================================

    @GET("api/baihat/getBH/{id}")
    Call<Song2> getSongById(@Path("id") int id);

    @GET("/api/dsyeuthich/baihat/id={id}")
    Call<LoveLIst> getSongsByLoveList(@Path("id") String lovelistId);

    @GET("/api/dsyeuthich/baihat/id={id}")
    Call<List<Song>> getSongsByLoveList2(@Path("id") String lovelistId);

    @GET("/api/dsyeuthich/baihat/id={id}")
    Call<List<Song2>> getSongsByLoveList3(@Path("id") String lovelistId);

//    =============================================================

    @POST("/api/dsyeuthich/addMacDinh")
    Call<Void> addToFavorite(@Body ThemBaiHat_DsMacDinh_Request request);

    @DELETE("/api/dsyeuthich/removeMacDinh/{idDs}/{idBaihat}")
    Call<Void> deleteToFavorite(@Path("idDs") String idDs, @Path("idBaihat") String idBaihat);


//    =============================================================

    @POST("/api/dsyeuthich/addCustom/{idDs}/{idBaihat}")
    Call<Void> addSongToCustom(@Path("idDs") String idDs, @Path("idBaihat") String idBaihat);

    @DELETE("/api/dsyeuthich/removeCustom/{idDs}/{idBaihat}")
    Call<Void> remoteSongToCustom(@Path("idDs") String idDs, @Path("idBaihat") String idBaihat);



}
