package vn.edu.stu.leafmusic.api.dto;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://192.168.83.1:8080/"; // Thay đổi URL nếu cần

    private static Retrofit retrofit;

    public static Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // GsonConverter để chuyển JSON thành object
                    .build();
        }
        return retrofit;
    }

}
