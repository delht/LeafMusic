package vn.edu.stu.leafmusic.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.api.dto.ApiService;
import vn.edu.stu.leafmusic.api.dto.request.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {
    EditText edtUser, edtPass, edtRePass;
    Button btnRegister, btnLogin;

    private static final String BASE_URL = "http://192.168.83.1:8080/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        AddControls();
        AddEvents();

    }



    private void AddControls() {
        edtUser=findViewById(R.id.edtUsername);
        edtPass=findViewById(R.id.edtPassWord);
        edtRePass=findViewById(R.id.edtRePassWord);
        btnRegister=findViewById(R.id.btnRegister);
        btnLogin=findViewById(R.id.btnLogin);

    }

    private void AddEvents() {
        btnRegister.setOnClickListener(view ->{
            String username = edtUser.getText().toString().trim();
            String password = edtPass.getText().toString().trim();
            String repass = edtRePass.getText().toString().trim();

            if (TextUtils.isEmpty(username)) {
                edtUser.setError("Vui lòng nhập tên đăng nhâp!");
                edtUser.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(repass)) {
                edtRePass.setError("Vui lòng nhập lại mật khẩu");
                edtRePass.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(password)) {
                edtPass.setError("Vui lòng nhập mật khẩu!");
                edtPass.requestFocus();
                return;
            }

            if (!password.equals(repass)) {
                edtRePass.setError("Mật khẩu không khớp!");
                edtRePass.requestFocus();
                return;
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);

            // Tạo request
            RegisterRequest registerRequest = new RegisterRequest(username, password);

            // Gọi API
            apiService.register(registerRequest).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        try {
                            // Lấy thông báo lỗi từ body của response
                            String errorMessage = response.errorBody().string();
                            Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        btnLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }








}