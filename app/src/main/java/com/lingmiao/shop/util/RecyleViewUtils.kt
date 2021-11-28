package com.lingmiao.shop.util

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
Create Date : 2021/5/304:16 PM
Auther      : Fox
Desc        :
 **/


fun <T> RecyclerView.initAdapter(mAdapter : BaseQuickAdapter<T,BaseViewHolder>){
    apply {
        layoutManager = initLayoutManager(context);
        adapter = mAdapter;
    }
}

fun initLayoutManager(context: Context): RecyclerView.LayoutManager {
    return LinearLayoutManager(context)
}
