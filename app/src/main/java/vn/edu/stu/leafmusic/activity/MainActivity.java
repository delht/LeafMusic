package vn.edu.stu.leafmusic.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import vn.edu.stu.leafmusic.R;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        edtSearch = findViewById(R.id.edtSearch);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        // Cập nhật tên người dùng vào header của NavigationView
        updateUserInfo();

        edtSearch.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        // Menu navigation
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.account) {
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                startActivity(intent);
            } else if (id == R.id.home) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
            } else if (id == R.id.logout) {
                showLogoutDialog();
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    // Đăng xuất
    private void showLogoutDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng Xuất")
                .setMessage("Bạn chắc chắn muốn đăng xuất?")
                .setPositiveButton("Có", (dialogInterface, i) -> {
                    // Xóa dữ liệu đăng nhập khỏi SharedPreferences
                    getSharedPreferences("user_prefs", MODE_PRIVATE)
                            .edit()
                            .clear() // Xóa tất cả dữ liệu
                            .apply();

                    Toast.makeText(MainActivity.this, "Đã Đăng Xuất", Toast.LENGTH_SHORT).show();

                    // Chuyển về LoginActivity
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish(); // Đóng MainActivity
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

    // Cập nhật tên người dùng trong NavigationView
    private void updateUserInfo() {
        // Lấy thông tin người dùng từ SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "Chưa có tên người dùng");

        // Lấy header của NavigationView và tìm TextView
        View headerView = navigationView.getHeaderView(0);
        TextView tvMenuUsername = headerView.findViewById(R.id.tvMenuUsername);

        // Cập nhật tên người dùng vào TextView
        tvMenuUsername.setText(username);
    }
}
