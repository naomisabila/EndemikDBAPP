package com.example.endemikdbapp.data.retrofit;

import com.example.endemikdbapp.data.model.Endemik;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    // ini URL belakangnya: hendroprwk08.github.io/data_endemik/endemik.json
    @GET("data_endemik/endemik.json")
    Call<List<Endemik>> getEndemikList();
}