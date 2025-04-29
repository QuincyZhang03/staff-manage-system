package com.zjquincy.ncu.net.response;

import com.google.gson.annotations.SerializedName;

public abstract class AbstractResponse {
    @SerializedName("response_type")
    protected String response_type;
}