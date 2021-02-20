package com.tugasbesar.lapakupi.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class AkunRespond {
    @SerializedName("data")
    ArrayList<Akun> dataAkun;
    @SerializedName("status")
    String status;
    @SerializedName("message")
    String pesan;

    public ArrayList<Akun> getDataAkun() {
        return dataAkun;
    }

    public void setDataAkun(ArrayList<Akun> dataAkun) {
        this.dataAkun = dataAkun;
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
