package vn.edu.stu.leafmusic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import vn.edu.stu.leafmusic.adapter.SongAdapter;
import vn.edu.stu.leafmusic.model.Artist;
import vn.edu.stu.leafmusic.model.Song;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private EditText edtSearch;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private SongAdapter adapter;
    private List<Song> allSongs;

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
            // Khởi tạo các view
            drawerLayout = findViewById(R.id.drawer_layout);
            navigationView = findViewById(R.id.nav_view);
            edtSearch = findViewById(R.id.edtSearch);
            recyclerView = findViewById(R.id.recyclerView);
            
            // Khởi tạo toolbar riêng
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

            // Thiết lập navigation drawer
            drawerToggle = new ActionBarDrawerToggle(
                this, 
                drawerLayout, 
                toolbar,
                R.string.open, 
                R.string.close
            );
            drawerLayout.addDrawerListener(drawerToggle);
            drawerToggle.syncState();

            // Thiết lập RecyclerView
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            loadSongsFromJson();

            // Thiết lập các listener
            setupListeners();

        } catch (Exception e) {
            Log.e("MainActivity", "Critical error in onCreate: " + e.getMessage());
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khởi tạo ứng dụng: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
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
            } else if (id == R.id.nav_home) {
                // Đã ở MainActivity, không cần làm gì
            } else if (id == R.id.nav_logout) {
                showLogoutDialog();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void loadSongsFromJson() {
        try {
            InputStream is = getAssets().open("sample.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonString = new String(buffer, StandardCharsets.UTF_8);

            Gson gson = new Gson();
            List<Artist> artists = gson.fromJson(jsonString, new TypeToken<List<Artist>>(){}.getType());
            
            allSongs = new ArrayList<>();
            for (Artist artist : artists) {
                allSongs.addAll(artist.getBaiHats());
            }

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

        } catch (Exception e) {
            Log.e("MainActivity", "Error loading songs: " + e.getMessage());
            Toast.makeText(this, "Không thể tải danh sách bài hát: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
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
}