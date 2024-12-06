package vn.edu.stu.leafmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ProfileActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imgAvatar;
    private TextView tvUsername, tvEmail, tvPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Ánh xạ view
        toolbar = findViewById(R.id.toolbar);
        imgAvatar = findViewById(R.id.imgAvatar);
        tvUsername = findViewById(R.id.edtFullName);
        tvEmail = findViewById(R.id.edtEmail);
        tvPhone = findViewById(R.id.edtPhone);

        // Thiết lập toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Thông tin cá nhân");

        // Hiển thị thông tin người dùng (có thể lấy từ SharedPreferences hoặc API)
        tvUsername.setText("Người dùng");
        tvEmail.setText("user@example.com");
        tvPhone.setText("0123456789");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 