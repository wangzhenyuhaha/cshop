package com.lingmiao.shop.business.goods.presenter

import com.lingmiao.shop.business.goods.api.bean.GoodsSkuCacheVO
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuVOWrapper
import com.lingmiao.shop.business.goods.api.bean.SpecKeyVO
import com.lingmiao.shop.business.goods.api.bean.SpecValueVO
import com.james.common.base.BasePresenter
import com.james.common.base.BaseView

/**
 * Author : Elson
 * Date   : 2020/8/13
 * Desc   : 规格设置页
 */
interface SpecSettingPre: BasePresenter {

    fun submitSpecValue(specKeyId: String, valueNames: String)

    fun getSpecKeyList(oldList: List<GoodsSkuVOWrapper>, specList: List<SpecKeyVO>)
    /**
     * 从服务端获取规格名称
     */
    fun loadSpecKeyList(goodsId: String?)

   fun  loadSpecKeyListFromCenter(goodsId: String?)

    fun loadSpecListByCid(cid : String?);

    fun showAddOldKey(cid : String?, keyId : String?, list: List<SpecValueVO>?);

    interface SpceSettingView : BaseView {
        fun onAddSpecValueSuccess(specKeyID: String, spceValueList: List<SpecValueVO>)

        fun onLoadSkuListSuccess(list: List<GoodsSkuVOWrapper>)

        fun onLoadCacheSkuListSuccess(skuCache: GoodsSkuCacheVO)

        fun onLoadedSpecListByCid(list : List<SpecKeyVO>);
    }
}