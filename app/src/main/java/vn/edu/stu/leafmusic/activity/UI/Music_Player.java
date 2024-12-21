package vn.edu.stu.leafmusic.activity.UI;

import android.app.Dialog;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.api.dto.ApiService;
import vn.edu.stu.leafmusic.api.dto.request.ThemBaiHat_DsMacDinh_Request;
import vn.edu.stu.leafmusic.model.LoveLIst;
import vn.edu.stu.leafmusic.model.LoveList_Simple;
import vn.edu.stu.leafmusic.model.Song2;
import vn.edu.stu.leafmusic.util.AddToPlaylistDialog;
import vn.edu.stu.leafmusic.util.RotateAnimationHelper;
import vn.edu.stu.leafmusic.util.SharedPrefsHelper;

public class Music_Player extends AppCompatActivity {

    private ImageButton btnBack, btnFavorite, btnAddToPlaylist;
    private ImageView imgSong;
    private TextView tvSongName, tvArtist, tvCurrentTime, tvTotalTime, tvAbum, tvTheLoai;
    private SeekBar seekBar;
    private ImageButton btnPrev, btnPlayPause, btnNext, btnShuffle, btnRepeat;

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private boolean isPlaying = false;
    private String songUrl;
    private boolean isShuffleOn = false;
    private boolean isRepeatOn = false;
    private boolean isFavorite = false;
    private ArrayList<Song2> playlist;
    private int currentSongIndex = 0;
    private RotateAnimationHelper rotateAnimationHelper;
    private DrawerLayout drawerLayout;
    private TextView tvDetailSongName, tvDetailArtist, tvDetailAlbum,
            tvDetailGenre, tvDetailReleaseDate;


    String idBaiHat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        //khởi tạo hiệu ứng đĩa xoay
        rotateAnimationHelper = new RotateAnimationHelper();

        //khởi tạo các view trong giao diện
        initViews();

        //nút quay về
        btnBack.setOnClickListener(v -> finish());

<<<<<<< Updated upstream
        initFavoriteButton();

        checkIfSongIsFavorite();

//============================

        playlist = getIntent().getParcelableArrayListExtra("playlist");
        currentSongIndex = getIntent().getIntExtra("position", 0);

        if (playlist != null && !playlist.isEmpty()) {
            Song2 currentSong = playlist.get(currentSongIndex);

            // Hiển thị thông tin bài hát
            tvSongName.setText(currentSong.getTenBaiHat());
            tvArtist.setText(currentSong.getCaSi());
            tvAbum.setText(currentSong.getAlbum());
            tvTheLoai.setText(currentSong.getTheLoai());

            Picasso.get()
                    .load(currentSong.getUrlHinh()) // URL hình ảnh
                    .placeholder(R.drawable.error) // Ảnh mặc định khi tải
                    .error(R.drawable.error) // Ảnh mặc định khi có lỗi
                    .into(imgSong);


            idBaiHat = String.valueOf(currentSong.getIdBaiHat());
            Log.e("TEST", "id bai hat: "+currentSong.getIdBaiHat());

            songUrl = currentSong.getUrlFile(); // Lấy URL bài hát
            setupMediaPlayer();
        }

//============================

