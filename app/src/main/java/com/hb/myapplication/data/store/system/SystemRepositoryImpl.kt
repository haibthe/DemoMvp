package com.hb.myapplication.data.store.system


import com.hb.myapplication.data.entity.DataWrapper
import com.hb.myapplication.data.repository.SystemRepository
import io.reactivex.Observable

/**
 * Created by buihai on 9/9/17.
 */

class SystemRepositoryImpl(
    private val storage: SystemStore.LocalStorage,
    private val service: SystemStore.RequestService
) : SystemRepository {

    override fun getDataTest(): Observable<List<DataWrapper<*>>> {
        return Observable.create<List<String>> {
            val data = ArrayList<String>()
            for (i in 0..20) {
                data.add("Item $i")
            }
            it.onNext(data)
            it.onComplete()
        }
            .map {
                it.map {
                    object : DataWrapper<String>(it) {
                        override fun getTitle(): String {
                            return getData()
                        }

                        override fun getSubtitle(): String {
                            return getData()
                        }

                        override fun getDescription(): String {
                            return getData()
                        }

                        override fun getIcon(): String {
                            return getData()
                        }
                    }
                }
            }
    }
}
