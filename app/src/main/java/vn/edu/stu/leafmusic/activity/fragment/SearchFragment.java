package vn.edu.stu.leafmusic.activity.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView; // cua androix


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.activity.adapter.SongsAdapter;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private SongsAdapter songsAdapter;
    private List<Song> songList = new ArrayList<>();


    TextView ThongBao;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        searchView = rootView.findViewById(R.id.search_view);
        recyclerView = rootView.findViewById(R.id.rv_search_results);

        ThongBao = rootView.findViewById(R.id.ThongBao);

        
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        songsAdapter = new SongsAdapter(songList, this::onSongClicked);
        recyclerView.setAdapter(songsAdapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query)) {
                    fetchSongs(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    songList.clear(); // Xóa danh sách khi không có tìm kiếm
                    songsAdapter.notifyDataSetChanged();
                }
                return true;
            }
        });

        return rootView;
    }


    private void fetchSongs(String query) {
        ApiClient.getApiService().searchSongs(query).enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    ThongBao.setVisibility(View.GONE); // Ẩn thông báo nếu có kết quả
                    songList.clear();
                    songList.addAll(response.body());
                    songsAdapter.notifyDataSetChanged();
                } else {
                    songList.clear();
                    songsAdapter.notifyDataSetChanged();
                    ThongBao.setVisibility(View.VISIBLE);
                    ThongBao.setText("Không có bài hát cần tìm");
                    Log.e("SearchFragment", "Ko co bai hat.");
                }
            }


            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.e("SearchFragment", "Loi API: " + t.getMessage());
            }
        });
    }

    private void onSongClicked(int songId) {
        //noi xu ly bai hat neu bam vao
    }
}
