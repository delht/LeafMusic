package vn.edu.stu.leafmusic.activity.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.model.Album;
import android.widget.ImageView;
import android.widget.TextView;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {

    private List<Album> albums;
    private OnItemClickListener listener;

    public AlbumsAdapter(List<Album> albums, OnItemClickListener listener) {
        this.albums = albums;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album, parent, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        Album album = albums.get(position);
        holder.albumName.setText(album.getTenAlbum());

        // Tải ảnh album sử dụng Glide
        Glide.with(holder.itemView.getContext())
                .load(album.getUrlHinh()) // URL ảnh album
                .into(holder.albumImage);

        String uri = album.getUrlHinh();
        Log.e("test", uri);

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(Integer.parseInt(album.getIdAlbum()));
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    // ViewHolder
    public static class AlbumViewHolder extends RecyclerView.ViewHolder {
        TextView albumName;
        ImageView albumImage;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            albumName = itemView.findViewById(R.id.tv_album_name);
            albumImage = itemView.findViewById(R.id.img_album);
        }
    }

    // Interface listener
    public interface OnItemClickListener {
        void onItemClick(int idAlbum); // Truyền idAlbum khi click
    }
}
