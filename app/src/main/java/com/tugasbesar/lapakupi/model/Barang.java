package com.tugasbesar.lapakupi.model;

import com.google.gson.annotations.SerializedName;

public class Barang {
    @SerializedName("id_barang")
    String id_barang;
    @SerializedName("id_akun")
    String id_akun;
    @SerializedName("nama_barang")
    String nama_barang;
    @SerializedName("harga")
    String harga;
    @SerializedName("id_kategori")
    String id_kategori;
    @SerializedName("deskripsi")
    String deskripsi;
    @SerializedName("date_created")
    String tgl_dibuat;
    @SerializedName("foto_barang")
    String foto_barang;

    public String getId_barang() {
        return id_barang;
    }

    public void setId_barang(String id_barang) {
        this.id_barang = id_barang;
    }

    public String getId_akun() {
        return id_akun;
    }

    public void setId_akun(String id_akun) {
        this.id_akun = id_akun;
    }

    public String getNama_barang() {
        return nama_barang;
    }

    public void setNama_barang(String nama_barang) {
        this.nama_barang = nama_barang;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getId_kategori() {
        return id_kategori;
    }

    public void setId_kategori(String id_kategori) {
        this.id_kategori = id_kategori;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTgl_dibuat() {
        return tgl_dibuat;
    }

    public void setTgl_dibuat(String tgl_dibuat) {
        this.tgl_dibuat = tgl_dibuat;
    }

    public String getFoto_barang() {
        return foto_barang;
    }

    public void setFoto_barang(String foto_barang) {
        this.foto_barang = foto_barang;
    }
}
