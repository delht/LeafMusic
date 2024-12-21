package vn.edu.stu.leafmusic.api.dto;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static final String BASE_URL = "http://192.168.83.1:8080/";
//    private static final String BASE_URL = "http://192.168.1.11:8080/";

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

    public static ApiService getApiService() {
        return getRetrofit().create(ApiService.class); // Tạo đối tượng ApiService từ Retrofit

    }

}
