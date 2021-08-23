package com.sariidaman.giovan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BasicResponse<T> {
    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("response")
    @Expose
    private T response;
    @SerializedName("message")
    @Expose
    private String message;

    public Boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
