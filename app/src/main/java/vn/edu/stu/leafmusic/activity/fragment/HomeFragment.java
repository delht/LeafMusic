package vn.edu.stu.leafmusic.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.activity.UI.Music_Player;
import vn.edu.stu.leafmusic.activity.adapter.AlbumsAdapter;
import vn.edu.stu.leafmusic.activity.adapter.ArtistsAdapter;
import vn.edu.stu.leafmusic.activity.adapter.SongsAdapter;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.api.dto.ApiService;
import vn.edu.stu.leafmusic.model.Album;
import vn.edu.stu.leafmusic.model.Artist;
import vn.edu.stu.leafmusic.model.Song;
import vn.edu.stu.leafmusic.model.Song2;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerRandomSongs, recyclerAlbums, recyclerArtists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("HomeFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerRandomSongs = view.findViewById(R.id.recycler_random_songs);
        recyclerAlbums = view.findViewById(R.id.recycler_albums);
        recyclerArtists = view.findViewById(R.id.recycler_artists);


        loadSongs();
        loadAlbums();
        loadArtists();

        return view;
    }


    //    ===================================================================================================


    private void loadSongs() {
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);

        apiService.getRandom5Songs().enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Song> songs = response.body();
                    setupSongsRecyclerView(songs);
                } else {
                    Toast.makeText(getContext(), "Không thể lấy dữ liệu bài hát", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeFragment", "Error: " + t.getMessage());
            }
        });
    }

    private void setupSongsRecyclerView(List<Song> songs) {
        SongsAdapter adapter = new SongsAdapter(songs, new SongsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int idSong) {
                // Hiển thị thông báo về bài hát được chọn
                Toast.makeText(getContext(), "Clicked song with ID: " + idSong, Toast.LENGTH_SHORT).show();

                // Gọi API để lấy chi tiết bài hát
//                getSongDetails(idSong);
            }
        });

        recyclerRandomSongs.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerRandomSongs.setAdapter(adapter);
    }


//    private void getSongDetails(int songId) {
//        ApiClient.getApiService().getSongById(songId).enqueue(new Callback<Song2>() {
//            @Override
//            public void onResponse(Call<Song2> call, Response<Song2> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    // Lấy dữ liệu bài hát từ API
//                    Song2 song = response.body();
//
//
//                    Log.d("Song Details", "ID: " + song.getIdBaiHat());
//                    Log.d("Song Details", "Tên bài hát: " + song.getTenBaiHat());
//                    Log.d("Song Details", "Ca sĩ: " + song.getCaSi());
//                    Log.d("Song Details", "Thể loại: " + song.getTheLoai());
//                    Log.d("Song Details", "Album: " + song.getAlbum());
//                    Log.d("Song Details", "Khu vực nhạc: " + song.getKhuVucNhac());
//                    Log.d("Song Details", "URL hình: " + song.getUrlHinh());
//                    Log.d("Song Details", "URL file: " + song.getUrlFile());
//                    Log.d("Song Details", "Ngày phát hành: " + song.getFormattedReleaseDate());
//
//
//                    Intent intent = new Intent(getContext(), Music_Player.class);
//                    intent.putExtra("song_name", song.getTenBaiHat());
//                    intent.putExtra("artist", song.getCaSi());
//                    intent.putExtra("song_url", song.getUrlFile());
//                    intent.putExtra("image_url", song.getUrlHinh());
//                    intent.putExtra("song_id", song.getIdBaiHat());
//                    intent.putExtra("album", song.getAlbum());
//                    intent.putExtra("genre", song.getTheLoai());
//                    intent.putExtra("region", song.getKhuVucNhac());
//                    intent.putExtra("release_date", song.getFormattedReleaseDate());
//
//
//                    ArrayList<Song2> playlist = new ArrayList<>();
//                    playlist.add(song);
//                    intent.putParcelableArrayListExtra("playlist", playlist);
//                    intent.putExtra("position", 0);
//
//                    startActivity(intent);
//                } else {
//                    Log.e("Error", "Failed to get song details");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Song2> call, Throwable t) {
//                Log.e("Error", "API call failed: " + t.getMessage());
//            }
//        });
//    }




//        ===================================================================================================

    private void loadAlbums() {
        // Khởi tạo Retrofit API
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);

        // Gọi API lấy danh sách album
        apiService.getAllAlbums().enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Album> albums = response.body();
                    // Thiết lập adapter cho RecyclerView
                    setupAlbumsRecyclerView(albums);
                } else {
                    Toast.makeText(getContext(), "Không thể lấy dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeFragment", "Error: " + t.getMessage());
            }
        });
    }

    private void setupAlbumsRecyclerView(List<Album> albums) {
        // Thiết lập adapter và layout manager cho RecyclerView
        AlbumsAdapter adapter = new AlbumsAdapter(albums, new AlbumsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int idAlbum) {
                // Xử lý sự kiện click và sử dụng idAlbum
                Toast.makeText(getContext(), "Clicked album with ID: " + idAlbum, Toast.LENGTH_SHORT).show();
                // Bạn có thể mở một màn hình chi tiết album hoặc làm gì đó với idAlbum
            }
        });

        recyclerAlbums.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerAlbums.setAdapter(adapter);
    }

//    ===================================================================================================

    private void loadArtists() {

        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);


        apiService.getAllArtists().enqueue(new Callback<List<Artist>>() {
            @Override
            public void onResponse(Call<List<Artist>> call, Response<List<Artist>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Artist> artists = response.body();

                    setupArtistsRecyclerView(artists);
                } else {
                    Toast.makeText(getContext(), "Không thể lấy dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Artist>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeFragment", "Error: " + t.getMessage());
            }
        });
    }

    private void setupArtistsRecyclerView(List<Artist> artists) {
        ArtistsAdapter adapter = new ArtistsAdapter(artists, new ArtistsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int idArtist) {
                Toast.makeText(getContext(), "Clicked artist with ID: " + idArtist, Toast.LENGTH_SHORT).show();
            }
        });

        recyclerArtists.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerArtists.setAdapter(adapter);
    }


//    ===================================================================================


}
