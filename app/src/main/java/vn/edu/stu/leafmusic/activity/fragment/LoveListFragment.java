package vn.edu.stu.leafmusic.activity.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
import vn.edu.stu.leafmusic.api.dto.request.DsYeuThich_Request;
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

        // Tạo button "Thêm"
        Button addButton = view.findViewById(R.id.btnThem);
        addButton.setOnClickListener(v -> showAddLoveListDialog());

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

    private void showAddLoveListDialog() {
        final EditText input = new EditText(getContext());
        input.setHint("Nhập tên danh sách yêu thích");

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Thêm danh sách yêu thích")
                .setView(input)
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String tenDs = input.getText().toString().trim();
                        if (!tenDs.isEmpty()) {
                            addLoveList(tenDs);
                        } else {
                            Toast.makeText(getContext(), "Vui lòng nhập tên danh sách yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void addLoveList(String tenDs) {
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);
        
        String idTaiKhoan = sharedPrefsHelper.getUserId();


        DsYeuThich_Request request = new DsYeuThich_Request();
        request.setTenDs(tenDs);
        request.setIdTaiKhoan(Integer.parseInt(idTaiKhoan));

        // Gửi yêu cầu POST
        apiService.createLoveList(request).enqueue(new Callback<LoveLIst>() {
            @Override
            public void onResponse(@NonNull Call<LoveLIst> call, @NonNull Response<LoveLIst> response) {
                if (response.isSuccessful()) {
                    fetchLoveLists();
                    Toast.makeText(getContext(), "Danh sách yêu thích đã được thêm", Toast.LENGTH_SHORT).show();
                } else {
                    handleErrorResponse(response);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoveLIst> call, @NonNull Throwable t) {
                Toast.makeText(getContext(), "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void handleErrorResponse(Response<LoveLIst> response) {
        try {
            String errorBody = response.errorBody() != null ? response.errorBody().string() : "Không có thông tin lỗi";
            Log.e("API Error", "Lỗi khi thêm danh sách yêu thích: " + response.code() + " - " + errorBody);
            Toast.makeText(getContext(), "Lỗi khi thêm danh sách yêu thích", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("API Error", "Không thể đọc thông tin lỗi: " + e.getMessage());
        }
    }
}

//======================Base