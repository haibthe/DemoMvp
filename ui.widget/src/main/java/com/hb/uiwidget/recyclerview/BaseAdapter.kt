package com.hb.uiwidget.recyclerview

import android.content.Context
import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView


abstract class BaseAdapter<D, VH : BaseViewHolder<*>>(
        protected var context: Context,
        protected var recyclerView: RecyclerView
) : RecyclerView.Adapter<VH>() {

    var mInflater: LayoutInflater = LayoutInflater.from(this.context)

    var mData: D? = null
    var mListener: OnItemClickListener? = null

    var data: D?
        get() = mData
        set(data) {
            mData = data
            notifyDataSetChanged()
        }

    open fun <T> getItem(position: Int): T? {
        if (mData == null) {
            return null
        }
        if (mData is List<*>) {
            return if (position < 0 || position >= (mData as List<*>).size) null else (mData as List<*>)[position] as T
        } else {

        }
        return null
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.itemView.setOnClickListener {
            if (mListener != null) {
                mListener!!.onItemClick(holder.itemView, getItem(position), position)
            }
        }
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mListener = listener
    }


}
