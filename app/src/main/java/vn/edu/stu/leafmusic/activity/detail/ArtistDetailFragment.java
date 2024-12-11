package vn.edu.stu.leafmusic.activity.detail;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.activity.adapter.AlbumsAdapter;
import vn.edu.stu.leafmusic.activity.adapter.SongsAdapter;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.api.dto.ApiService;
import vn.edu.stu.leafmusic.model.Album;
import vn.edu.stu.leafmusic.model.Song;

public class ArtistDetailFragment extends Fragment {

    private ImageView artistImage;
    private TextView artistName;

    private RecyclerView recyclerViewSongs;
    private SongsAdapter songsAdapter;
    private List<Song> songsList;

    private RecyclerView recyclerViewAlbums;
    private AlbumsAdapter albumsAdapter;  // Đổi thành AlbumsAdapter
    private List<Album> albumsList;

    public static ArtistDetailFragment newInstance(String artistId, String artistName, String artistImageUrl) {
        ArtistDetailFragment fragment = new ArtistDetailFragment();
        Bundle args = new Bundle();
        args.putString("artistId", artistId);
        args.putString("artistName", artistName);
        args.putString("artistImage", artistImageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_artist_detail, container, false);

        artistImage = rootView.findViewById(R.id.artist_image);
        artistName = rootView.findViewById(R.id.artist_name);

        recyclerViewSongs = rootView.findViewById(R.id.recycler_songs);
        recyclerViewAlbums = rootView.findViewById(R.id.recycler_albums);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String artistNameText = arguments.getString("artistName");
            String artistmageUrl = arguments.getString("artistImage");

            artistName.setText(artistNameText);
            Glide.with(getActivity()).load(artistmageUrl).into(artistImage);

            String artistId = arguments.getString("artistId");

            if (artistId != null) {
                loadAlbumsForArtist(artistId);
                loadSongs(); // Gọi loadSongs ở đây để tải bài hát
            } else {
                Log.e("ArtistDetailFragment", "Artist ID is null");
                Toast.makeText(getActivity(), "Lỗi: Không có artistId", Toast.LENGTH_SHORT).show();
            }
        }

        return rootView;
    }

    private void loadAlbumsForArtist(String artistId) {
        // Khởi tạo Retrofit API
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);

        // Gọi API lấy album của nghệ sĩ
        apiService.getAlbumsByArtistId(artistId).enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Album> albums = response.body();
                    // Thiết lập adapter cho RecyclerView
                    setupAlbumsRecyclerView(albums);
                } else {
                    Toast.makeText(getContext(), "Không thể lấy dữ liệu album", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ArtistDetailFragment", "Error: " + t.getMessage());
            }
        });
    }

    private void setupAlbumsRecyclerView(List<Album> albums) {
        // Thiết lập adapter và layout manager cho RecyclerView
        albumsAdapter = new AlbumsAdapter(albums, new AlbumsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int albumId) {
                // Xử lý sự kiện click và sử dụng albumId
                Toast.makeText(getContext(), "Clicked album with ID: " + albumId, Toast.LENGTH_SHORT).show();
                // Bạn có thể mở một màn hình chi tiết album hoặc làm gì đó với albumId
            }
        });

        recyclerViewAlbums.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerViewAlbums.setAdapter(albumsAdapter);
    }

//    =============================================================================

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
                Toast.makeText(getContext(), "Clicked song with ID: " + idSong, Toast.LENGTH_SHORT).show();

                // Gọi API để lấy thông tin chi tiết bài hát khi người dùng nhấn vào bài hát
                ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);
                apiService.getSongByArtistId(String.valueOf(idSong)).enqueue(new Callback<Song>() {
                    @Override
                    public void onResponse(Call<Song> call, Response<Song> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Song songDetails = response.body();
                            // Xử lý songDetails (ví dụ, hiển thị chi tiết bài hát)
                            Toast.makeText(getContext(), "Song details: " + songDetails.getTenBaiHat(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Không thể lấy thông tin bài hát", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Song> call, Throwable t) {
                        Toast.makeText(getContext(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("SongDetails", "Error: " + t.getMessage());
                    }
                });
            }
        });

        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerViewSongs.setAdapter(adapter);
    }






}
