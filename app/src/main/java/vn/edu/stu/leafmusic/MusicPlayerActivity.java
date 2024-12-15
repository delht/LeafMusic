package vn.edu.stu.leafmusic;

import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import vn.edu.stu.leafmusic.adapter.FavoriteAdapter;
import vn.edu.stu.leafmusic.database.DataManager;
import vn.edu.stu.leafmusic.model.Song;
import vn.edu.stu.leafmusic.utils.RotateAnimationHelper;

public class MusicPlayerActivity extends AppCompatActivity {
    private ImageButton btnBack, btnFavorite;
    private ImageView imgSong;
    private TextView tvSongName, tvArtist, tvCurrentTime, tvTotalTime;
    private SeekBar seekBar;
    private ImageButton btnPrev, btnPlayPause, btnNext, btnShuffle, btnRepeat;
    private ImageButton btnAddPlaylist;

    private MediaPlayer mediaPlayer;
    private Handler handler = new Handler();
    private boolean isPlaying = false;
    private String songUrl;
    private boolean isShuffleOn = false;
    private boolean isRepeatOn = false;
    private ArrayList<Song> playlist;
    private int currentSongIndex = 0;
    private Song currentSong;
    private RotateAnimationHelper rotateAnimationHelper;
    private DrawerLayout drawerLayout;
    private TextView tvDetailSongName, tvDetailArtist, tvDetailAlbum,
            tvDetailGenre,tvDetailArea, tvDetailReleaseDate;
    private ArrayList<Song> favoriteSongs = new ArrayList<>();
    private FavoriteAdapter adapter;  // Khai báo adapter


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        rotateAnimationHelper = new RotateAnimationHelper();



        initViews();

        btnBack.setOnClickListener(v -> finish());

        if (getIntent().hasExtra("song_url")) {
            songUrl = getIntent().getStringExtra("song_url");
            String songName = getIntent().getStringExtra("song_name");
            String artist = getIntent().getStringExtra("artist");
            String imageUrl = getIntent().getStringExtra("image_url");
            playlist = getIntent().getParcelableArrayListExtra("playlist");
            currentSongIndex = getIntent().getIntExtra("position", 0);

            if (playlist != null && currentSongIndex >= 0 && currentSongIndex < playlist.size()) {
                currentSong = playlist.get(currentSongIndex); // Khởi tạo currentSong
            }

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

        btnAddPlaylist = findViewById(R.id.btnAddToPlaylist);
        btnAddPlaylist.setOnClickListener(v -> {
            if (currentSong != null) {
                if (isSongInPlaylist(currentSong)) {
                    removeFromPlaylist(currentSong);
                } else {
                    addToPlaylist(currentSong);
                    Intent intent = new Intent(MusicPlayerActivity.this, PlaylistActivity.class);
                    startActivity(intent);
                }
                togglePlaylistButton();
            } else {
                Toast.makeText(this, "Không có bài hát để thêm vào danh sách phát", Toast.LENGTH_SHORT).show();
            }
        });

    }
//     chức năng giao diện nghe nhạc
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

