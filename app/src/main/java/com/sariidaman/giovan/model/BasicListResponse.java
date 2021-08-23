package com.sariidaman.giovan.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class BasicListResponse<T> {
        @SerializedName("success")
        @Expose
        private Boolean success;
        @SerializedName("data")
        @Expose
        private ArrayList<T> datas = null;
        @SerializedName("message")
        @Expose
        private String message;

        public Boolean isSuccess() {
            return success;
        }

        public ArrayList<T> getDatas() {
            return datas;
        }

        public String getMessage() {
            return message;
        }

}
