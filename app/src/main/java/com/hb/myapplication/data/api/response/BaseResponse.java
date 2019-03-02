package com.hb.myapplication.data.api.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by haibt3 on 2/24/2017.
 */

public class BaseResponse<T> {
    @SerializedName("status")
    public int status;

    @SerializedName("message")
    public String message;

    @SerializedName("data")
    public T data;

}
