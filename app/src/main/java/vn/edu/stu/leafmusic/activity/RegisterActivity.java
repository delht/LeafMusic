package vn.edu.stu.leafmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import vn.edu.stu.leafmusic.R;

public class RegisterActivity extends AppCompatActivity {
    EditText edtUser, edtEmail, edtPass;
    Button btnRegister, btnLogin;

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
    }

    private void AddControls() {
        edtUser=findViewById(R.id.edtUser);
        edtEmail=findViewById(R.id.edtEmail);
        edtPass=findViewById(R.id.edtPass);
        btnRegister=findViewById(R.id.btnRegister);
        btnLogin=findViewById(R.id.btnLogin);

        btnRegister.setOnClickListener(view ->{
            String username=edtUser.getText().toString().trim();
            String email=edtEmail.getText().toString().trim();
            String pass=edtPass.getText().toString().trim();

            if (TextUtils.isEmpty(username)){
                edtUser.setError("Vui lòng nhập tên đăng nhâp!");
                edtUser.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(email)){
                edtEmail.setError("Vui lòng nhập Email!");
                edtEmail.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(pass)){
                edtPass.setError("Vui lòng nhập mật khẩu!");
                edtPass.requestFocus();
                return;
            }


        } );

        btnLogin.setOnClickListener(v -> {
            Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

    }
}