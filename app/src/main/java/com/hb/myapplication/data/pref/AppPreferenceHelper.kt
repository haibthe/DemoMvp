package com.hb.myapplication.data.pref

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.google.gson.Gson
import com.hb.myapplication.R

class AppPreferenceHelper(val context: Context, val gson: Gson) : PreferenceHelper {

    private var pref: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE)




}