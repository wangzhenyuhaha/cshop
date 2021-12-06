package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.business.commonpop.bean.ItemData
import com.lingmiao.shop.business.commonpop.pop.AbsOneItemAllPop
import com.lingmiao.shop.business.commonpop.pop.AbsOneItemPop
import com.lingmiao.shop.business.goods.api.bean.CategoryVO

/**
Create Date : 2021/3/74:52 PM
Auther      : Fox
Desc        :
 **/
class ChildrenCatePopPreImpl<T : ItemData>(view: BaseView) : BasePreImpl(view) {

    private var mItemPop: AbsOneItemPop<T>? = null;
    private var mItemAllPop: AbsOneItemAllPop<T>? = null

    fun showPop(context: Context, value: String?, list: List<T>?, callback: (List<T>?) -> Unit) {
        mItemPop = AbsOneItemPop<T>(context, value, list, "请选择分类").apply {
            isMultiChecked = true;
            listener = { item: List<T> ->
                callback.invoke(item);
            }
        }
        mItemPop?.showPopupWindow();
    }

    fun showAllPop(context: Context, value: String?, list: List<T>?, callback: (List<T>?) -> Unit) {
        mItemAllPop = AbsOneItemAllPop<T>(context, value, list, "请选择分类").apply {
            //设置是否可以选择，false表示可以选择
            isMultiChecked = true
            listener = { item: List<T> ->
                callback.invoke(item)
            }
        }
        mItemAllPop?.showPopupWindow()
    }

    override fun onDestroy() {
        mItemPop?.dismiss()
        mItemPop = null;

        mItemAllPop?.dismiss()
        mItemAllPop = null;
        super.onDestroy()
    }
}