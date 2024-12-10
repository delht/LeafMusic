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
import vn.edu.stu.leafmusic.activity.adapter.SongsAdapter;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.api.dto.ApiService;
import vn.edu.stu.leafmusic.model.Album;
import vn.edu.stu.leafmusic.model.Song;

public class AlbumDetailFragment extends Fragment {

    private ImageView albumImage;
    private TextView albumName, artistName;
    private RecyclerView recyclerViewSongs;
    private SongsAdapter songsAdapter;
    private List<Song> songsList;

    public static AlbumDetailFragment newInstance(String albumId, String albumName, String artistName, String albumImageUrl) {
        AlbumDetailFragment fragment = new AlbumDetailFragment();
        Bundle args = new Bundle();
        args.putString("albumId", albumId);
        args.putString("albumName", albumName);
        args.putString("artistName", artistName);
        args.putString("albumImage", albumImageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_album_detail, container, false);

        albumImage = rootView.findViewById(R.id.album_image);
        albumName = rootView.findViewById(R.id.album_name);
        artistName = rootView.findViewById(R.id.artist_name);
        recyclerViewSongs = rootView.findViewById(R.id.recycler_songs);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String albumNameText = arguments.getString("albumName");
            String artistNameText = arguments.getString("artistName");
            String albumImageUrl = arguments.getString("albumImage");

            albumName.setText(albumNameText);
            artistName.setText(artistNameText);
            Glide.with(getActivity()).load(albumImageUrl).into(albumImage);

            String albumId = arguments.getString("albumId");

            if (albumId != null) {
                loadSongsForAlbum(albumId);
            } else {
                Log.e("AlbumDetailFragment", "Album ID is null");
                Toast.makeText(getActivity(), "Lỗi: Không có albumId", Toast.LENGTH_SHORT).show();
            }
        }

        return rootView;
    }

    private void loadSongsForAlbum(String albumId) {
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);
        apiService.getSongsByAlbum(albumId).enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Album album = response.body();
                    albumName.setText(album.getTenAlbum());
                    artistName.setText(album.getTenCaSi());
                    Glide.with(getActivity()).load(album.getUrlHinh()).into(albumImage);

                    List<Song> songList = album.getBaiHats();
                    setupSongsRecyclerView(songList);
                } else {
                    Toast.makeText(getActivity(), "Không thể lấy dữ liệu bài hát", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("ERR", t.getMessage());
            }
        });
    }

    private void setupSongsRecyclerView(List<Song> songs) {
        songsAdapter = new SongsAdapter(songs, new SongsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int idSong) {
                Toast.makeText(getActivity(), "Clicked song with ID: " + idSong, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewSongs.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerViewSongs.setAdapter(songsAdapter);
    }
}
