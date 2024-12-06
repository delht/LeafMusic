package vn.edu.stu.leafmusic.activity.UI;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.activity.login.LoginActivity;
import vn.edu.stu.leafmusic.util.SharedPrefsHelper;

public class Home extends AppCompatActivity {

    TextView tvTest;

    private SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tvTest = findViewById(R.id.tvTEST);

        sharedPrefsHelper = new SharedPrefsHelper(this);
        String id = sharedPrefsHelper.getUserId();

        tvTest.setText(id);

        // Xử lý nút đăng xuất
        tvTest.setOnClickListener(v -> {
            // Xóa dữ liệu SharedPreferences
            sharedPrefsHelper.clearLoginState();

            // Điều hướng về màn hình đăng nhập
            Intent intent = new Intent(Home.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
