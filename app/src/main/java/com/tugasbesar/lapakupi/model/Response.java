package com.tugasbesar.lapakupi.model;

import com.google.gson.annotations.SerializedName;

public class Response {
    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }


}
