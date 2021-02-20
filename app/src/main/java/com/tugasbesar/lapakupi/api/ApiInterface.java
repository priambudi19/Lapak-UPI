package com.tugasbesar.lapakupi.api;

import com.tugasbesar.lapakupi.model.AkunRespond;
import com.tugasbesar.lapakupi.model.GetListBarang;
import com.tugasbesar.lapakupi.model.GetPostPutBarang;
import com.tugasbesar.lapakupi.model.Login;
import com.tugasbesar.lapakupi.model.Response;
import com.tugasbesar.lapakupi.model.StatMessRespond;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;


public interface ApiInterface {
    @Multipart
    @POST("register")
    Call<Response> register(@Part("username") RequestBody username, @Part("password") RequestBody pass,
                            @Part("nama") RequestBody nama, @Part("prodi") RequestBody prodi, @Part("no_hp") RequestBody nohp
                            , @Part MultipartBody.Part foto_profil
                        );


    @GET("register")
    Call<AkunRespond> cekUsername(@Query("username") String username);


    @FormUrlEncoded
    @POST("login")
    Call<Login> login(@Field("username") String username, @Field("password") String password);
    @Multipart
    @POST("barang")
    Call<GetPostPutBarang> postBarang (@Part MultipartBody.Part foto_barang,
                                       @Part("id_akun") RequestBody id_akun,
                                       @Part("nama_barang") RequestBody nama_barang,
                                       @Part("harga") RequestBody harga,
                                       @Part("id_kategori") RequestBody id_kategori,
                                       @Part("deskripsi") RequestBody deskripsi);

    @GET("barang")
    Call<GetListBarang> getBarangBySearch(@Query("nama_barang") String nama_barang);



    @Multipart
    @POST("editbarang")
    Call<GetPostPutBarang> putBarangWithImg (@Part MultipartBody.Part foto_barang,
                                             @Part("id_akun") RequestBody id_akun,
                                             @Part("id_barang") RequestBody id_barang,
                                             @Part("nama_barang") RequestBody nama_barang,
                                             @Part("harga") RequestBody harga,
                                             @Part("id_kategori") RequestBody id_kategori,
                                             @Part("deskripsi") RequestBody deskripsi);

    @Multipart
    @POST("editbarang")
    Call<GetPostPutBarang> putBarangNoImg (@Part("id_akun") RequestBody id_akun,
                                           @Part("id_barang") RequestBody id_barang,
                                           @Part("nama_barang") RequestBody nama_barang,
                                           @Part("harga") RequestBody harga,
                                           @Part("id_kategori") RequestBody id_kategori,
                                           @Part("deskripsi") RequestBody deskripsi);

    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "barang", hasBody = true)
    Call<StatMessRespond> delleteBarang (@Field("id_barang") String id_barang);

    @GET("barang")
    Call<GetListBarang> getBarangByKategori(@Query("id_kategori") String id_kategori);

    @GET("barang")
    Call<GetListBarang> getBarangById(@Query("id_barang") String id_barang);


    @GET("akun")
    Call<AkunRespond> getAkunById(@Query("id_akun") String id_akun);

    @Multipart
    @POST("akun")
    Call<Response> editAkun(
            @Part("id_akun") RequestBody id,@Part("username") RequestBody username, @Part("password") RequestBody pass,
            @Part("nama") RequestBody nama, @Part("prodi") RequestBody prodi, @Part("no_hp") RequestBody nohp
            , @Part MultipartBody.Part foto_profil
    );

    @Multipart
    @POST("akun")
    Call<Response> editAkunNoImage(
            @Part("id_akun") RequestBody id, @Part("username") RequestBody username, @Part("password") RequestBody pass,
            @Part("nama") RequestBody nama, @Part("prodi") RequestBody prodi, @Part("no_hp") RequestBody nohp
    );
    @GET("barang")
    Call<GetListBarang> getBarangByAkun(@Query("id_akun") String id_akun);
}
