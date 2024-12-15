package vn.edu.stu.leafmusic.activity.detail;

import android.os.Bundle;
import android.os.LocaleList;
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
import vn.edu.stu.leafmusic.model.LoveLIst;
import vn.edu.stu.leafmusic.model.Song;

public class LoveListDetailFragment extends Fragment {

    TextView nameLoveList;
    private RecyclerView recyclerViewSongs;
    private SongsAdapter songsAdapter;

    public static LoveListDetailFragment newInstance(String idDs, String loaiDs, String tenDs) {
        LoveListDetailFragment fragment = new LoveListDetailFragment();
        Bundle args = new Bundle();
        args.putString("idDs", idDs);
        args.putString("loaiDs", loaiDs);
        args.putString("tenDs", tenDs);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_love_list_detail, container, false);

        nameLoveList = rootView.findViewById(R.id.name_lovelist);
        recyclerViewSongs = rootView.findViewById(R.id.recycler_songs);

        Bundle arguments = getArguments();
        if (arguments != null) {
            String loveListname = arguments.getString("tenDs");
            nameLoveList.setText(loveListname);

            String loveListId = arguments.getString("idDs");

            if (loveListId != null) {
                loadSongsForLoveList(loveListId);
            } else {
                Log.e("LoveListDetailFragment", "LoveList ID is null");
                Toast.makeText(getActivity(), "Lỗi: Không có loveListId", Toast.LENGTH_SHORT).show();
            }
        }

        return rootView;
    }


    private void loadSongsForLoveList(String lovelistId) {
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);

        apiService.getSongsByLoveList2(lovelistId).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Song> songList = response.body();

                    setupSongsRecyclerView(songList);
                } else {
                    Toast.makeText(getActivity(), "Không thể lấy dữ liệu bài hát", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
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
