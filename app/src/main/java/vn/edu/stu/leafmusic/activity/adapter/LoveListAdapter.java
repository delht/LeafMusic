package vn.edu.stu.leafmusic.activity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.model.LoveLIst;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.api.dto.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoveListAdapter extends RecyclerView.Adapter<LoveListAdapter.LoveListViewHolder> {

    private final Context context;
    private final List<LoveLIst> loveList;

    public LoveListAdapter(Context context, List<LoveLIst> loveList) {
        this.context = context;
        this.loveList = loveList;
    }

    @NonNull
    @Override
    public LoveListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_lovelist, parent, false);
        return new LoveListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoveListViewHolder holder, int position) {
        LoveLIst item = loveList.get(position);

        holder.tvName.setText(item.getTenDs());

        // Highlight default list
        if (item.isDefaultList()) {
            holder.tvName.setTextColor(context.getResources().getColor(R.color.Green4));
        } else {
            holder.tvName.setTextColor(context.getResources().getColor(R.color.black));
        }

        // Show options menu when click "Xem thêm"
        holder.btnMoreOptions.setOnClickListener(v -> showOptionsMenu(v, item));
    }

    @Override
    public int getItemCount() {
        return loveList.size();
    }

    private void showOptionsMenu(View view, LoveLIst item) {
        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.getMenuInflater().inflate(R.menu.menu_options, popupMenu.getMenu()); // Tạo menu tùy chọn
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.menu_delete) {
                // Nếu là danh sách custom thì mới có thể xóa
                if (!item.getLoaiDs().equals("macdinh")) {
                    deleteLoveList(item.getIdDs());
                } else {
                    Toast.makeText(context, "Không thể xóa danh sách mặc định", Toast.LENGTH_SHORT).show();
                }
                return true;

            } else if (menuItem.getItemId() == R.id.menu_rename) {
                // Nếu là danh sách custom thì mới có thể đổi tên
                if (!item.getLoaiDs().equals("macdinh")) {
                    showRenameDialog(item); // Hiển thị dialog để đổi tên
                } else {
                    Toast.makeText(context, "Không thể đổi tên danh sách mặc định", Toast.LENGTH_SHORT).show();
                }
                return true;

            } else {
                return false;
            }
        });
    }

    private void showRenameDialog(LoveLIst item) {
        // Tạo EditText để nhập tên mới
        EditText input = new EditText(context);
        input.setText(item.getTenDs());  // Đặt tên hiện tại làm giá trị mặc định

        // Tạo AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Đổi tên danh sách");
        builder.setMessage("Nhập tên mới cho danh sách");

        // Thêm EditText vào dialog
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                renameLoveList(item.getIdDs(), newName); // Gọi API để đổi tên danh sách
            } else {
                Toast.makeText(context, "Tên không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        // Hiển thị dialog
        builder.show();
    }

    private void deleteLoveList(int idDs) {
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);
        apiService.deleteLoveList(idDs).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Đã xóa danh sách", Toast.LENGTH_SHORT).show();
                    // Cập nhật lại RecyclerView
                    loveList.removeIf(item -> item.getIdDs() == idDs);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Lỗi khi xóa danh sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void renameLoveList(int idDs, String newName) {
        ApiService apiService = ApiClient.getRetrofit().create(ApiService.class);
        apiService.renameLoveList(idDs, newName).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(@NonNull Call<Void> call, @NonNull Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(context, "Đã đổi tên danh sách", Toast.LENGTH_SHORT).show();
                    // Cập nhật lại RecyclerView
                    for (LoveLIst item : loveList) {
                        if (item.getIdDs() == idDs) {
                            item.setTenDs(newName);
                            break;
                        }
                    }
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Lỗi khi đổi tên danh sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<Void> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static class LoveListViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        ImageView imgIcon;
        Button btnMoreOptions;  // Thêm nút "Xem thêm"

        public LoveListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_ds_name);
            imgIcon = itemView.findViewById(R.id.img_ds);
            btnMoreOptions = itemView.findViewById(R.id.btnBHXemThem);  // Kết nối với nút "Xem thêm"
        }
    }
}