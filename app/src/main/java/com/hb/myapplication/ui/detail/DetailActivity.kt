package com.hb.myapplication.ui.detail

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import com.hb.lib.mvp.impl.HBMvpActivity
import com.hb.myapplication.R
import com.hb.myapplication.data.entity.DataWrapper

class DetailActivity : HBMvpActivity<DetailPresenter>(), DetailContract.View {


    override fun getResLayoutId(): Int {
        return R.layout.activity_detail
    }

    @BindView(android.R.id.text1)
    lateinit var title: TextView
    @BindView(android.R.id.text2)
    lateinit var description: TextView

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(toolbar)

        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setDisplayShowHomeEnabled(true)

            actionBar.title = "Detail"
        }


        mPresenter.loadData()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun updateData(data: DataWrapper<*>) {

        title.text = data.getTitle()
        description.text = data.getDescription()
    }

    override fun showError(error: String) {
        showErrorDialog(error, View.OnClickListener {

        })
    }
}