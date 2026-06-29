package com.example.endemikdbapp.data.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // URL depannya (base URL)
    private static final String BASE_URL = "https://hendroprwk08.github.io/";

    private static Retrofit retrofit = null;

    // "Mesin" Retrofit-nya, cuma dibikin sekali
    public static com.example.endemikdbapp.data.retrofit.ApiService getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(com.example.endemikdbapp.data.retrofit.ApiService.class);
    }
}