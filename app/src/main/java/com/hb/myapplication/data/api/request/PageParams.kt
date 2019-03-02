package com.hb.myapplication.data.api.request

import com.google.gson.annotations.SerializedName

open class PageParams {
    @SerializedName("page")
    var page = 1
}