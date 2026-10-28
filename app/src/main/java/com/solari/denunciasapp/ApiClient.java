package com.solari.denunciasapp;

import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Render: "https://TU-URL.onrender.com/"
    // Emulador local: "http://10.0.2.2:8080/"
    private static final String BASE_URL = "http://10.0.2.2:8080/";
    static DenunciaApi api;

    public static DenunciaApi getApi() {
        if (api == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(90, TimeUnit.SECONDS)
                    .readTimeout(90, TimeUnit.SECONDS)
                    .writeTimeout(90, TimeUnit.SECONDS)
                    .build();
            api = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(DenunciaApi.class);
        }
        return api;
    }
}