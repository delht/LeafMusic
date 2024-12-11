package vn.edu.stu.leafmusic.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import vn.edu.stu.leafmusic.R;
import vn.edu.stu.leafmusic.activity.fragment.AccountFragment;
import vn.edu.stu.leafmusic.activity.fragment.HomeFragment;
import vn.edu.stu.leafmusic.activity.fragment.LoveListFragment;
import vn.edu.stu.leafmusic.activity.fragment.SettingFragment;
import vn.edu.stu.leafmusic.activity.login.LoginActivity;
import vn.edu.stu.leafmusic.util.SharedPrefsHelper;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    Toolbar toolbar;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_SETTING = 1;
    private static final int FRAGMENT_LOVELIST = 2;
    private static final int FRAGMENT_ACCOUNT = 3;
//    private static final int FRAGMENT_LOGOUT = 4;

    private int CurrentFragment = FRAGMENT_HOME;

    SharedPrefsHelper sharedPrefsHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_drawer);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawer_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        sharedPrefsHelper = new SharedPrefsHelper(this);
        addControls();
        addEvents();

        setUsernameInHeader();
    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void addEvents() {

        drawerLayout = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                MainActivity.this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);

        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        replaceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);


    }

    //xu ly nhan nut trong nav
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_home){
//            if(CurrentFragment != FRAGMENT_HOME){
                replaceFragment(new HomeFragment());
                CurrentFragment = FRAGMENT_HOME;
                Log.d("Fragment", "Home Fragment Selected");
//            }
        } else if (id == R.id.nav_setting) {
            if(CurrentFragment != FRAGMENT_SETTING){
                replaceFragment(new SettingFragment());
                CurrentFragment = FRAGMENT_SETTING;
                Log.d("Fragment", "SETTING Fragment Selected");
            }
        }else if (id == R.id.nav_lovelist) {
            if(CurrentFragment != FRAGMENT_LOVELIST){
                replaceFragment(new LoveListFragment());
                CurrentFragment = FRAGMENT_LOVELIST;
                Log.d("Fragment", "LOVELIST Fragment Selected");
            }
        }else if (id == R.id.nav_account) {
            if(CurrentFragment != FRAGMENT_ACCOUNT){
                replaceFragment(new AccountFragment());
                CurrentFragment = FRAGMENT_ACCOUNT;
                Log.d("Fragment", "ACCOUNT Fragment Selected");
            }
        } else if (id == R.id.nav_Logout) {
            sharedPrefsHelper.clearLoginState();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }

        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    //xu ly khi nhan nut bach
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    private void replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }

    //hien thi ten user
    private void setUsernameInHeader() {
        NavigationView navigationView = findViewById(R.id.navigation_view);
        View headerView = navigationView.getHeaderView(0);
        TextView tvUsername = headerView.findViewById(R.id.tvUsername);

        if (sharedPrefsHelper != null) {
            String username = sharedPrefsHelper.getUsername();

            if (username != null) {
                tvUsername.setText(username);
            } else {
                tvUsername.setText("Khác");
            }
        }
    }


}