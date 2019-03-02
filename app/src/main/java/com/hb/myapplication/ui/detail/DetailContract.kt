package com.hb.myapplication.ui.detail

import com.hb.myapplication.data.entity.DataWrapper

interface DetailContract {
    interface View {
        fun updateData(data: DataWrapper<*>)

        fun showError(error: String)
    }

    interface Presenter {
        fun loadData()
    }
}