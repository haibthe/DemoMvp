package com.hb.myapplication.ui.main

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import com.hb.lib.mvp.impl.lce.sr.HBMvpLceSRActivity
import com.hb.myapplication.R
import com.hb.myapplication.data.DataManager
import com.hb.myapplication.data.entity.DataWrapper
import com.hb.myapplication.navigation.Navigator
import com.hb.uiwidget.recyclerview.BaseAdapter
import com.hb.uiwidget.recyclerview.BaseViewHolder
import com.hb.uiwidget.recyclerview.OnItemClickListener

class MainActivity : HBMvpLceSRActivity<List<DataWrapper<*>>, MainPresenter>(), MainContract.View {
//class MainActivity: HBMvpLceSRActivity<List<SuperMarket>, MainPresenter>(), MainContract.View {

    override fun getResLayoutId(): Int {
        return R.layout.activity_lce_sr_search
    }

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(toolbar)

    }

    override fun createAdapter(context: Context, recyclerView: RecyclerView): RecyclerView.Adapter<*> {
        val adapter = MyAdapter(context, recyclerView)
        adapter.setOnItemClickListener(OnItemClickListener { anchor, obj, position ->
            if (obj is DataWrapper<*>) {
                mPresenter.dataManager<DataManager>().setData(obj.getData() as String)
            }
            Navigator.startDetail(this)
        })
        return adapter
    }

    override fun setData(data: List<DataWrapper<*>>) {
        val adapter = getAdapter<MyAdapter>()
        adapter.data = data
    }

    class MyViewHolder(itemView: View) : BaseViewHolder<DataWrapper<*>>(itemView) {

        @BindView(android.R.id.text1)
        lateinit var title: TextView

        override fun bindData(data: DataWrapper<*>) {
            title.text = data.getTitle()
        }
    }

    class MyAdapter(context: Context, rv: RecyclerView) : BaseAdapter<List<DataWrapper<*>>, MyViewHolder>(context, rv) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = mInflater.inflate(android.R.layout.simple_list_item_1, parent, false)
            return MyViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            super.onBindViewHolder(holder, position)
            val data = getItem<DataWrapper<*>>(position)!!
            holder.bindData(data)
        }

        override fun getItemCount(): Int {
            if (mData == null)
                return 0
            return mData!!.size
        }
    }
}
