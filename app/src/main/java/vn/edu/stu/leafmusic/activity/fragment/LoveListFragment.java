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

import java.util.ArrayList;
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
    private SharedPrefsHelper sharedPrefsHelper;
    private String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lovelist, container, false);

        sharedPrefsHelper = new SharedPrefsHelper(requireContext());
        id = sharedPrefsHelper.getUserId();

        rvLoveList = view.findViewById(R.id.rvLoveList);
        rvLoveList.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchLoveLists();
        return view;
    }

    private void fetchLoveLists() {
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);

        List<LoveLIst> combinedList = new ArrayList<>();

        // Fetch Default Love List
        apiService.getDefaultLoveList(id).enqueue(new Callback<List<LoveLIst>>() {
            @Override
            public void onResponse(@NonNull Call<List<LoveLIst>> call, @NonNull Response<List<LoveLIst>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    combinedList.addAll(response.body());
                }

                // Fetch Custom Love List
                apiService.getLoveList(id).enqueue(new Callback<List<LoveLIst>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<LoveLIst>> call, @NonNull Response<List<LoveLIst>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            combinedList.addAll(response.body());
                        }

                        // Update RecyclerView
                        LoveListAdapter adapter = new LoveListAdapter(getContext(), combinedList);
                        rvLoveList.setAdapter(adapter);
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<LoveLIst>> call, @NonNull Throwable t) {
                        Log.e("API Error", "Failed to fetch custom love list", t);
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<List<LoveLIst>> call, @NonNull Throwable t) {
                Log.e("API Error", "Failed to fetch default love list", t);
            }
        });
    }
}
