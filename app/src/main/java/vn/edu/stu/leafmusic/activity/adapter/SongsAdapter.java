package vn.edu.stu.leafmusic.activity.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.stu.leafmusic.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.ArrayList;
import java.util.List;

import vn.edu.stu.leafmusic.activity.UI.Music_Player;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.model.Song;
import vn.edu.stu.leafmusic.model.Song2;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {

    private List<Song> songs;
    private OnItemClickListener listener;
    private Context context;

    public SongsAdapter(List<Song> songs, OnItemClickListener listener) {
        this.songs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        this.context = parent.getContext();  // Lưu context ở đây
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songName.setText(song.getTenBaiHat());

        Glide.with(holder.itemView.getContext())
                .load(song.getUrlHinh())
                .placeholder(R.drawable.ic_launcher_background) // Hình ảnh hiển thị khi đang tải
                .error(R.drawable.ic_launcher_foreground)           // Hình ảnh hiển thị khi tải thất bại
                .transform(new RoundedCorners(30))       // Bo góc hình ảnh
                .into(holder.songImage);

        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(Integer.parseInt(String.valueOf(song.getIdBaiHat())));
            getSongDetails(song.getIdBaiHat());  // Gọi phương thức mở bài hát
        });
    }


    @Override
    public int getItemCount() {
        return songs.size();
    }


    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songName;
        ImageView songImage;
//        ImageButton moreOptionsButton;

        public SongViewHolder(View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.tv_song_name);
            songImage = itemView.findViewById(R.id.img_song);

            //cai nay de xu ly nut xem them

//            moreOptionsButton = itemView.findViewById(R.id.btnBHXemThem);

//            moreOptionsButton.setOnClickListener(v -> {
//                Toast.makeText(itemView.getContext(), "Bạn vừa bấm vào nút Xem Thêm!", Toast.LENGTH_SHORT).show();
//            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int idSong);
    }





//    -=========================================================================

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



}
