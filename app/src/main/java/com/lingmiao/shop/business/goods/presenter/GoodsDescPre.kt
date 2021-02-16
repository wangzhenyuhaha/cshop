package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.luck.picture.lib.entity.LocalMedia

/**
 * Author : Elson
 * Date   : 2020/9/1
 * Desc   : 商品详情
 */
interface GoodsDescPre: BasePresenter {

    fun uploadImages(list: List<LocalMedia>?)



    interface DescView : BaseView {
        fun onUploadSuccess(images: String)
    }
}