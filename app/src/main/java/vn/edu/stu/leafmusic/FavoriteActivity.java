package vn.edu.stu.leafmusic;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.stu.leafmusic.adapter.FavoriteAdapter;
import vn.edu.stu.leafmusic.database.ApiClient;
import vn.edu.stu.leafmusic.database.ApiService;
import vn.edu.stu.leafmusic.model.Song;

public class FavoriteActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private EditText edtSearch;
    private Toolbar toolbar;
    private RecyclerView recyclerViewFavorites;
    private ArrayList<Song> favoriteSongs;
    private FavoriteAdapter adapter;
    private ProgressBar progressBar;

    private BroadcastReceiver favoriteUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            loadFavoriteSongsFromAPI();
        }
    };

    private FavoriteAdapter.OnSongClickListener songClickListener = new FavoriteAdapter.OnSongClickListener() {
        @Override
        public void onSongClick(Song song, int position) {
        }
    };

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
        setContentView(R.layout.activity_favorite);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        recyclerViewFavorites = findViewById(R.id.recyclerViewFavorites);

        Toolbar toolbar = findViewById(R.id.toolbar);
        edtSearch = findViewById(R.id.edtSearch);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        recyclerViewFavorites.setLayoutManager(new LinearLayoutManager(this));
        loadFavoriteSongsFromAPI();

        favoriteSongs = getIntent().getParcelableArrayListExtra("favorite_songs");
        if (favoriteSongs == null) {
            favoriteSongs = new ArrayList<>();
        }

        adapter = new FavoriteAdapter(favoriteSongs, songClickListener);
        recyclerViewFavorites.setAdapter(adapter);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        edtSearch.setOnClickListener(view -> {
            Intent intent=new Intent(FavoriteActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_profile) {
                    Intent intent = new Intent(FavoriteActivity.this, ProfileActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_home) {
                    Intent intent = new Intent(FavoriteActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.nav_logout) {
                    showLogoutDialog();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        registerReceiver(favoriteUpdateReceiver, new IntentFilter("UPDATE_FAVORITE_LIST"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(favoriteUpdateReceiver);
    }

    private void loadFavoriteSongsFromAPI() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Song>> call = apiService.getFavoriteSongs();
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favoriteSongs = new ArrayList<>(response.body());
                    adapter = new FavoriteAdapter(favoriteSongs, songClickListener);
                    recyclerViewFavorites.setAdapter(adapter);
                    Log.d("FavoriteActivity", "Số bài hát yêu thích: " + favoriteSongs.size());
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.e("FavoriteActivity", "Lỗi khi gọi API: " + t.getMessage());
                Toast.makeText(FavoriteActivity.this, "Lỗi kết nối với máy chủ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchFavoriteSongs() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Song>> call = apiService.getFavoriteSongs();
        call.enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                if (response.isSuccessful()) {
                    List<Song> favoriteSongs = response.body();
                    updateFavoriteList(favoriteSongs);
                } else {
                    Log.e("FavoriteActivity", "Lỗi khi lấy danh sách bài hát yêu thích: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {
                Log.e("FavoriteActivity", "Lỗi kết nối: " + t.getMessage());
            }
        });
    }

    private void updateFavoriteList(List<Song> favoriteSongs) {
        if (favoriteSongs != null && !favoriteSongs.isEmpty()) {
            FavoriteAdapter adapter = new FavoriteAdapter(favoriteSongs, songClickListener);
            recyclerViewFavorites.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(this, "Không có bài hát yêu thích", Toast.LENGTH_SHORT).show();
        }
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng Xuất")
                .setMessage("Bạn chắc chắn muốn đăng xuất?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(FavoriteActivity.this, "Đã Đăng Xuất", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(FavoriteActivity.this, LoginActivity.class);
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
