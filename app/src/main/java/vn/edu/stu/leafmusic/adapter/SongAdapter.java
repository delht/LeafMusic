package vn.edu.stu.leafmusic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.model.Song;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Song> songs;
    private OnSongClickListener onSongClickListener;

    public interface OnSongClickListener {
        void onSongClick(Song song, int position);
    }

    public SongAdapter(List<Song> songs, OnSongClickListener onSongClickListener) {
        this.songs = songs;
        this.onSongClickListener = onSongClickListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.tvSongName.setText(song.getTenBaiHat());
        holder.tvArtist.setText(song.getCaSi());
        holder.tvReleaseDate.setText(formatReleaseDate(song.getNgayPhatHanh()));

        // Tải hình ảnh bài hát bằng Glide
        Glide.with(holder.itemView.getContext())
                .load(song.getUrlHinh())
                .into(holder.imgSong);

        holder.itemView.setOnClickListener(v -> onSongClickListener.onSongClick(song, position));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView tvSongName;
        TextView tvArtist;
        TextView tvReleaseDate;
        ImageView imgSong;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSongName = itemView.findViewById(R.id.tvSongName);
            tvArtist = itemView.findViewById(R.id.tvArtist);
            tvReleaseDate = itemView.findViewById(R.id.tvReleaseDate);
            imgSong = itemView.findViewById(R.id.imgSong);
        }
    }
    private String formatReleaseDate(int[] ngayPhatHanh) {
        if (ngayPhatHanh != null && ngayPhatHanh.length >= 3) {
            return ngayPhatHanh[0] + "-" + ngayPhatHanh[1] + "-" + ngayPhatHanh[2];
        }
        return "Không có thông tin";
    }
}