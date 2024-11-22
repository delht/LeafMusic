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

import com.google.android.material.navigation.NavigationView;

public class UserActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;
    private EditText edtSearch;
    private Toolbar toolbar;

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
        setContentView(R.layout.activity_user);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        Toolbar toolbar = findViewById(R.id.toolbar);
        edtSearch=findViewById(R.id.edtSearch);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);


        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        }

        edtSearch.setOnClickListener(view -> {
            Intent intent=new Intent(UserActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.tvListLike).setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, FavoriteActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.tvLibrary).setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, LibraryActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.tvAlbum).setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, AlbumActivity.class);
            startActivity(intent);
        });
        findViewById(R.id.tvListCare).setOnClickListener(v -> {
            Intent intent = new Intent(UserActivity.this, CareActivity.class);
            startActivity(intent);
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.account) {
                    Intent intent = new Intent(UserActivity.this, UserActivity.class);
                    startActivity(intent);
                } else if (id == R.id.home) {
                    Intent intent = new Intent(UserActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (id == R.id.logout) {
                    showLogoutDialog();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng Xuất")
                .setMessage("Bạn chắc chắn muốn đăng xuất?")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(UserActivity.this, "Đã Đăng Xuất", Toast.LENGTH_SHORT).show();

                        Intent intent=new Intent(UserActivity.this, LoginActivity.class);
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
