package com.solari.denunciasapp;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface DenunciaApi {
    @POST("denuncias/")
    Call<Denuncia> registrar(@Body Denuncia d);

    @GET("denuncias/")
    Call<List<Denuncia>> listar(@Query("offset") int offset, @Query("limit") int limit);

    @GET("denuncias/{id}")
    Call<Denuncia> obtener(@Path("id") int id);

    @GET("denuncias/ciudadano/{dni}")
    Call<List<Denuncia>> porDni(@Path("dni") String dni);

    @PUT("denuncias/{id}")
    Call<Denuncia> atender(@Path("id") int id);
}