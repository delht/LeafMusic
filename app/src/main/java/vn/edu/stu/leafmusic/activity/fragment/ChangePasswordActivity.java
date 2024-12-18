package vn.edu.stu.leafmusic.activity.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.api.dto.ApiClient;
import vn.edu.stu.leafmusic.api.dto.request.ChangePasswordRequest;
import vn.edu.stu.leafmusic.util.SharedPrefsHelper;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText edtCu, edtMoi, edtReMoi;
    private Button btnRegister;
    private SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);


        edtCu = findViewById(R.id.edtCu);
        edtMoi = findViewById(R.id.edtMoi);
        edtReMoi = findViewById(R.id.edtReMoi);
        btnRegister = findViewById(R.id.btnRegister);

        sharedPrefsHelper = new SharedPrefsHelper(this);

//===================================================================================

        btnRegister.setOnClickListener(v -> {
            String oldPassword = edtCu.getText().toString();
            String newPassword = edtMoi.getText().toString();
            String confirmPassword = edtReMoi.getText().toString();

            if (newPassword.equals(confirmPassword)) {
                String userId = sharedPrefsHelper.getUserId();
                ChangePasswordRequest request = new ChangePasswordRequest(userId, oldPassword, newPassword);

                // Gọi API đổi mật khẩu
                changePassword(request);
            } else {
                Toast.makeText(ChangePasswordActivity.this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
            }
        });
    }




//    ===================================================================================================

    private void changePassword(ChangePasswordRequest request) {
        ApiClient.getApiService().changePassword(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
