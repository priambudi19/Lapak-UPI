package com.tugasbesar.lapakupi.model;

import com.google.gson.annotations.SerializedName;

public class StatMessRespond {

    @SerializedName("status")
    String status;
    @SerializedName("message")
    String pesan;

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
