package com.hb.uiwidget.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import butterknife.ButterKnife


/**
 * Created by haibt3 on 12/19/2016.
 */

abstract class BaseViewHolder<D>(itemView: View) : RecyclerView.ViewHolder(itemView) {


    init {
        ButterKnife.bind(this, itemView)
    }

    abstract fun bindData(data: D)
}
