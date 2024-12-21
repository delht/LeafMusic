package vn.edu.stu.leafmusic.activity.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.activity.MainActivity;
import vn.edu.stu.leafmusic.activity.UI.Home;
import vn.edu.stu.leafmusic.activity.UI.ResetPasswordActivity;
import vn.edu.stu.leafmusic.api.dto.ApiService;
import vn.edu.stu.leafmusic.api.dto.reponse.LoginResponse;
import vn.edu.stu.leafmusic.api.dto.request.LoginRequest;
import vn.edu.stu.leafmusic.util.SharedPrefsHelper;

public class LoginActivity extends AppCompatActivity {

    private static final String BASE_URL = "http://192.168.83.1:8080/";
//    private static final String BASE_URL = "http://192.168.1.11:8080/";

    EditText edtUser, edtPass;
    Button btnLogin, btnCreate;
    TextView tvForgot;

    private SharedPrefsHelper sharedPrefsHelper;

//    ================================================================================
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Controls();
        // Kiem tra xem co dang nhap ko
        if (sharedPrefsHelper.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        Events();
    }

    private void Controls() {
        sharedPrefsHelper = new SharedPrefsHelper(this);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnCreate = findViewById(R.id.btnCreate);
        tvForgot = findViewById(R.id.tvForgotPass);
        btnLogin = findViewById(R.id.btnLogin);
    }

    private void Events(){
        btnLogin.setOnClickListener(v -> login());
        tvForgot.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, ResetPasswordActivity.class);
            startActivity(intent);
        });

        btnCreate.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void login() {
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

        if (pass.length() < 8) {
            edtPass.setError("Mật khẩu phải có ít nhất 8 ký tự!");
            edtPass.requestFocus();
            return;
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        LoginRequest loginRequest = new LoginRequest(username, pass);

        // gui yeu cau dang nhap len api
        apiService.login(loginRequest).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse loginResponse = response.body();
                    if (loginResponse != null) {

                        //luu trang thai dang nhap
                        sharedPrefsHelper.saveLoginState(
                                loginResponse.getId_taikhoan(),
                                loginResponse.getUsername(),
                                true
                        );

                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Tên đăng nhập hoặc mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
