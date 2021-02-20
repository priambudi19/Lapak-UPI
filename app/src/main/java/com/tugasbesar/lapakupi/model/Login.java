package com.tugasbesar.lapakupi.model;

import com.google.gson.annotations.SerializedName;

public class Login {
    @SerializedName("id_akun")
    String id_akun;

    public String getId() {
        return id_akun;
    }

    public void setId(String id_akun) {
        this.id_akun = id_akun;
    }

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    @SerializedName("status")
    String status;

    public String getStatus() {
        return status;
    }
}
