package com.lingmiao.shop.business.main.presenter

import android.content.Context
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.main.bean.CategoryItem

interface ReplenishInfoPresenter : BasePresenter {

    fun searchCategory()

    fun search2Category()

    fun showCategoryPop(context: Context, callback: (CategoryItem?, CategoryItem?) -> Unit)

    interface View : BaseView {

    }
}