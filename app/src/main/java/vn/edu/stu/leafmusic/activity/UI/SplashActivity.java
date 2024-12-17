package vn.edu.stu.leafmusic.activity.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.activity.MainActivity;
import vn.edu.stu.leafmusic.activity.login.LoginActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        // Set up window insets to handle system bars padding
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Áp dụng hiệu ứng slide-in cho logo
        ImageView logoImage = findViewById(R.id.logoImage);
        TranslateAnimation slideIn = new TranslateAnimation(
                Animation.ABSOLUTE, 0f,  // Từ vị trí ban đầu
                Animation.ABSOLUTE, 0f,
                Animation.ABSOLUTE, 500f,  // Lướt từ dưới lên 500px
                Animation.ABSOLUTE, 0f);
        slideIn.setDuration(2000);  // Thời gian lướt (2 giây)
        logoImage.startAnimation(slideIn);

        // Sau 3 giây chuyển sang MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Đóng SplashActivity
            }
        }, 1000);  // Delay 3 giây
    }
}
