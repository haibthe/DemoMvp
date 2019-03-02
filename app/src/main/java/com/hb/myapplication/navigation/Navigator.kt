package com.hb.myapplication.navigation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.hb.myapplication.ui.detail.DetailActivity


object Navigator {

    @SuppressLint("MissingPermission")
    fun callToNumber(activity: Activity, phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)

        intent.data = Uri.parse("tel:$phoneNumber")
        activity.startActivity(intent)
    }

    fun startDetail(activity: Activity) {
        val intent = Intent(activity, DetailActivity::class.java)
        activity.startActivity(intent)
    }


}