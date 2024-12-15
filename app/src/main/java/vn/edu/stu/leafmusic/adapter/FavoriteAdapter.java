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

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.SongViewHolder> {

    private List<Song> favoriteSongs;
    private OnSongClickListener onSongClickListener;

    public interface OnSongClickListener {
        void onSongClick(Song song, int position);
    }

    public FavoriteAdapter(List<Song> favoriteSongs, OnSongClickListener onSongClickListener) {
        this.favoriteSongs = favoriteSongs;
        this.onSongClickListener = onSongClickListener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = favoriteSongs.get(position);
        holder.tvSongName.setText(song.getTenBaiHat());
        holder.tvArtist.setText(song.getCaSi());
        holder.tvReleaseDate.setText(song.getFormattedReleaseDate());

        Glide.with(holder.itemView.getContext())
                .load(song.getUrlHinh())
                .into(holder.imgSong);

        holder.itemView.setOnClickListener(v -> onSongClickListener.onSongClick(song, position));
    }

    @Override
    public int getItemCount() {
        return favoriteSongs.size();
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
}