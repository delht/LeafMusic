package vn.edu.stu.leafmusic.util;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.api.dto.ApiService;
import vn.edu.stu.leafmusic.model.LoveList_Simple;
import vn.edu.stu.leafmusic.R;

public class AddToPlaylistDialog {

    private static SharedPrefsHelper sharedPrefsHelper;
    private static String userId;

    // Thêm một tham số idBaiHat vào phương thức show()
    public static void show(Context context, String idBaiHat) {

        // Kiểm tra trạng thái đăng nhập
        if (userId == null) {
            Toast.makeText(context, "User ID không hợp lệ. Vui lòng đăng nhập lại.", Toast.LENGTH_SHORT).show();
            return;
        }

        Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_to_playlist);

        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.height = (int) (context.getResources().getDisplayMetrics().heightPixels * 0.5);
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.getWindow().setAttributes(params);

        dialog.setCanceledOnTouchOutside(true);  // Bấm ra ngoài để đóng dialog

        // Hiển thị dialog
        dialog.show();

        // Tạo biến lưu idDs
        String[] selectedIdDs = {null}; // Sử dụng mảng để giữ giá trị trong lambda

        // Gọi API để lấy danh sách yêu thích trong Dialog
        ApiService apiService = ApiClient.getApiService();
        apiService.getLoveListSimple(userId).enqueue(new Callback<List<LoveList_Simple>>() {
            @Override
            public void onResponse(Call<List<LoveList_Simple>> call, Response<List<LoveList_Simple>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LoveList_Simple> loveList = response.body();
                    if (loveList.isEmpty()) {
                        Toast.makeText(context, "Danh sách yêu thích rỗng.", Toast.LENGTH_SHORT).show();
                    } else {
                        // Tạo Adapter và thiết lập cho ListView trong Dialog
                        ListView listView = dialog.findViewById(R.id.lvDS);
                        ArrayAdapter<LoveList_Simple> adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_single_choice, loveList);
                        listView.setAdapter(adapter);

                        // Thiết lập chế độ chọn item trong ListView
                        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE); // Cho phép chọn một item duy nhất

                        // Lắng nghe sự kiện chọn item trong ListView
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                                LoveList_Simple selectedItem = loveList.get(position);
                                selectedIdDs[0] = String.valueOf(selectedItem.getIdDs());  // Lưu idDs vào biến cục bộ

                                // Khi chọn một mục, kích hoạt nút "Lưu"
                                Button btnLuu = dialog.findViewById(R.id.btnLuu);
                                btnLuu.setEnabled(true);  // Kích hoạt nút lưu

                                // Đánh dấu mục đã chọn (màu chấm chọn sẽ sáng lên)
                                listView.setItemChecked(position, true);  // Đánh dấu mục đã chọn
                            }
                        });
                    }
                } else {
                    Toast.makeText(context, "Không có dữ liệu.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LoveList_Simple>> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("E", t.getMessage());
            }
        });

        // Lấy nút lưu và thực hiện gọi API khi nhấn
        Button btnLuu = dialog.findViewById(R.id.btnLuu);
        btnLuu.setEnabled(false);  // Ban đầu nút "Lưu" vô hiệu hóa
        btnLuu.setOnClickListener(v -> {
            // Kiểm tra idDs đã được chọn chưa
            if (selectedIdDs[0] == null) {
                Toast.makeText(context, "Vui lòng chọn một danh sách yêu thích.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi API để thêm bài hát vào danh sách yêu thích
            addSongToPlaylist(selectedIdDs[0], idBaiHat, context);  // Truyền idDs và context
            dialog.dismiss();
        });


        // Lấy nút Hủy và thực hiện hành động đóng dialog khi nhấn
        Button btnHuy = dialog.findViewById(R.id.btnHuy);
        btnHuy.setOnClickListener(v -> {
            dialog.dismiss();  // Đóng Dialog khi nhấn nút Hủy
        });




    }

    // Phương thức gửi API addCustom với idDs và idBaiHat
    private static void addSongToPlaylist(String idDs, String idBaiHat, Context context) {
        // Kiểm tra idDs và idBaiHat không null
        if (userId == null || idBaiHat == null || idDs == null) {
            Log.e("AddToPlaylist", "User ID, ID bài hát hoặc ID danh sách không hợp lệ");
            return;
        }

        ApiService apiService = ApiClient.getApiService();
        apiService.addSongToCustom(idDs, idBaiHat)  // Sử dụng idDs từ danh sách và idBaiHat
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(context, "Thêm bài hát vào danh sách thành công!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Thêm bài hát thất bại, bài hát đã có trong danh sách này.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.e("AddToPlaylist", t.getMessage());
                    }
                });
    }

    // Phương thức để khởi tạo userId từ SharedPrefs
    public static void initialize(Context context) {
        sharedPrefsHelper = new SharedPrefsHelper(context); // Truyền context vào
        userId = sharedPrefsHelper.getUserId(); // Lấy userId từ SharedPreferences
    }
}
