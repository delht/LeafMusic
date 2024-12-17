package vn.edu.stu.leafmusic.activity.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.activity.UI.Music_Player;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.api.dto.ApiService;
import vn.edu.stu.leafmusic.model.Song;
import vn.edu.stu.leafmusic.model.Song2;

public class SongsAdapter2 extends RecyclerView.Adapter<SongsAdapter2.SongViewHolder> {

    private List<Song> songs;
    private OnItemClickListener listener;
    private Context context;
    private String idDs;  // ID danh sách yêu thích

    private String loaiDs;

    public SongsAdapter2(List<Song> songs, OnItemClickListener listener, String idDs, String loaiDs) {
        this.songs = songs;
        this.listener = listener;
        this.idDs = idDs;  // Nhận ID danh sách từ Activity hoặc Fragment
        this.loaiDs = loaiDs;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song2, parent, false);
        this.context = parent.getContext();
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songName.setText(song.getTenBaiHat());

        Glide.with(holder.itemView.getContext())
                .load(song.getUrlHinh())
                .transform(new RoundedCorners(30))
                .into(holder.songImage);

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(Integer.parseInt(String.valueOf(song.getIdBaiHat())));
            getSongDetails(song.getIdBaiHat());
        });

        holder.itemView.setOnLongClickListener(v -> {
            showConfirmationDialog(song); // Hiển thị hộp thoại xác nhận khi nhấn lâu
            return true;
        });

        // Xử lý sự kiện nhấn nút "Xem thêm"
        holder.moreOptionsButton.setOnClickListener(v -> {
            showConfirmationDialog(song);  // Hiển thị thông báo khi nhấn nút "Xem thêm"
        });


        holder.moreOptionsButton.setImageResource(R.drawable.ic_delete);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songName;
        ImageView songImage;
        ImageView moreOptionsButton;  // Chuyển thành ImageView

        public SongViewHolder(View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.tv_song_name);
            songImage = itemView.findViewById(R.id.img_song);
            moreOptionsButton = itemView.findViewById(R.id.btnBHXemThem);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int idSong);
    }

    // Phương thức lấy chi tiết bài hát
    private void getSongDetails(int songId) {
        ApiClient.getApiService().getSongById(songId).enqueue(new Callback<Song2>() {
            @Override
            public void onResponse(Call<Song2> call, Response<Song2> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Song2 song = response.body();

                    Log.d("Song Details", "ID: " + song.getIdBaiHat());
                    Log.d("Song Details", "Tên bài hát: " + song.getTenBaiHat());
                    Log.d("Song Details", "Ca sĩ: " + song.getCaSi());
                    Log.d("Song Details", "Thể loại: " + song.getTheLoai());
                    Log.d("Song Details", "Album: " + song.getAlbum());
                    Log.d("Song Details", "Khu vực nhạc: " + song.getKhuVucNhac());
                    Log.d("Song Details", "URL hình: " + song.getUrlHinh());
                    Log.d("Song Details", "URL file: " + song.getUrlFile());
                    Log.d("Song Details", "Ngày phát hành: " + song.getFormattedReleaseDate());

                    Intent intent = new Intent(context, Music_Player.class);
                    intent.putExtra("song_name", song.getTenBaiHat());
                    intent.putExtra("artist", song.getCaSi());
                    intent.putExtra("song_url", song.getUrlFile());
                    intent.putExtra("image_url", song.getUrlHinh());
                    intent.putExtra("song_id", song.getIdBaiHat());
                    intent.putExtra("album", song.getAlbum());
                    intent.putExtra("genre", song.getTheLoai());
                    intent.putExtra("region", song.getKhuVucNhac());
                    intent.putExtra("release_date", song.getFormattedReleaseDate());

                    ArrayList<Song2> playlist = new ArrayList<>();
                    playlist.add(song);
                    intent.putParcelableArrayListExtra("playlist", playlist);
                    intent.putExtra("position", 0);

                    context.startActivity(intent);  // Sử dụng context để mở Activity
                } else {
                    Log.e("Error", "Failed to get song details");
                }
            }

            @Override
            public void onFailure(Call<Song2> call, Throwable t) {
                Log.e("Error", "API call failed: " + t.getMessage());
            }
        });
    }

    // Phương thức hiển thị AlertDialog và xóa bài hát khỏi danh sách
    private void showConfirmationDialog(Song song) {
        new AlertDialog.Builder(context)
                .setTitle("Xác nhận")
                .setMessage("Bạn có muốn xóa bài hát \"" + song.getTenBaiHat() + "\" khỏi danh sách yêu thích không?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Gọi API để xóa bài hát khỏi danh sách yêu thích
                        removeSongFromCustom(idDs, String.valueOf(song.getIdBaiHat()));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context, "Bạn đã chọn No!", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    // Phương thức gọi API để xóa bài hát khỏi danh sách yêu thích
//    private void removeSongFromCustom(String idDs, String idBaihat) {
//        ApiClient.getApiService().remoteSongToCustom(idDs, idBaihat).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    Toast.makeText(context, "Bài hát đã được xóa khỏi danh sách!", Toast.LENGTH_SHORT).show();
//                    // Cập nhật lại giao diện (xóa bài hát khỏi danh sách trong RecyclerView)
//                    songs.removeIf(song -> song.getIdBaiHat() == Integer.parseInt(idBaihat));
//                    notifyDataSetChanged();
//                } else {
//                    Toast.makeText(context, "Không thể xóa bài hát. Thử lại sau!", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(context, "Lỗi kết nối. Thử lại sau!", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    // Phương thức gọi API để xóa bài hát khỏi danh sách yêu thích
    private void removeSongFromCustom(String idDs, String idBaihat) {
        Log.e("TEST", idDs);
        ApiService apiService = ApiClient.getApiService();

        // Kiểm tra nếu loại danh sách là "maccdinh"
        if ("macdinh".equals(loaiDs)) {
            // Gọi API xóa bài hát khỏi danh sách yêu thích mặc định
            apiService.deleteToFavorite(idDs, idBaihat).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Bài hát đã được xóa khỏi danh sách mặc định!", Toast.LENGTH_SHORT).show();
                        // Cập nhật lại giao diện (xóa bài hát khỏi danh sách trong RecyclerView)
                        songs.removeIf(song -> song.getIdBaiHat() == Integer.parseInt(idBaihat));
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Không thể xóa bài hát khỏi danh sách mặc định. Thử lại sau!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Lỗi kết nối. Thử lại sau!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // Gọi API xóa bài hát khỏi danh sách yêu thích thông thường
            apiService.remoteSongToCustom(idDs, idBaihat).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(context, "Bài hát đã được xóa khỏi danh sách!", Toast.LENGTH_SHORT).show();
                        // Cập nhật lại giao diện (xóa bài hát khỏi danh sách trong RecyclerView)
                        songs.removeIf(song -> song.getIdBaiHat() == Integer.parseInt(idBaihat));
                        notifyDataSetChanged();
                    } else {
                        Toast.makeText(context, "Không thể xóa bài hát. Thử lại sau!", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Lỗi kết nối. Thử lại sau!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }



}
