package vn.edu.stu.leafmusic;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class LoginActivity extends AppCompatActivity {
    EditText edtUser, edtPass;
    Button btnLogin, btnCreate;
    TextView tvForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LoginActivity", "onCreate called");
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        AddControls();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("LoginActivity", "onDestroy called");
    }

    private void AddControls() {
        edtUser=findViewById(R.id.edtUser);
        edtPass=findViewById(R.id.edtPass);
        btnCreate=findViewById(R.id.btnCreate);
        tvForgot=findViewById(R.id.tvForgotPass);
        btnLogin=findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });

        tvForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, ForgotPass.class);
                startActivity(intent);            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });
    }

    private void Login() {
        try {
            String username = edtUser.getText().toString().trim();
            String pass = edtPass.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                edtUser.setError("Vui lòng nhập tên đăng nhập!");
                edtUser.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(pass)) {
                edtPass.setError("Vui lòng nhập mật khẩu!");
                edtPass.requestFocus();
                return;
            }

            if (username.equals("tue") && pass.equals("123")) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP 
                            | Intent.FLAG_ACTIVITY_NEW_TASK 
                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                
                Toast.makeText(getApplicationContext(), 
                    "Đăng nhập thành công", 
                    Toast.LENGTH_SHORT).show();
                
                startActivity(intent);
                finishAffinity();
            } else {
                Toast.makeText(getApplicationContext(), 
                    "Tên đăng nhập hoặc mật khẩu không đúng!!", 
                    Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("LoginActivity", "Error during login: " + e.getMessage());
            Toast.makeText(getApplicationContext(), 
                "Có lỗi xảy ra khi đăng nhập", 
                Toast.LENGTH_SHORT).show();
        }
    }
}