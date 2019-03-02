package com.hb.uiwidget.recyclerview

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

/**
 * Created by haibui on 2017-03-31.
 */

abstract class EndlessRecyclerViewListener : RecyclerView.OnScrollListener {

    protected var visibleThreshold = 1
    // The current offset index of data you have loaded
    var currentPage = 0
    // The total number of items in the dataset after the last load
    protected var previousTotalItemCount = 0
    // True if we are still waiting for the last set of data to load.
    var isLoadNext = true
        protected set
    // Sets the starting page index
    internal var startingPageIndex: Int = 0

    protected var MIN_TOTAL_COUNT = 5

    protected var layoutManagerType: LAYOUT_MANAGER_TYPE? = null
    protected var lastPositions: IntArray? = null


    protected var isPause = false

    constructor() {}

    constructor(visibleThreshold: Int) {
        this.visibleThreshold = visibleThreshold
    }

    constructor(visibleThreshold: Int, startPage: Int) : this(visibleThreshold) {
        this.startingPageIndex = startPage
        this.currentPage = startPage
    }

    fun setMinTotalCount(minTotalCount: Int) {
        MIN_TOTAL_COUNT = minTotalCount
    }

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        when (newState) {
            RecyclerView.SCROLL_STATE_DRAGGING -> {
            }

            RecyclerView.SCROLL_STATE_IDLE -> {
            }
        }//  Glide.with(CApplication.Instance()).pauseRequests();
        // Glide.with(CApplication.Instance()).resumeRequests();
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        loadMoreItemsIfNecessary(recyclerView)
    }

    protected fun loadMoreItemsIfNecessary(recyclerView: RecyclerView?) {
        val layoutManager = recyclerView!!.layoutManager!!

        val visibleItemCount = layoutManager.childCount
        val totalItemCount = layoutManager.itemCount

        if (MIN_TOTAL_COUNT >= totalItemCount) return

        var lastVisibleItemPosition = -1
        if (layoutManagerType == null) {
            if (layoutManager is GridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.GRID
            } else if (layoutManager is LinearLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.LINEAR
            } else if (layoutManager is StaggeredGridLayoutManager) {
                layoutManagerType = LAYOUT_MANAGER_TYPE.STAGGERED_GRID
            } else {
                throw RuntimeException("Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager")
            }
        }

        when (layoutManagerType) {
            EndlessRecyclerViewListener.LAYOUT_MANAGER_TYPE.LINEAR -> lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
            EndlessRecyclerViewListener.LAYOUT_MANAGER_TYPE.GRID -> {
                val gridLayoutManager = layoutManager as GridLayoutManager
                lastVisibleItemPosition = gridLayoutManager.findLastVisibleItemPosition()
                if (gridLayoutManager.findFirstVisibleItemPosition() == 0) {
                    return
                }
            }
            EndlessRecyclerViewListener.LAYOUT_MANAGER_TYPE.STAGGERED_GRID -> {
                val staggeredGridLayoutManager = layoutManager as StaggeredGridLayoutManager
                if (lastPositions == null)
                    lastPositions = IntArray(staggeredGridLayoutManager.spanCount)

                staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions)
                lastVisibleItemPosition = findMax(lastPositions!!)
            }
        }


        if (isPause)
            return

        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex
            this.previousTotalItemCount = totalItemCount
            if (totalItemCount == 0) {
                this.isLoadNext = true
            } else {
                isLoadNext = false
                return
            }
        }

        if (isLoadNext && totalItemCount > previousTotalItemCount) {
            isLoadNext = false
            previousTotalItemCount = totalItemCount
            currentPage++
        }

        if ((totalItemCount - lastVisibleItemPosition <= visibleThreshold || totalItemCount - lastVisibleItemPosition == 0 && totalItemCount > visibleItemCount) && !isLoadNext) {

            isLoadNext = true
            onLoadMore(currentPage + 1, totalItemCount)
        }
    }

    abstract fun onLoadMore(page: Int, totalItemsCount: Int)


    fun onPause() {
        isPause = true
    }

    fun onResume() {
        isPause = false
    }

    fun setStartingPageIndex(startingPageIndex: Int) {
        this.startingPageIndex = startingPageIndex
    }

    protected fun findMax(lastPositions: IntArray): Int {
        var max = Integer.MIN_VALUE
        for (value in lastPositions) {
            if (value > max)
                max = value
        }
        return max
    }

    enum class LAYOUT_MANAGER_TYPE {
        LINEAR,
        GRID,
        STAGGERED_GRID
    }
}
