package vn.edu.stu.leafmusic.activity.adapter;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.activity.detail.AlbumDetailFragment;
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
                .load(album.getUrlHinh()) //url hinh
                .placeholder(R.drawable.error) // Hình ảnh hiển thị khi đang tải
                .error(R.drawable.error)
                .transform(new RoundedCorners(30)) //bo goc
                .into(holder.albumImage);

        String uri = album.getUrlHinh();
        Log.e("test", uri);

        holder.itemView.setOnClickListener(v -> {
            // Tạo một instance mới của AlbumDetailFragment
            AlbumDetailFragment albumDetailFragment = AlbumDetailFragment.newInstance(
                    album.getIdAlbum(),
                    album.getTenAlbum(),
                    album.getTenCaSi(),
                    album.getUrlHinh()
            );

            // Thay thế Fragment trong MainActivity
            FragmentTransaction transaction = ((AppCompatActivity) holder.itemView.getContext()).getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, albumDetailFragment);
            transaction.addToBackStack(null); // Thêm vào back stack để quay lại
            transaction.commit();
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


    public interface OnItemClickListener {
        void onItemClick(int idAlbum);
    }
}
