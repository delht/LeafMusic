package vn.edu.stu.leafmusic.activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.activity.adapter.LoveListAdapter;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.api.dto.ApiService;
import vn.edu.stu.leafmusic.model.LoveLIst;
import vn.edu.stu.leafmusic.util.SharedPrefsHelper;

public class LoveListFragment extends Fragment {

    private RecyclerView rvLoveList;
    private SharedPrefsHelper sharedPrefsHelper; // Biến thành viên
    private String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lovelist, container, false);

        // Khởi tạo SharedPrefsHelper
        sharedPrefsHelper = new SharedPrefsHelper(requireContext());
        id = sharedPrefsHelper.getUserId(); // Lấy ID sau khi khởi tạo

        rvLoveList = view.findViewById(R.id.rvLoveList);

        // Thiết lập RecyclerView
        rvLoveList.setLayoutManager(new LinearLayoutManager(getContext()));

        // Gọi API để lấy danh sách yêu thích
        fetchLoveList();

        return view;
    }

    private void fetchLoveList() {
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);

        apiService.getLoveList(id).enqueue(new Callback<List<LoveLIst>>() {
            @Override
            public void onResponse(@NonNull Call<List<LoveLIst>> call, @NonNull Response<List<LoveLIst>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LoveLIst> loveLists = response.body();
                    for (LoveLIst item : loveLists) {
                        Log.d("API Response", "ID: " + item.getIdDs() + ", Loại: " + item.getLoaiDs() + ", Tên: " + item.getTenDs());
                    }
                    LoveListAdapter adapter = new LoveListAdapter(getContext(), loveLists);
                    rvLoveList.setAdapter(adapter);
                } else {
                    Log.e("API Error", "Response body is null or not successful");
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LoveLIst>> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
