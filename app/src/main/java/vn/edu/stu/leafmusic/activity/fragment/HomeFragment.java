package vn.edu.stu.leafmusic.activity.fragment;

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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.activity.adapter.AlbumsAdapter;
import vn.edu.stu.leafmusic.activity.adapter.ArtistsAdapter;
import vn.edu.stu.leafmusic.activity.adapter.SongsAdapter;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.api.dto.ApiService;
import vn.edu.stu.leafmusic.model.Album;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerRandomSongs, recyclerAlbums, recyclerArtists;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("HomeFragment", "onCreateView called");
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Khởi tạo RecyclerView
        recyclerRandomSongs = view.findViewById(R.id.recycler_random_songs);
        recyclerAlbums = view.findViewById(R.id.recycler_albums);
        recyclerArtists = view.findViewById(R.id.recycler_artists);

        // Thiết lập các RecyclerView
        setupRandomSongsRecyclerView();
        setupArtistsRecyclerView();
        loadAlbums();  // Gọi loadAlbums để lấy dữ liệu album

        return view;
    }

    private void setupRandomSongsRecyclerView() {
        // Dummy data
        List<String> randomSongs = Arrays.asList("Song 1", "Song 2", "Song 3", "Song 4", "Song 5");

        // Adapter
        SongsAdapter adapter = new SongsAdapter(randomSongs);

        // Layout Manager
        recyclerRandomSongs.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerRandomSongs.setAdapter(adapter);
    }

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


    private void setupArtistsRecyclerView() {
        // Dummy data
        List<String> artists = Arrays.asList("Artist 1", "Artist 2", "Artist 3");

        // Adapter
        ArtistsAdapter adapter = new ArtistsAdapter(artists);

        // Layout Manager
        recyclerArtists.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerArtists.setAdapter(adapter);
    }
}
