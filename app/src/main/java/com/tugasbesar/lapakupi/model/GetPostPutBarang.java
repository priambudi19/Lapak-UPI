package com.tugasbesar.lapakupi.model;

import com.google.gson.annotations.SerializedName;

public class GetPostPutBarang {
    @SerializedName("data")
    Barang data;
    @SerializedName("status")
    String status;
    @SerializedName("message")
    String pesan;

    public Barang getData() {
        return data;
    }

    public void setData(Barang data) {
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
