package com.lingmiao.shop.business.goods.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.api.bean.GoodsSkuDO
import com.lingmiao.shop.util.GlideUtils

class GoodsScanAdapter :
    BaseQuickAdapter<GoodsSkuDO, BaseViewHolder>(R.layout.goods_adapter_goods_check) {

    override fun convert(helper: BaseViewHolder, item: GoodsSkuDO?) {
        item?.apply {

            //显示图片
            GlideUtils.setImageUrl1(helper.getView(R.id.goodsIv), thumbnail)


            //显示商品名
            helper.setText(R.id.goodsNameTv, goods_name)

            //useless
            helper.setText(R.id.goodsQuantityTv, "")

            //商品价格
            helper.setText(R.id.goodsPriceTv, price.toString())


            helper.setVisible(R.id.menuIv, false)
            helper.setVisible(R.id.deleteIv, false)

            helper.setVisible(R.id.goodsCheckSubmit, true)

            helper.addOnClickListener(R.id.goodsCheckSubmit)
        }
    }
}