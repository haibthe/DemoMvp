package com.hb.myapplication.data

import com.hb.lib.data.IDataManager
import com.hb.myapplication.data.pref.PreferenceHelper


interface DataManager : IDataManager, PreferenceHelper {

    fun logout()

    fun setData(data: String)

    fun getData(): String

}