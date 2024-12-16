package vn.edu.stu.leafmusic.activity.UI;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
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
import vn.edu.stu.leafmusic.model.Song2;
import vn.edu.stu.leafmusic.util.RotateAnimationHelper;
import vn.edu.stu.leafmusic.util.SharedPrefsHelper;

public class Music_Player extends AppCompatActivity {

    private ImageButton btnBack, btnFavorite;
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

        rotateAnimationHelper = new RotateAnimationHelper();

        initViews();

        btnBack.setOnClickListener(v -> finish());

        initFavoriteButton();

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
            Picasso.get().load(currentSong.getUrlHinh()).into(imgSong);


            idBaiHat = String.valueOf(currentSong.getIdBaiHat());
            Log.e("TEST", "id bai hat: "+currentSong.getIdBaiHat());

            songUrl = currentSong.getUrlFile(); // Lấy URL bài hát
            setupMediaPlayer();
        }

//============================



        if (getIntent().hasExtra("song_url")) {
            songUrl = getIntent().getStringExtra("song_url");
            String songName = getIntent().getStringExtra("song_name");
            String artist = getIntent().getStringExtra("artist");
            String imageUrl = getIntent().getStringExtra("image_url");
            playlist = getIntent().getParcelableArrayListExtra("playlist");
            currentSongIndex = getIntent().getIntExtra("position", 0);

            // Hiển thị thông tin bài hát
            tvSongName.setText(songName);
            tvArtist.setText(artist);
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get().load(imageUrl).into(imgSong);
            }

            setupMediaPlayer();
        }

        setupListeners();

        initDetailViews();

        updateSongDetails();

        drawerLayout = findViewById(R.id.drawerLayout);

        // Cho phép vuốt từ cạnh phải
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            drawerLayout.setSystemGestureExclusionRects(Collections.emptyList());
        }

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                View contentView = findViewById(R.id.rootLayout);
                float slideX = drawerView.getWidth() * slideOffset;
                contentView.setTranslationX(-slideX);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {}

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {}

            @Override
            public void onDrawerStateChanged(int newState) {}
        });


    }

    private void initViews() {
        btnBack = findViewById(R.id.btnBack);
        btnFavorite = findViewById(R.id.btnFavorite);
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

    private void initDetailViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view_detail);
        View headerView = navigationView.getHeaderView(0);

        tvDetailSongName = findViewById(R.id.tvDetailSongName);
        tvDetailArtist = findViewById(R.id.tvDetailArtist);
        tvDetailAlbum = findViewById(R.id.tvDetailAlbum);
        tvDetailGenre = findViewById(R.id.tvDetailGenre);
        tvDetailReleaseDate = findViewById(R.id.tvDetailReleaseDate);

        drawerLayout.setScrimColor(Color.TRANSPARENT);
        drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                View contentView = findViewById(R.id.rootLayout);
                float slideX = drawerView.getWidth() * slideOffset;
                contentView.setTranslationX(-slideX);
                contentView.setScaleX(1 - (slideOffset * 0.1f));
                contentView.setScaleY(1 - (slideOffset * 0.1f));

                drawerView.setTranslationX(drawerView.getWidth() * (1 - slideOffset));
            }
        });
    }

    private void updateSongDetails() {
        if (playlist != null && currentSongIndex >= 0 && currentSongIndex < playlist.size()) {
            Song2 currentSong = playlist.get(currentSongIndex);

            tvDetailSongName.setText(currentSong.getTenBaiHat());
            tvDetailArtist.setText(currentSong.getCaSi());
            tvDetailReleaseDate.setText(currentSong.getFormattedReleaseDate());
        }
    }


    private void setupMediaPlayer() {
        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            try {
                if (mediaPlayer != null) {
                    try {
                        if (mediaPlayer.isPlaying()) {
                            mediaPlayer.stop();
                        }
                        mediaPlayer.reset();
                        mediaPlayer.release();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mediaPlayer = null;
                }

                mediaPlayer = new MediaPlayer();
                mediaPlayer.setDataSource(songUrl);
                mediaPlayer.setOnPreparedListener(mp -> {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        tvTotalTime.setText(formatTime(mediaPlayer.getDuration()));
                        seekBar.setMax(mediaPlayer.getDuration());
                        playMusic();
                    });
                });

                // Thêm OnCompletionListener để chuyển sang bài tiếp theo khi bài hát kết thúc
                mediaPlayer.setOnCompletionListener(mp -> {
                    if (isShuffleOn) {
                        playNextSongRandom(); // Chế độ Shuffle: phát bài hát ngẫu nhiên
                    } else {
                        playNextSong(); // Chế độ bình thường: phát bài hát tiếp theo
                    }
                });


                mediaPlayer.setOnCompletionListener(mp -> {
                    if (isShuffleOn) {
                        playNextSongRandom(); // Chế độ Shuffle: phát bài hát ngẫu nhiên
                    } else if (isRepeatOn) {
                        // Chế độ lặp lại một bài hát: phát lại bài hát hiện tại
                        playMusic();
                    } else {
                        // Chế độ bình thường: phát bài hát tiếp theo
                        playNextSong();
                    }
                });

                mediaPlayer.setOnCompletionListener(mp -> {
                    if (isShuffleOn) {
                        playNextSongRandom(); // Chế độ Shuffle: phát bài hát ngẫu nhiên
                    } else if (isRepeatOn) {
                        // Chế độ lặp lại một bài hát: phát lại bài hát hiện tại
                        playMusic();
                    } else {
                        // Chế độ bình thường: phát bài hát tiếp theo
                        playNextSong();
                    }
                });




                mediaPlayer.prepareAsync();





            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(Music_Player.this, "Không thể phát bài hát này", Toast.LENGTH_SHORT).show();
                });
            }
        }).start();
    }



    private void setupListeners() {
        btnPlayPause.setOnClickListener(v -> {
            if (isPlaying) {
                pauseMusic();
            } else {
                playMusic();
            }
        });

        btnNext.setOnClickListener(v -> playNextSong());
        btnPrev.setOnClickListener(v -> playPreviousSong());
        btnShuffle.setOnClickListener(v -> toggleShuffle());
        btnRepeat.setOnClickListener(v -> toggleRepeat());

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                    tvCurrentTime.setText(formatTime(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnBack.setOnClickListener(v -> onBackPressed());

    }

    private void playMusic() {
        try {
            if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
                mediaPlayer.start();
                btnPlayPause.setImageResource(R.drawable.ic_pause);
                isPlaying = true;
                updateSeekBar();
                // Bắt đầu animation
                if (imgSong != null) {
                    rotateAnimationHelper.startAnimation(imgSong);
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi phát nhạc", Toast.LENGTH_SHORT).show();
        }
    }

    private void pauseMusic() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.pause();
                btnPlayPause.setImageResource(R.drawable.ic_play_circle);
                isPlaying = false;
                if (imgSong != null) {
                    rotateAnimationHelper.pauseAnimation(imgSong);
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi tạm dừng", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateSeekBar() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    tvCurrentTime.setText(formatTime(mediaPlayer.getCurrentPosition()));
                    if (isPlaying) {
                        handler.postDelayed(this, 1000);
                    }
                }
            }
        }, 1000);
    }

    private String formatTime(int milliseconds) {
        int seconds = (milliseconds / 1000) % 60;
        int minutes = (milliseconds / (1000 * 60)) % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void playNextSong() {
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
        isShuffleOn = !isShuffleOn;
        if (isShuffleOn) {
            btnShuffle.setImageResource(R.drawable.ic_shuffle_on);  // Thay đổi hình ảnh của nút khi bật chế độ ngẫu nhiên
        } else {
            btnShuffle.setImageResource(R.drawable.ic_shuffle);  // Thay đổi hình ảnh của nút khi tắt chế độ ngẫu nhiên
        }
    }


    private void toggleRepeat() {
        isRepeatOn = !isRepeatOn;
        if (isRepeatOn) {
            btnRepeat.setImageResource(R.drawable.ic_repeat_one);  // Thay đổi hình ảnh của nút khi bật chế độ lặp lại một bài
        } else {
            btnRepeat.setImageResource(R.drawable.ic_repeat);  // Thay đổi hình ảnh của nút khi tắt chế độ lặp lại
        }
    }


    private void playSong(int index) {
        if (playlist != null && index >= 0 && index < playlist.size()) {
            Song2 song = playlist.get(index);
            songUrl = song.getUrlFile();
            setupMediaPlayer();
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

        // Lấy danh sách yêu thích mặc định và thực hiện thêm hoặc xóa
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






}


