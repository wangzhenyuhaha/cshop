package com.lingmiao.shop.business.goods.presenter.impl

import android.content.Context
import com.james.common.base.BasePreImpl
import com.james.common.base.BaseView
import com.lingmiao.shop.business.commonpop.pop.AbsOneItemPop
import com.lingmiao.shop.business.goods.api.bean.CategoryVO

/**
Create Date : 2021/3/74:52 PM
Auther      : Fox
Desc        :
 **/
class ChildrenCatePopPreImpl(view: BaseView) : BasePreImpl(view) {

    private var mItemPop : AbsOneItemPop<CategoryVO>? = null;

    fun showPop(context: Context, value : String?, list : List<CategoryVO>?, callback: ( List<CategoryVO>?) -> Unit) {
        mItemPop = AbsOneItemPop<CategoryVO>(context, value, list, "请选择分类").apply {
            isMultiChecked = true;
            listener = { item: List<CategoryVO> ->
                callback.invoke(item);
            }
        }
        mItemPop?.showPopupWindow();
    }

    override fun onDestroy() {
        mItemPop?.dismiss()
        mItemPop = null;
        super.onDestroy()
    }
}