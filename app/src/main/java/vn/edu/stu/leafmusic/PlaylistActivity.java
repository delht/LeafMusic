package vn.edu.stu.leafmusic;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
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

import vn.edu.stu.leafmusic.adapter.SongAdapter;
import vn.edu.stu.leafmusic.database.DataManager;
import vn.edu.stu.leafmusic.model.Song;

public class PlaylistActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private EditText edtSearch;
    private Toolbar toolbar;
    private RecyclerView recyclerViewPlaylist;
    private SongAdapter adapter;
    private ArrayList<Song> playlistSongs;

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
        setContentView(R.layout.activity_playlist);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        edtSearch = findViewById(R.id.edtSearch);
        recyclerViewPlaylist = findViewById(R.id.recyclerViewFavorites);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        recyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(this));
        playlistSongs = DataManager.getInstance().getPlaylistSongs();
        adapter = new SongAdapter(playlistSongs, (song, position) -> {
            Intent intent = new Intent(PlaylistActivity.this, MusicPlayerActivity.class);
            intent.putExtra("song_url", song.getUrlFile());
            intent.putExtra("song_name", song.getTenBaiHat());
            intent.putExtra("artist", song.getCaSi());
            intent.putExtra("image_url", song.getUrlHinh());
            intent.putParcelableArrayListExtra("playlist", playlistSongs);
            intent.putExtra("position", position);
            startActivity(intent);
        });
        recyclerViewPlaylist.setAdapter(adapter);

        edtSearch.setOnClickListener(view -> {
            Intent intent = new Intent(PlaylistActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_profile) {
                Intent intent = new Intent(PlaylistActivity.this, ProfileActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_home) {
                Intent intent = new Intent(PlaylistActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {
                showLogoutDialog();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng Xuất")
                .setMessage("Bạn chắc chắn muốn đăng xuất?")
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    Toast.makeText(PlaylistActivity.this, "Đã Đăng Xuất", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PlaylistActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
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
