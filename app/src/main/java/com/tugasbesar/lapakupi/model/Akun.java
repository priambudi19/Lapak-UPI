package com.tugasbesar.lapakupi.model;

import com.google.gson.annotations.SerializedName;


public class Akun {
    @SerializedName("id_akun")
    String id_akun;
    @SerializedName("username")
    String username;

    @SerializedName("password")
    String password;
    @SerializedName("nama")
    String nama;
    @SerializedName("prodi")
    String prodi;
    @SerializedName("foto_profil")
    String foto_profil;
    @SerializedName("no_hp")
    String no_hp;


    public String getId_akun() {
        return id_akun;
    }

    public void setId_akun(String id_akun) {
        this.id_akun = id_akun;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getProdi() {
        return prodi;
    }

    public void setProdi(String prodi) {
        this.prodi = prodi;
    }

    public String getFoto_profil() {
        return foto_profil;
    }

    public void setFoto_profil(String foto_profil) {
        this.foto_profil = foto_profil;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public void setNo_hp(String no_hp) {
        this.no_hp = no_hp;
    }
}
