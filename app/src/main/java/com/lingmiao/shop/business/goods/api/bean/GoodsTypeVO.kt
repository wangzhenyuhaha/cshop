package com.lingmiao.shop.business.goods.api.bean

import com.lingmiao.shop.business.goods.config.GoodsConfig

/**
 * Author : Elson
 * Date   : 2020/7/16
 * Desc   :
 */
class GoodsTypeVO {
    var typeId: String? = null
    var typeName: String? = null
    var typeDesc: String? = null
    var value: String? = null

    companion object {

        private const val TYPE_NORMAL_NAME = "实物商品"

        private const val TYPE_VIRTUAL_NAME = "虚拟商品"

        private const val TYPE_TICKET_NAME = "券包"

        fun getName(isVirtual: Int): String {
            return when (isVirtual) {

                0 -> {
                    TYPE_NORMAL_NAME
                }
                1 -> {
                    TYPE_VIRTUAL_NAME
                }
                2 -> {
                    TYPE_TICKET_NAME
                }
                else -> {
                    TYPE_NORMAL_NAME
                }
            }
        }

        fun getValue(isVirtual: Int): String {
            return when (isVirtual) {
                0 -> {
                    GoodsConfig.GOODS_TYPE_NORMAL
                }
                1 -> {
                    GoodsConfig.GOODS_TYPE_VIRTUAL
                }
                2 -> {
                    GoodsConfig.GOODS_TYPE_TICKET
                }
                else -> {
                    GoodsConfig.GOODS_TYPE_NORMAL
                }
            }
        }

        fun isVirtual(value: String?): Boolean {
            return GoodsConfig.GOODS_TYPE_VIRTUAL == value;
        }


        fun getGoodsTypeList(): MutableList<GoodsTypeVO> {
            val mList: MutableList<GoodsTypeVO> = mutableListOf()
            var item = GoodsTypeVO();
            item.typeName = TYPE_NORMAL_NAME
            item.typeDesc = "物流发货"
            item.value = GoodsConfig.GOODS_TYPE_NORMAL
            mList.add(item);

            item = GoodsTypeVO();
            item.typeName = TYPE_VIRTUAL_NAME
            item.typeDesc = "无需发货"
            item.value = GoodsConfig.GOODS_TYPE_VIRTUAL
            mList.add(item)

            item = GoodsTypeVO();
            item.typeName = TYPE_TICKET_NAME
            item.typeDesc = "无需发货"
            item.value = GoodsConfig.GOODS_TYPE_TICKET
            mList.add(item)
            return mList;
        }

    }
}