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

    //được gọi khi tạo một đối tượng songdapter mới
    public SongsAdapter(List<Song> songs, OnItemClickListener listener) {
        this.songs = songs;
        this.listener = listener;
    }

    //được gọi khi tạo một đối tượng songdapter mới
    @NonNull
    @Override
    //tạo ra một view cho mỗi item trong recycleview và tái sử dụng
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //sử dụng layoutinflater để chuyển đổi item sang đối tượng view mà recycle có thể sử dụng
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_song, parent, false);
        this.context = parent.getContext();  // Lưu context
        //trả về songviewholder để quản lý view
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        //lấy bài hát tại vị trí position hiện tại của item trong danh sách song
        Song song = songs.get(position);
        //thiết lập tên bài hát vào textview của songviewholder
        holder.songName.setText(song.getTenBaiHat());//setText lấy dữ liệu tên bài hát từ lớp Song

<<<<<<< Updated upstream
        Glide.with(holder.itemView.getContext())
                .load(song.getUrlHinh())
                .placeholder(R.drawable.error)
                .error(R.drawable.error)
                .transform(new RoundedCorners(30))
                .into(holder.songImage);
=======
        //sử dụng thư viện glide tải hình ảnh từ url
        Glide.with(holder.itemView.getContext())//glide hđ trong ngữ cảnh itemview
                .load(song.getUrlHinh()) //lấy dữ liệu hình từ lớp song
                .transform(new RoundedCorners(30)) // bo góc
                .into(holder.songImage); //gắn hình đã tải vào imageview
>>>>>>> Stashed changes

        //gắn sự kiện click vào item của songviewholder
        holder.itemView.setOnClickListener(v -> {
            listener.onItemClick(Integer.parseInt(String.valueOf(song.getIdBaiHat()))); //gửi id bài hát cho listener sau đó gọi API lấy chi tiết bài hát
            getSongDetails(song.getIdBaiHat());  // Gọi phương thức mở bài hát
        });
    }

<<<<<<< Updated upstream

=======
    //trả về số lượng item trong recycleview
>>>>>>> Stashed changes
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
            //ánh xạ thành phần từ layout vào biến
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
        void onItemClick(int idSong); // khi nhấn vào item thì id của bài hát sẽ được truyền vào
    }





//    -=========================================================================

    private void getSongDetails(int songId) {
        //sử dụng để gọi API và lấy chi tiết của một bài hát dựa vào id bài hát
        ApiClient.getApiService().getSongById(songId).enqueue(new Callback<Song2>() {
            @Override
            //trả dữ liệu về qua phương thức onResponse
            public void onResponse(Call<Song2> call, Response<Song2> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Song2 song = response.body(); //trả về đối tượng dữ liệu bài hát

//                    Log.d("Song Details", "ID: " + song.getIdBaiHat());
//                    Log.d("Song Details", "Tên bài hát: " + song.getTenBaiHat());
//                    Log.d("Song Details", "Ca sĩ: " + song.getCaSi());
//                    Log.d("Song Details", "Thể loại: " + song.getTheLoai());
//                    Log.d("Song Details", "Album: " + song.getAlbum());
//                    Log.d("Song Details", "Khu vực nhạc: " + song.getKhuVucNhac());
//                    Log.d("Song Details", "URL hình: " + song.getUrlHinh());
//                    Log.d("Song Details", "URL file: " + song.getUrlFile());
//                    Log.d("Song Details", "Ngày phát hành: " + song.getFormattedReleaseDate());

                    //context là dối tượng của fragment  hiện tại được sử dụng khởi tạo intent
                    Intent intent = new Intent(context, Music_Player.class);
                    //intent.putExtra() : lưu trữ thông tin chi tiết bài hát vào Intent
                    intent.putExtra("song_name", song.getTenBaiHat()); //theo cặp key-value
                    intent.putExtra("artist", song.getCaSi());
                    intent.putExtra("song_url", song.getUrlFile());
                    intent.putExtra("image_url", song.getUrlHinh());
                    intent.putExtra("song_id", song.getIdBaiHat());
                    intent.putExtra("album", song.getAlbum());
                    intent.putExtra("genre", song.getTheLoai());
                    intent.putExtra("region", song.getKhuVucNhac());
                    intent.putExtra("release_date", song.getFormattedReleaseDate());

                    ArrayList<Song2> playlist = new ArrayList<>();
                    playlist.add(song); //tạo một arraylist chứa bài hát hiện tại
                    intent.putParcelableArrayListExtra("playlist", playlist); // lưu vào intent, danh sách sẽ truyền tới musicplayer
                    intent.putExtra("position", 0); //bài hát hiện tại là bài đầu tiên trong danh sách

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
