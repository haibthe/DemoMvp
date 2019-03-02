package com.hb.myapplication.data

import android.content.Context
import com.hb.myapplication.data.cache.ICache
import com.hb.myapplication.data.pref.PreferenceHelper

class AppDataManager
constructor(
    private val context: Context,
    private val pref: PreferenceHelper,
    private val cache: ICache
) : DataManager {

    companion object {
    }

    private var mData: String = ""

    override fun logout() {
    }

    override fun setData(data: String) {
        mData = data
    }

    override fun getData(): String {
        return mData
    }
}