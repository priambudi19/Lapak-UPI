package com.tugasbesar.lapakupi.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class GetListBarang {
    @SerializedName("data")
    ArrayList<Barang> data;
    @SerializedName("status")
    String status;
    @SerializedName("message")
    String pesan;

    public ArrayList<Barang> getData() {
        return data;
    }

    public void setData(ArrayList<Barang> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }
}