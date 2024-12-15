package vn.edu.stu.leafmusic;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import vn.edu.stu.leafmusic.adapter.SongAdapter;
import vn.edu.stu.leafmusic.database.DataManager;
import vn.edu.stu.leafmusic.model.Artist;
import vn.edu.stu.leafmusic.model.Song;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private EditText edtSearch;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private List<Song> allSongs;

    private BroadcastReceiver favoriteUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Cập nhật danh sách yêu thích
            fetchSongsFromApi(); // Hoặc gọi phương thức nào đó để cập nhật danh sách
        }
    };

    private BroadcastReceiver playlistUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Cập nhật danh sách phát nếu cần
            updatePlaylist();
        }
    };

    //    toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            edtSearch = findViewById(R.id.edtSearch);
            recyclerView = findViewById(R.id.recyclerView);

            try {
                toolbar = findViewById(R.id.toolbar);
                if (toolbar != null) {
                    setSupportActionBar(toolbar);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().setTitle(null);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
                    }
                } else {
                    Log.e("MainActivity", "Toolbar not found in layout");
                    Toast.makeText(this, "Lỗi: Không tìm thấy toolbar", Toast.LENGTH_LONG).show();
                    return;
                }
            } catch (Exception e) {
                Log.e("MainActivity", "Error setting up toolbar: " + e.getMessage());
                Toast.makeText(this, "Lỗi khởi tạo toolbar: " + e.getMessage(), Toast.LENGTH_LONG).show();
                return;
            }

            drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
            );
            drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();

            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            fetchSongsFromApi();

            setupListeners();

            registerReceiver(favoriteUpdateReceiver, new IntentFilter("UPDATE_FAVORITE_LIST"));
            registerReceiver(playlistUpdateReceiver, new IntentFilter("UPDATE_PLAYLIST"));

        } catch (Exception e) {
            Log.e("MainActivity", "Critical error in onCreate: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khởi tạo ứng dụng: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(favoriteUpdateReceiver);
        unregisterReceiver(playlistUpdateReceiver);
    }

    private void setupListeners() {
        // Search
        edtSearch.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        // Navigation
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            } else if (id == R.id.nav_favorite) {
                startActivity(new Intent(MainActivity.this, FavoriteActivity.class));

            } else if (id == R.id.nav_home) {
            } else if (id == R.id.nav_logout) {
                showLogoutDialog();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

//    API
    private void fetchSongsFromApi() {
        new Thread(() -> {
            try {
                String apiUrl = "https://67597e7d099e3090dbe1e2a6.mockapi.io/api/baihat/getBH/id/1";
                String jsonString = fetchDataFromApi(apiUrl);

                Log.d("API Response", jsonString);

                if (jsonString == null || jsonString.isEmpty()) {
                    throw new IOException("Dữ liệu trả về từ API là null ");
                }

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Song>>(){}.getType();
                List<Song> songs = gson.fromJson(jsonString, listType);

                if (songs == null || songs.isEmpty()) {
                    throw new IOException("Danh sách bài hát rỗng");
                }

                allSongs = new ArrayList<>(songs);

                runOnUiThread(() -> {
                    adapter = new SongAdapter(allSongs, (song, position) -> {
                        Intent intent = new Intent(MainActivity.this, MusicPlayerActivity.class);
                        intent.putExtra("song_url", song.getUrlFile());
                        intent.putExtra("song_name", song.getTenBaiHat());
                        intent.putExtra("artist", song.getCaSi());
                        intent.putExtra("image_url", song.getUrlHinh());
                        intent.putParcelableArrayListExtra("playlist", new ArrayList<>(allSongs));
                        intent.putExtra("position", position);
                        startActivity(intent);
                    });
                    recyclerView.setAdapter(adapter);
                });

            } catch (Exception e) {
                Log.e("MainActivity", "Error loading songs: " + e.getMessage());
                runOnUiThread(() -> Toast.makeText(this, "Không thể tải danh sách bài hát: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }

    //    Đăng xuất
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng Xuất")
                .setMessage("Bạn chắc chắn muốn đăng xuất?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MainActivity.this, "Đã Đăng Xuất", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private String fetchDataFromApi(String apiUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(apiUrl).build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                Log.e("API Error", "Unexpected code " + response);
                throw new IOException("Unexpected code " + response);
            }
            return response.body().string();
        }
    }

    private void updatePlaylist() {
        ArrayList<Song> playlist = DataManager.getInstance().getPlaylistSongs();
    }
}