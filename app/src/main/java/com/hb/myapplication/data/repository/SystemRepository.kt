package com.hb.myapplication.data.repository

import com.hb.myapplication.data.entity.DataWrapper
import io.reactivex.Observable

/**
 * Created by buihai on 9/9/17.
 */

interface SystemRepository {

    fun getDataTest(): Observable<List<DataWrapper<*>>>

}