        if (getIntent().hasExtra("song_url")) {
            String songId = getIntent().getStringExtra("song_id");
            songUrl = getIntent().getStringExtra("song_url");
=======
        //Lấy dữ liệu từ Intent ở SongAdapter, dữ liệu sẽ được truyền từ Active trước đó thông qua Intent
        if (getIntent().hasExtra("song_url")) {
            songUrl = getIntent().getStringExtra("song_url");//nhận các giá trị được truyền
>>>>>>> Stashed changes
            String songName = getIntent().getStringExtra("song_name");
            String artist = getIntent().getStringExtra("artist");
            String imageUrl = getIntent().getStringExtra("image_url");
            playlist = getIntent().getParcelableArrayListExtra("playlist"); //lấy danh sách bài hát
            currentSongIndex = getIntent().getIntExtra("position", 0);

            // Hiển thị thông tin bài hát
            tvSongName.setText(songName);
            tvArtist.setText(artist);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get()
                        .load(imageUrl) // URL của ảnh
                        .placeholder(R.drawable.error) // Hình ảnh mặc định khi tải
                        .error(R.drawable.error) // Hình ảnh mặc định khi có lỗi
                        .into(imgSong);
            } else {
                // Nếu không có imageUrl hoặc là rỗng, dùng ảnh mặc định
                Picasso.get()
                        .load(R.drawable.error) // Sử dụng ảnh mặc định
                        .into(imgSong);
            }

            setupMediaPlayer();
        }

        //xử lý các nút
        setupListeners();

        initDetailViews();

        updateSongDetails();

        drawerLayout = findViewById(R.id.drawerLayout);

        // Cho phép vuốt từ cạnh phải
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        //kiểm tra android 10 (API level 29)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawerLayout.setSystemGestureExclusionRects(Collections.emptyList());// tạo một danh sách trống, có nghĩa là không có vùng gesture nào bị loại trừ.
        }//giúp vuốt cạnh màn hình mà không bị ảnh hưởng bới drawerlayout

        //sự kiện vuốt
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                View contentView = findViewById(R.id.rootLayout);
                float slideX = drawerView.getWidth() * slideOffset; //tính toán độ dịch chuyển thanh drawer
                contentView.setTranslationX(-slideX); //nội dung chính bị dịch chuyển sang trái
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {}

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });
//=============================================

