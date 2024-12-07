package vn.edu.stu.leafmusic.activity.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import java.util.List;

import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.model.Artist;

import android.widget.ImageView;
import android.widget.TextView;

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder> {

    private List<Artist> artists;
    private OnItemClickListener listener;

    public ArtistsAdapter(List<Artist> artists, OnItemClickListener listener) {
        this.artists = artists;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        Artist artist = artists.get(position);
        holder.artistName.setText(artist.getTenCaSi());


        Glide.with(holder.itemView.getContext())
                .load(artist.getUrlHinh()) //url hinh
                .transform(new RoundedCorners(30)) //bo goc
                .into(holder.artistImage);

        String uri = artist.getUrlHinh();
        Log.e("test", uri);

        // Xử lý sự kiện click
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(Integer.parseInt(artist.getIdCaSi()));
        });
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    // ViewHolder
    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;
        ImageView artistImage;

        public ArtistViewHolder(View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.tv_artist_name);
            artistImage = itemView.findViewById(R.id.img_artist);
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int idArtist);
    }
}
