package vn.edu.stu.leafmusic.activity.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import vn.edu.stu.leafmusic.R;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

import vn.edu.stu.leafmusic.model.Song;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongViewHolder> {

    private List<Song> songs;
    private OnItemClickListener listener;

    public SongsAdapter(List<Song> songs, OnItemClickListener listener) {
        this.songs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Song song = songs.get(position);
        holder.songName.setText(song.getTenBaiHat());

        Glide.with(holder.itemView.getContext())
                .load(song.getUrlHinh())
                .transform(new RoundedCorners(30)) //bo goc
                .into(holder.songImage);

        holder.itemView.setOnClickListener(v -> listener.onItemClick(Integer.parseInt(String.valueOf(song.getIdBaiHat()))));
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView songName;
        ImageView songImage;

        public SongViewHolder(View itemView) {
            super(itemView);
            songName = itemView.findViewById(R.id.tv_song_name);
            songImage = itemView.findViewById(R.id.img_song);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int idSong);
    }
}
