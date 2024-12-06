package vn.edu.stu.leafmusic.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsHelper {
    private static final String PREFS_NAME = "user_prefs";
    private final SharedPreferences sharedPreferences;

    public SharedPrefsHelper(Context context) {
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    //luu thong tin da dang nhap
    public void saveLoginState(String userId, String username, boolean isLoggedIn) {
        sharedPreferences.edit()
                .putString("user_id", userId)
                .putString("username", username)
                .putBoolean("is_logged_in", isLoggedIn)
                .apply();
    }

    //lay thong tin da dang nhap
    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean("is_logged_in", false);
    }

    //lay thong tin user (ID)
    public String getUserId() {
        return sharedPreferences.getString("user_id", null);
    }

    public String getUsername() {
        return sharedPreferences.getString("username", null);
    }

    //xoa data user (dang xuat)
    public void clearLoginState() {
        sharedPreferences.edit()
                .remove("user_id")
                .remove("username")
                .putBoolean("is_logged_in", false)
                .apply();
    }
}
