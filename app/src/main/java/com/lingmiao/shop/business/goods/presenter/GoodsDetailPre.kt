package com.lingmiao.shop.business.goods.presenter

import com.james.common.base.BasePresenter
import com.james.common.base.BaseView
import com.lingmiao.shop.business.goods.api.bean.GoodsParamVo

/**
Create Date : 2021/3/611:09 AM
Auther      : Fox
Desc        :
 **/
interface GoodsDetailPre : BasePresenter {

    fun loadInfoByCId(cid : String);

    fun addInfo(cId : String, str : String);

    interface View : BaseView {

        fun onLoadedInfoList(list : List<GoodsParamVo>);

        fun onInfoAdded(vo : GoodsParamVo);
    }

}