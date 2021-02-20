package com.tugasbesar.lapakupi;

import com.tugasbesar.lapakupi.model.Response;


import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiInterface {
    @Multipart
    @POST("register")
    Call<Response> register(@Part("username") RequestBody username, @Part("password") RequestBody pass,
                            @Part("nama") RequestBody nama, @Part("prodi") RequestBody prodi, @Part("no_hp") RequestBody nohp
                            , @Part MultipartBody.Part foto_profil
                        );
    @FormUrlEncoded
    @POST("login")
    Call<Response> login(@Field("username") String username, @Field("password") String password);
}
