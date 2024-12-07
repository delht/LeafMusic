package vn.edu.stu.leafmusic.activity.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.activity.adapter.SongsAdapter;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.api.dto.ApiService;
import vn.edu.stu.leafmusic.model.Album;
import vn.edu.stu.leafmusic.model.Song;

public class AlbumDetailActivity extends AppCompatActivity {

    private ImageView albumImage;
    private TextView albumName, artistName;
    private RecyclerView recyclerViewSongs;
    private SongsAdapter songsAdapter;
    private List<Song> songsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        albumImage = findViewById(R.id.album_image);
        albumName = findViewById(R.id.album_name);
        artistName = findViewById(R.id.artist_name);
        recyclerViewSongs = findViewById(R.id.recycler_songs);


        Intent intent = getIntent();
        String albumNameText = intent.getStringExtra("albumName");
        String artistNameText = intent.getStringExtra("artistName");
        String albumImageUrl = intent.getStringExtra("albumImage");

        albumName.setText(albumNameText);
        artistName.setText(artistNameText);
        Glide.with(this).load(albumImageUrl).into(albumImage);

        String albumId = intent.getStringExtra("albumId");

        if (albumId == null) {
            Log.e("AlbumDetailActivity", "Album ID is null");
            Toast.makeText(this, "Lỗi: Không có albumId", Toast.LENGTH_SHORT).show();
            return;
        }
        loadSongsForAlbum(albumId);
    }

    private void loadSongsForAlbum(String albumId) {
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);
        apiService.getSongsByAlbum(albumId).enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Album album = response.body();

                    Log.d("AlbumResponse", "Album: " + album.toString());

                    // Hiển thị thông tin album
                    albumName.setText(album.getTenAlbum());
                    artistName.setText(album.getTenCaSi());
                    Glide.with(AlbumDetailActivity.this).load(album.getUrlHinh()).into(albumImage);

                    // Lấy danh sách bài hát từ album
                    List<Song> songList = album.getBaiHats();
                    setupSongsRecyclerView(songList);
                } else {
                    Toast.makeText(AlbumDetailActivity.this, "Không thể lấy dữ liệu bài hát", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                Toast.makeText(AlbumDetailActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ERR", t.getMessage());
            }
        });
    }





    private void setupSongsRecyclerView(List<Song> songs) {
        songsAdapter = new SongsAdapter(songs, new SongsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int idSong) {
                Toast.makeText(AlbumDetailActivity.this, "Clicked song with ID: " + idSong, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerViewSongs.setAdapter(songsAdapter);
    }
}