//        btnAddToPlaylist.setOnClickListener(v -> showAddToPlaylistDialog());

        btnAddToPlaylist.setOnClickListener(v -> {
            AddToPlaylistDialog.initialize(Music_Player.this);
//            AddToPlaylistDialog.show(Music_Player.this);

            AddToPlaylistDialog.show(this, idBaiHat);
        });



    }

    //Ánh xạ các view vào biến
    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);
        btnAddToPlaylist = findViewById(R.id.btnAddToPlaylist);
        imgSong = findViewById(R.id.imgSong);
        tvSongName = findViewById(R.id.tvSongName);
        tvArtist = findViewById(R.id.tvArtist);
        tvCurrentTime = findViewById(R.id.tvCurrentTime);
        tvAbum = findViewById(R.id.tvDetailAlbum);
        tvTheLoai = findViewById(R.id.tvDetailGenre);
        tvTotalTime = findViewById(R.id.tvTotalTime);
        seekBar = findViewById(R.id.seekBar);
        btnPrev = findViewById(R.id.btnPrevious);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        btnNext = findViewById(R.id.btnNext);
        btnShuffle = findViewById(R.id.btnShuffle);
        btnRepeat = findViewById(R.id.btnRepeat);
    }

    //thông tin khi vuốt phải để xem chi tiết bài hát
    private void initDetailViews() {
        //Ánh xạ thành phần giao diện vào biến
        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view_detail);
        View headerView = navigationView.getHeaderView(0);

        tvDetailSongName = findViewById(R.id.tvDetailSongName);
        tvDetailArtist = findViewById(R.id.tvDetailArtist);
        tvDetailAlbum = findViewById(R.id.tvDetailAlbum);
        tvDetailGenre = findViewById(R.id.tvDetailGenre);
        tvDetailReleaseDate = findViewById(R.id.tvDetailReleaseDate);

        //loại bỏ màu nền mặc định
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        //lắng nghe thay đổi trong trạng thái Drawlayout
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            //gọi khi Draw đang mở hoặc trượt
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View contentView = findViewById(R.id.rootLayout);
                float slideX = drawerView.getWidth() * slideOffset;
                contentView.setTranslationX(-slideX); //dịch chuyển nội dung chính về bên trái khi vuốt phải
                contentView.setScaleX(1 - (slideOffset * 0.1f)); //thu nhỏ chiều ngang của nội dung chính tạo hiệu ứng zoom out
                contentView.setScaleY(1 - (slideOffset * 0.1f)); //thu nhỏ chiều dọc

                drawerView.setTranslationX(drawerView.getWidth() * (1 - slideOffset)); //dịch chuyển menu trượt từ phải sang trái
            }
        });
    }

    //cập nhật chi tiết bài hát lên giao diện chi tiết
    private void updateSongDetails() {
        //danh sách không null, chỉ số bài hát hiện tại không âm, nằm trong phạm vi danh sách bài hát
        if (playlist != null && currentSongIndex >= 0 && currentSongIndex < playlist.size()) {
            //truy xuất bài hát tại vị trí currentsongindex từ ds bài hát
            Song2 currentSong = playlist.get(currentSongIndex);//currentsong chứa thông tin chi tiết bài hát hiện tại

            tvDetailSongName.setText(currentSong.getTenBaiHat());
            tvDetailArtist.setText(currentSong.getCaSi());
            tvDetailReleaseDate.setText(currentSong.getFormattedReleaseDate());
        }
    }

    //thiết lập và chuẩn bị phát nhạc thông qua MediaPlayer
    private void setupMediaPlayer() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);  //hiển thị thanh tiến trình trong khi nhạc đang được tải

        new Thread(() -> {
            try {
                if (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.isPlaying()) { //nếu phát
                            mediaPlayer.stop(); // thì dừng
                        }
                        mediaPlayer.reset(); //đặt lại trạng thái
                        mediaPlayer.release();//giải phóng tài nguyên
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mediaPlayer = null;
                }

                //khởi tạo đối tượng mới
                mediaPlayer = new MediaPlayer();
<<<<<<< Updated upstream
                mediaPlayer.setDataSource(songUrl);
=======

                //gọi khi MediaPLayer chuẩn bị xong dữ liệu nhạc
>>>>>>> Stashed changes
                mediaPlayer.setOnPreparedListener(mp -> {
                    runOnUiThread(() -> {
                        //Ẩn ProgressBar
                        progressBar.setVisibility(View.GONE);
                        //Hiển thị thời gian tổng của bài hát
                        tvTotalTime.setText(formatTime(mediaPlayer.getDuration()));
                        //thiết lập độ dài bài hát
                        seekBar.setMax(mediaPlayer.getDuration());
                        //bắt đầu phát nhạc
                        playMusic();
                    });
                });

<<<<<<< Updated upstream
                // Thêm OnCompletionListener để chuyển sang bài tiếp theo khi bài hát kết thúc
                mediaPlayer.setOnCompletionListener(mp -> {
                    if (isShuffleOn) {
                        playNextSongRandom();
                    } else {
                        playNextSong();
                    }
                });


                mediaPlayer.setOnCompletionListener(mp -> {
                    if (isShuffleOn) {
                        playNextSongRandom();
                    } else if (isRepeatOn) {
                        playMusic();
=======
                //khi kết thúc
                mediaPlayer.setOnCompletionListener(mp -> {
                    if (isRepeatOn) { //nếu lặp lại
                        playMusic(); //thì phát tiếp tục
>>>>>>> Stashed changes
                    } else {
                        playNextSong(); //chuyển bài
                    }
                });

<<<<<<< Updated upstream
                mediaPlayer.setOnCompletionListener(mp -> {
                    if (isShuffleOn) {
                        playNextSongRandom();
                    } else if (isRepeatOn) {
                        playMusic();
                    } else {
                        playNextSong();
                    }
                });

=======
                //xảy ra lỗi
                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(Music_Player.this,
                                "Lỗi phát nhạc: " + what, Toast.LENGTH_SHORT).show();
                    });
                    return true;
                });

                //cung cấp đường dẫn bài hát
                mediaPlayer.setDataSource(songUrl);
                //chuẩn bị dữ liệu phát nhạc
>>>>>>> Stashed changes
                mediaPlayer.prepareAsync();

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(Music_Player.this, "Không thể phát bài hát này", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();



    }

    private void setupListeners() {
        //nút dừng
        btnPlayPause.setOnClickListener(v -> {
            if (isPlaying) {//nếu phát
                pauseMusic();//thì dừng
            } else {
                playMusic();//phát
            }
        });

        // chuyển bài
        btnNext.setOnClickListener(v -> playNextSong());
        //bài trước
        btnPrev.setOnClickListener(v -> playPreviousSong());
        //ngẫu nhiên
        btnShuffle.setOnClickListener(v -> toggleShuffle());
        //lặp lại
        btnRepeat.setOnClickListener(v -> toggleRepeat());

        //cập nhật tiến trình phát nhạc khi kéo thanh trượt
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) { //nếu từ người dùng
                    mediaPlayer.seekTo(progress); //chuyển tới tgian tương ứng
                    tvCurrentTime.setText(formatTime(progress)); //cập nhật thời gian
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        //quay lại màn hình trước
        btnBack.setOnClickListener(v -> onBackPressed());

    }

    //Phát nhạc
    private void playMusic() {
        try {
            //Không null và medialpayer tồn tại và bài hát ko phát
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start(); //gọi mediaplayer bắt đầu phát bài hát
                btnPlayPause.setImageResource(R.drawable.ic_pause);//cập nhật nút play thành pause
                isPlaying = true;
                updateSeekBar(); //đồng bộ tiến trình seekbar với tgian phát nhạc
                // Bắt đầu animation (xoay ảnh bài hát)
                if (imgSong != null) {
                    rotateAnimationHelper.startAnimation(imgSong);
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi phát nhạc", Toast.LENGTH_SHORT).show();
        }
    }

    //dừng phát
    private void pauseMusic() {
        try {
            //ko null và đang phát
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause(); //dừng
                btnPlayPause.setImageResource(R.drawable.ic_play_circle);//cập nhật nút thành play
                isPlaying = false; //dừng
                if (imgSong != null) { //tạm dừng xoay hình
                    rotateAnimationHelper.pauseAnimation(imgSong);
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tạm dừng", Toast.LENGTH_SHORT).show();
        }
    }

    //cập nhật trạng thái seekbar
    private void updateSeekBar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());//cập nhật vị trí hiện tại
                    tvCurrentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));//hiển thị tgian hiện tại
                    if (isPlaying) {//đang phát
                        handler.postDelayed(this, 1000); //gọi lại phương thức run sau 1 giây bằng handler.postDelayed.
                    }
                }
            }
        }, 1000); //thực thi sau 1 giây từ thời điểm gọi
    }

    private String formatTime(int milliseconds) { //tgian đầu vào 1s = 1000 mili s
        //chuyển mili sang giây
        int seconds = (milliseconds / 1000) % 60; // % 60: Lấy phần dư khi chia cho 60, để chỉ lấy số giây còn lại (từ 0 đến 59).
        int minutes = (milliseconds / (1000 * 60)) % 60; //chuyển từ mili s sang phút
        return String.format("%02d:%02d", minutes, seconds); // hiển thị số nguyên và hiển thị 2 chữ số
    }

    private void playNextSong() {
<<<<<<< Updated upstream
        if (playlist != null && currentSongIndex < playlist.size() - 1) {
            currentSongIndex++;
            Song2 nextSong = playlist.get(currentSongIndex);

            // Cập nhật thông tin bài hát
            tvSongName.setText(nextSong.getTenBaiHat());
            tvArtist.setText(nextSong.getCaSi());
            Picasso.get().load(nextSong.getUrlHinh()).into(imgSong);

            songUrl = nextSong.getUrlFile();  // Cập nhật URL bài hát mới
            setupMediaPlayer();  // Cài đặt lại MediaPlayer với bài hát mới
            playMusic();  // Bắt đầu phát bài hát

            updateSongDetails();
=======
        //ko null, ko rỗng
        if (playlist != null && !playlist.isEmpty()) {
            if (isShuffleOn) { //bật ngẫu nhiên
                currentSongIndex = new Random().nextInt(playlist.size());//lấy ngẫu nhiên từ 0 đến kích thước danh sách
            } else {//nếu đến cuối bài thì quay về bài đầu
                currentSongIndex = (currentSongIndex + 1) % playlist.size(); //lấy chỉ số tiếp theo trong danh sách phát
            }
            playSong(currentSongIndex); //phát bài hát
>>>>>>> Stashed changes
        }
    }


    private void playPreviousSong() {
        if (playlist != null && currentSongIndex > 0) {
            currentSongIndex--;
            Song2 prevSong = playlist.get(currentSongIndex);

            // Cập nhật thông tin bài hát
            tvSongName.setText(prevSong.getTenBaiHat());
            tvArtist.setText(prevSong.getCaSi());
            Picasso.get().load(prevSong.getUrlHinh()).into(imgSong);

            songUrl = prevSong.getUrlFile();  // Cập nhật URL bài hát mới
            setupMediaPlayer();  // Cài đặt lại MediaPlayer với bài hát mới
            playMusic();  // Bắt đầu phát bài hát

            updateSongDetails();
        }
    }


    private void toggleShuffle() {
<<<<<<< Updated upstream
        isShuffleOn = !isShuffleOn;
        if (isShuffleOn) {
            btnShuffle.setImageResource(R.drawable.ic_shuffle_on);  // Thay đổi hình ảnh của nút khi bật chế độ ngẫu nhiên
        } else {
            btnShuffle.setImageResource(R.drawable.ic_shuffle);  // Thay đổi hình ảnh của nút khi tắt chế độ ngẫu nhiên
        }
=======
        isShuffleOn = !isShuffleOn; //chuyển đổi trạng thái chế độ ngẫu nhiên
        btnShuffle.setImageResource(isShuffleOn ? R.drawable.ic_shuffle_on : R.drawable.ic_repeat); //nếu ngẫu nhiên bật thì hiển thị ic_shuffle_on , nếu tắt hiển thị ic_shuffle
>>>>>>> Stashed changes
    }


    private void toggleRepeat() {
        isRepeatOn = !isRepeatOn;
        if (isRepeatOn) {
            btnRepeat.setImageResource(R.drawable.ic_repeat_one);  // Thay đổi hình ảnh của nút khi bật chế độ lặp lại một bài
        } else {
            btnRepeat.setImageResource(R.drawable.ic_repeat);  // Thay đổi hình ảnh của nút khi tắt chế độ lặp lại
        }
    }

<<<<<<< Updated upstream

=======
    //phát bài hát tại vị trí index trong danh sách phát
>>>>>>> Stashed changes
    private void playSong(int index) {
        //ko null, đúng với kích thước danh sách
        if (playlist != null && index >= 0 && index < playlist.size()) {
            Song2 song = playlist.get(index);
            songUrl = song.getUrlFile();//lấy url bài hát từ đối tượng song2 gán cho songurl
            setupMediaPlayer();//gọi để chuẩn bị và bắt đầu bài hát
        }
    }

    @Override
    public void onBackPressed() {
        if (isPlaying) {
            pauseMusic();  // Tạm dừng nhạc khi quay lại
        }
        super.onBackPressed();  // Quay lại màn hình trước đó
    }

    private void playNextSongRandom() {
        if (playlist != null && !playlist.isEmpty()) {
            Random random = new Random();
            int nextSongIndex = random.nextInt(playlist.size());
            Song2 nextSong = playlist.get(nextSongIndex);

            // Cập nhật thông tin bài hát
            tvSongName.setText(nextSong.getTenBaiHat());
            tvArtist.setText(nextSong.getCaSi());
            Picasso.get().load(nextSong.getUrlHinh()).into(imgSong);

            songUrl = nextSong.getUrlFile();  // Cập nhật URL bài hát mới
            setupMediaPlayer();  // Cài đặt lại MediaPlayer với bài hát mới
            playMusic();  // Bắt đầu phát bài hát

            updateSongDetails();
        }
    }


//    =========================================================
    
    private void initFavoriteButton() {

        btnFavorite.setOnClickListener(v -> {
            favoriteBtn();
        });
    }

    private void favoriteBtn() {
        isFavorite = !isFavorite;  // Đảo trạng thái yêu thích

        // Lấy idTaiKhoan từ SharedPrefs
        SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(getApplicationContext());
        String idTaiKhoan = sharedPrefsHelper.getUserId();
        if (idTaiKhoan == null) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }



        getDanhSachMacDinh(idTaiKhoan);
    }

    private void addBaiHatVaoDanhSachMacDinh(String idTaiKhoan, String idDs) {
        ThemBaiHat_DsMacDinh_Request request = new ThemBaiHat_DsMacDinh_Request(idDs, idBaiHat);

        Gson gson = new Gson();
        String jsonRequest = gson.toJson(request);
        Log.d("JSON Request", jsonRequest);

        ApiService apiService = ApiClient.getApiService();
        Call<Void> call = apiService.addToFavorite(request);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    btnFavorite.setImageResource(R.drawable.ic_favorite_filled);  // Cập nhật icon yêu thích
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi khi thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void removeBaiHatKhoiDanhSachMacDinh(String idTaiKhoan, String idDs, String idBaiHat) {
        // Phương thức xóa bài hát khỏi danh sách yêu thích

        // Gọi API với idDs và idBaiHat
        ApiService apiService = ApiClient.getApiService();
        Call<Void> call = apiService.deleteToFavorite(idDs, idBaiHat);  // Truyền tham số vào URL

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Đã xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                    btnFavorite.setImageResource(R.drawable.ic_favorite);  // Cập nhật icon chưa yêu thích
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi khi xóa khỏi yêu thích", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getDanhSachMacDinh(String idTaiKhoan) {
        ApiService apiService = ApiClient.getApiService();
        Call<List<LoveLIst>> call = apiService.getDefaultLoveList(idTaiKhoan);

        call.enqueue(new Callback<List<LoveLIst>>() {
            @Override
            public void onResponse(Call<List<LoveLIst>> call, Response<List<LoveLIst>> response) {
                if (response.isSuccessful()) {
                    List<LoveLIst> danhSachMacDinh = response.body();
                    if (danhSachMacDinh != null && !danhSachMacDinh.isEmpty()) {
                        // Lấy id_ds của danh sách mặc định
                        String idDs = String.valueOf(danhSachMacDinh.get(0).getIdDs());

                        // Kiểm tra xem bài hát đã yêu thích chưa để thêm hay xóa
                        if (isFavorite) {
                            addBaiHatVaoDanhSachMacDinh(idTaiKhoan, idDs);
                        } else {
                            removeBaiHatKhoiDanhSachMacDinh(idTaiKhoan, idDs, idBaiHat);
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Không tìm thấy danh sách mặc định", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi khi tải danh sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LoveLIst>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }


//    =======================

    private void checkIfSongIsFavorite() {

        SharedPrefsHelper sharedPrefsHelper = new SharedPrefsHelper(getApplicationContext());
        String idTaiKhoan = sharedPrefsHelper.getUserId();
        if (idTaiKhoan == null) {
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }


        ApiService apiService = ApiClient.getApiService();
        Call<List<LoveLIst>> call = apiService.getDefaultLoveList(idTaiKhoan);

        call.enqueue(new Callback<List<LoveLIst>>() {
            @Override
            public void onResponse(Call<List<LoveLIst>> call, Response<List<LoveLIst>> response) {
                if (response.isSuccessful()) {
                    List<LoveLIst> danhSachMacDinh = response.body();
                    if (danhSachMacDinh != null && !danhSachMacDinh.isEmpty()) {
                        String idDs = String.valueOf(danhSachMacDinh.get(0).getIdDs());

                        checkIfSongInLoveList(idTaiKhoan, idDs);
                    } else {
                        Toast.makeText(getApplicationContext(), "Không tìm thấy danh sách mặc định", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi khi tải danh sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<LoveLIst>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkIfSongInLoveList(String idTaiKhoan, String idDs) {
        // Kiểm tra xem bài hát có trong danh sách yêu thích không
        ApiService apiService = ApiClient.getApiService();
        Call<List<Song2>> call = apiService.getSongsByLoveList3(idDs);  // Lấy bài hát trong danh sách yêu thích

        call.enqueue(new Callback<List<Song2>>() {
            @Override
            public void onResponse(Call<List<Song2>> call, Response<List<Song2>> response) {
                if (response.isSuccessful()) {
                    List<Song2> songs = response.body();
                    if (songs != null) {

                        for (Song2 song : songs) {
                            int idBaiHatInt = Integer.parseInt(idBaiHat);

                            Log.e("TEST", "idbh" + String.valueOf(idBaiHatInt));
                            Log.e("TEST", "idbh" + idBaiHat);
                            Log.e("TEST", "idbh" + String.valueOf(song.getIdBaiHat()));

                            if (song.getIdBaiHat() == idBaiHatInt) {

                                isFavorite = true;
                                btnFavorite.setImageResource(R.drawable.ic_favorite_filled);  // Cập nhật icon yêu thích
                                return;
                            }
                        }
                    }

                    isFavorite = false;
                    btnFavorite.setImageResource(R.drawable.ic_favorite);
                } else {
                    Toast.makeText(getApplicationContext(), "Lỗi khi kiểm tra bài hát yêu thích", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Song2>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    =====================================================




//    private void showAddToPlaylistDialog() {
//        // Tạo một Dialog mới và sử dụng layout của bạn
//        Dialog dialog = new Dialog(Music_Player.this);
//        dialog.setContentView(R.layout.dialog_add_to_playlist);
//
//        // Điều chỉnh kích thước của Dialog (50% chiều cao màn hình)
//        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//        params.height = (int) (getResources().getDisplayMetrics().heightPixels * 0.5);  // 50% chiều cao
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;  // Chiếm chiều rộng toàn màn hình
//        dialog.getWindow().setAttributes(params);
//
//        // Cài đặt hành vi khi bấm ra ngoài để tắt Dialog
//        dialog.setCanceledOnTouchOutside(true);  // Bấm ra ngoài để đóng dialog
//
//        // Hiển thị dialog
//        dialog.show();
//
//        // Gọi API để lấy danh sách yêu thích trong Dialog
//        ApiService apiService = ApiClient.getApiService();
//        apiService.getLoveListSimple("1").enqueue(new Callback<List<LoveList_Simple>>() {
//            @Override
//            public void onResponse(Call<List<LoveList_Simple>> call, Response<List<LoveList_Simple>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    List<LoveList_Simple> loveList = response.body();
//                    if (loveList.isEmpty()) {
//                        Toast.makeText(Music_Player.this, "Danh sách yêu thích rỗng.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        // Tạo Adapter và thiết lập cho ListView trong Dialog
//                        ListView listView = dialog.findViewById(R.id.lvDS);
//                        ArrayAdapter<LoveList_Simple> adapter = new ArrayAdapter<>(Music_Player.this, android.R.layout.simple_list_item_single_choice, loveList);
//                        listView.setAdapter(adapter);
//                    }
//                } else {
//                    Toast.makeText(Music_Player.this, "Không có dữ liệu.", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<LoveList_Simple>> call, Throwable t) {
//                Toast.makeText(Music_Player.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }




}