        btnFavorite.setOnClickListener(v -> {
            if (currentSong != null) {
                if (isSongFavorite(currentSong)) {
                    removeFromFavorites(currentSong);
                } else {
                    addToFavorites(currentSong);
                }
                toggleFavoriteButton();
                openFavoriteActivity();
            } else {
                Toast.makeText(this, "Không có bài hát để thêm", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initDetailViews() {
        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view_detail);
        View headerView = navigationView.getHeaderView(0);

        tvDetailSongName = findViewById(R.id.tvDetailSongName);
        tvDetailArtist = findViewById(R.id.tvDetailArtist);
        tvDetailAlbum = findViewById(R.id.tvDetailAlbum);
        tvDetailGenre = findViewById(R.id.tvDetailGenre);
        tvDetailArea = findViewById(R.id.tvDetailArea);
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
            Song currentSong = playlist.get(currentSongIndex);

            tvDetailSongName.setText(currentSong.getTenBaiHat());
            tvDetailArtist.setText(currentSong.getCaSi());
            tvDetailAlbum.setText(currentSong.getAlbum());
            tvDetailGenre.setText(currentSong.getTheLoai());
            tvDetailArea.setText(currentSong.getKhuVucNhac());
            
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

                mediaPlayer.setOnPreparedListener(mp -> {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        tvTotalTime.setText(formatTime(mediaPlayer.getDuration()));
                        seekBar.setMax(mediaPlayer.getDuration());
                        playMusic();
                    });
                });

                mediaPlayer.setOnCompletionListener(mp -> {
                    if (isRepeatOn) {
                        playMusic();
                    } else {
                        playNextSong();
                    }
                });

                mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                    runOnUiThread(() -> {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MusicPlayerActivity.this,
                                "Lỗi phát nhạc: " + what, Toast.LENGTH_SHORT).show();
                    });
                    return true;
                });

                mediaPlayer.setDataSource(songUrl);
                mediaPlayer.prepareAsync();

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MusicPlayerActivity.this,
                            "Không thể phát bài hát này: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
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
        if (playlist != null && !playlist.isEmpty()) {
            if (isShuffleOn) {
                currentSongIndex = new Random().nextInt(playlist.size());
            } else {
                currentSongIndex = (currentSongIndex + 1) % playlist.size();
            }
            playSong(currentSongIndex);
        }
    }

    private void playPreviousSong() {
        if (playlist != null && !playlist.isEmpty()) {
            if (isShuffleOn) {
                currentSongIndex = new Random().nextInt(playlist.size());
            } else {
                currentSongIndex = (currentSongIndex - 1 + playlist.size()) % playlist.size();
            }
            playSong(currentSongIndex);
        }
    }

    private void toggleShuffle() {
        isShuffleOn = !isShuffleOn;
        btnShuffle.setImageResource(isShuffleOn ?
                R.drawable.ic_shuffle_on : R.drawable.ic_shuffle);
    }

    private void toggleRepeat() {
        isRepeatOn = !isRepeatOn;
        btnRepeat.setImageResource(isRepeatOn ?
                R.drawable.ic_repeat_one : R.drawable.ic_repeat);

        if (mediaPlayer != null) {
            mediaPlayer.setLooping(isRepeatOn);
        }
    }

    private void playSong(int position) {
        if (position < 0 || position >= playlist.size()) return;

        try {
            Song song = playlist.get(position);
            songUrl = song.getUrlFile();
            tvSongName.setText(song.getTenBaiHat());
            tvArtist.setText(song.getCaSi());

            if (song.getUrlHinh() != null && !song.getUrlHinh().isEmpty()) {
                Picasso.get().load(song.getUrlHinh()).into(imgSong);
                imgSong.setClipToOutline(true);
            }

            isPlaying = false;
            btnPlayPause.setImageResource(R.drawable.ic_play_circle);
            imgSong.clearAnimation();

            setupMediaPlayer();
            updateSongDetails();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi chuyển bài", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            try {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (imgSong != null) {
            rotateAnimationHelper.stopAnimation(imgSong);
        }
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (imgSong != null) {
            rotateAnimationHelper.pauseAnimation(imgSong);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPlaying && imgSong != null) {
            rotateAnimationHelper.resumeAnimation(imgSong);
        }
    }

    public void openDrawer() {
        if (drawerLayout != null) {
            drawerLayout.openDrawer(GravityCompat.END);
        }
    }

    private void addToFavorites(Song song) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlayPause.setImageResource(R.drawable.ic_play_circle);
            isPlaying = false;
        }

        if (song != null) {
            DataManager.getInstance().addFavoriteSong(song);
            Toast.makeText(this, "Đã thêm vào danh sách yêu thích", Toast.LENGTH_SHORT).show();
            
            Intent intent = new Intent("UPDATE_FAVORITE_LIST");
            sendBroadcast(intent);
            
            favoriteSongs = DataManager.getInstance().getFavoriteSongs();
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        } else {
            Toast.makeText(this, "Không có bài hát để thêm", Toast.LENGTH_SHORT).show();
        }
    }

    private void openFavoriteActivity() {
        Intent intent = new Intent(this, FavoriteActivity.class);


        intent.putParcelableArrayListExtra("favorite_songs", favoriteSongs);
        startActivity(intent);
    }

    private void toggleFavoriteButton() {
        if (isSongFavorite(currentSong)) {
            btnFavorite.setImageResource(R.drawable.ic_favorite_filled);
        } else {
            btnFavorite.setImageResource(R.drawable.ic_favorite);
        }
    }

    private boolean isSongFavorite(Song song) {
        return DataManager.getInstance().getFavoriteSongs().contains(song);
    }

    private void removeFromFavorites(Song song) {
        DataManager.getInstance().removeFavoriteSong(song);
        Toast.makeText(this, "Đã xóa khỏi danh sách yêu thích", Toast.LENGTH_SHORT).show();
        
        // Thông báo cho MainActivity cập nhật danh sách yêu thích
        Intent intent = new Intent("UPDATE_FAVORITE_LIST");
        sendBroadcast(intent);
    }

    private void addToPlaylist(Song song) {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            btnPlayPause.setImageResource(R.drawable.ic_play_circle);
            isPlaying = false;
        }
        DataManager.getInstance().addSongToPlaylist(song);
        Toast.makeText(this, "Đã thêm vào danh sách phát", Toast.LENGTH_SHORT).show();
        
        Intent intent = new Intent("UPDATE_PLAYLIST");
        sendBroadcast(intent);
    }

    private boolean isSongInPlaylist(Song song) {
        return DataManager.getInstance().getPlaylistSongs().contains(song);
    }

    private void removeFromPlaylist(Song song) {
        DataManager.getInstance().removeSongFromPlaylist(song);
        Toast.makeText(this, "Đã xóa khỏi danh sách phát", Toast.LENGTH_SHORT).show();
    }

    private void togglePlaylistButton() {
        if (isSongInPlaylist(currentSong)) {
            btnAddPlaylist.setImageResource(R.drawable.ic_playlist_check);
        } else {
            btnAddPlaylist.setImageResource(R.drawable.ic_playlist);
        }
    }

}