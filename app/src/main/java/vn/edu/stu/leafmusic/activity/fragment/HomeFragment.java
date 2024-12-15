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
                loadSongDetails(idSong); // Gọi phương thức để lấy chi tiết bài hát
            }
        });

        recyclerRandomSongs.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerRandomSongs.setAdapter(adapter);
    }



    private void loadSongDetails(int idSong) {
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);

        apiService.getSongById(idSong).enqueue(new Callback<Song>() {
            @Override
            public void onResponse(Call<Song> call, Response<Song> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Song song = response.body();
                    // Chuyển dữ liệu vào Activity phát nhạc
                    Intent intent = new Intent(getContext(), Music_Player.class);
                    intent.putExtra("song", String.valueOf(song)); // Truyền đối tượng Song qua Intent
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(), "Không thể lấy dữ liệu bài hát", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Song> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HomeFragment", "Error: " + t.getMessage());
            }
        });
    }






    //    ===================================================================================================

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
