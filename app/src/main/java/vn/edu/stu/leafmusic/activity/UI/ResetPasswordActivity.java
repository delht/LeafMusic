package vn.edu.stu.leafmusic.activity.UI;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.api.dto.ApiService;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText edtEmail, edtCode, edtNewPass;
    private Button btnSendCode, btnReset;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);


        edtEmail = findViewById(R.id.edtEmail);
        edtCode = findViewById(R.id.edtCode);
        edtNewPass = findViewById(R.id.edtNewPass);
        btnSendCode = findViewById(R.id.btnSendCode);
        btnReset = findViewById(R.id.btnReset);


        apiService = ApiClient.getApiService();


        btnSendCode.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            if (email.isEmpty()) {
                edtEmail.setError("Email không được để trống");
                return;
            }
            sendVerificationCode(email);
        });


        btnReset.setOnClickListener(v -> {
            String email = edtEmail.getText().toString();
            String code = edtCode.getText().toString();
            String newPassword = edtNewPass.getText().toString();

            if (email.isEmpty() || code.isEmpty() || newPassword.isEmpty()) {
                Toast.makeText(ResetPasswordActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (newPassword.length() < 8) {
                edtNewPass.setError("Mật khẩu phải có ít nhất 8 ký tự");
                edtNewPass.requestFocus();
                return;
            }

            resetPassword(code, newPassword, email);
        });
    }

    // Gửi mã xác nhận đến email
    private void sendVerificationCode(String email) {
        Call<ResponseBody> call = apiService.forgotPassword(email);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {

                    edtCode.setVisibility(View.VISIBLE);
                    edtNewPass.setVisibility(View.VISIBLE);
                    btnReset.setVisibility(View.VISIBLE);
                    Toast.makeText(ResetPasswordActivity.this, "Mã xác nhận đã được gửi đến email", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ResetPasswordActivity.this, "Lỗi: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, "Có lỗi xảy ra: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Đặt lại mật khẩu
    private void resetPassword(String code, String newPassword, String email) {
        Call<ResponseBody> call = apiService.resetPassword(code, newPassword, email);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "Mật khẩu đã được đổi thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    try {
                        String errorBody = response.errorBody().string();
                        Log.e("ResetPassword", "Error body: " + errorBody);  // Xem lỗi chi tiết
                        Toast.makeText(ResetPasswordActivity.this, "Lỗi: " + errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(ResetPasswordActivity.this, "Có lỗi xảy ra: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
