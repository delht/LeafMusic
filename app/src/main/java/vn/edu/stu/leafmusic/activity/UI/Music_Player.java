package vn.edu.stu.leafmusic.activity.UI;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.model.Song2;
import vn.edu.stu.leafmusic.util.RotateAnimationHelper;

public class Music_Player extends AppCompatActivity {

    private ImageButton btnBack, btnFavorite;
    private ImageView imgSong;
    private TextView tvSongName, tvArtist, tvCurrentTime, tvTotalTime;
    private SeekBar seekBar;
    private ImageButton btnPrev, btnPlayPause, btnNext, btnShuffle, btnRepeat;

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private boolean isPlaying = false;
    private String songUrl;
    private boolean isShuffleOn = false;
    private boolean isRepeatOn = false;
    private ArrayList<Song2> playlist;
    private int currentSongIndex = 0;
    private RotateAnimationHelper rotateAnimationHelper;
    private DrawerLayout drawerLayout;
    private TextView tvDetailSongName, tvDetailArtist, tvDetailAlbum,
            tvDetailGenre, tvDetailReleaseDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        rotateAnimationHelper = new RotateAnimationHelper();

        initViews();

        btnBack.setOnClickListener(v -> finish());

//============================

        playlist = getIntent().getParcelableArrayListExtra("playlist");
        currentSongIndex = getIntent().getIntExtra("position", 0);

        if (playlist != null && !playlist.isEmpty()) {
            Song2 currentSong = playlist.get(currentSongIndex);

            // Hiển thị thông tin bài hát
            tvSongName.setText(currentSong.getTenBaiHat());
            tvArtist.setText(currentSong.getCaSi());
            Picasso.get().load(currentSong.getUrlHinh()).into(imgSong);

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
        }
    }


    private void toggleShuffle() {
        isShuffleOn = !isShuffleOn;
        btnShuffle.setImageResource(isShuffleOn ? R.drawable.ic_shuffle_on : R.drawable.ic_repeat);
    }

    private void toggleRepeat() {
        isRepeatOn = !isRepeatOn;
        btnRepeat.setImageResource(isRepeatOn ? R.drawable.ic_repeat_one : R.drawable.ic_repeat);
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


    

}


