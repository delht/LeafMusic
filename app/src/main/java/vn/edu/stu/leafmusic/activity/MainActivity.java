package vn.edu.stu.leafmusic.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

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

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    Toolbar toolbar;

    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_SETTING = 1;
    private static final int FRAGMENT_LOVELIST = 2;
    private static final int FRAGMENT_ACCOUNT = 3;

    private int CurrentFragment = FRAGMENT_HOME;

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

        addControls();
        addEvents();
    }

    private void addControls() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);


        repalceFragment(new HomeFragment());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
    }

    private void addEvents() {
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                MainActivity.this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();




    }

    //xu ly nhan nut trong nav
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_home){
            if(CurrentFragment != FRAGMENT_HOME){
                repalceFragment(new HomeFragment());
                CurrentFragment = FRAGMENT_HOME;
                Log.d("Fragment", "Home Fragment Selected");
            }
        } else if (id == R.id.nav_setting) {
            if(CurrentFragment != FRAGMENT_SETTING){
                repalceFragment(new SettingFragment());
                CurrentFragment = FRAGMENT_SETTING;
                Log.d("Fragment", "SETTING Fragment Selected");
            }
        }else if (id == R.id.nav_lovelist) {
            if(CurrentFragment != FRAGMENT_LOVELIST){
                repalceFragment(new LoveListFragment());
                CurrentFragment = FRAGMENT_LOVELIST;
                Log.d("Fragment", "LOVELIST Fragment Selected");
            }
        }else if (id == R.id.nav_account) {
            if(CurrentFragment != FRAGMENT_ACCOUNT){
                repalceFragment(new AccountFragment());
                CurrentFragment = FRAGMENT_ACCOUNT;
                Log.d("Fragment", "ACCOUNT Fragment Selected");
            }
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

    private void repalceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }



}