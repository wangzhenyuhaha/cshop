package com.lingmiao.shop.business.goods.api.bean

import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * @author elson
 * @date 2020/7/18
 * @Desc 店铺分组
 */
class ShopGroupVO : Serializable {

    @SerializedName("cat_path")
    var catPath: String? = null
    @SerializedName("children")
    var children: List<ShopGroupVO>? = null
    @SerializedName("disable")
    var disable: Int = 1 //1显示 0不显示
    @SerializedName("shop_cat_desc")
    var shopCatDesc: String? = null
    @SerializedName("shop_cat_id")
    var shopCatId: String? = null
    @SerializedName("shop_cat_name")
    var shopCatName: String? = null
    @SerializedName("shop_cat_pic")
    var shopCatPic: String? = null
    @SerializedName("shop_cat_pid")
    var shopCatPid: String? = null
    @SerializedName("shop_id")
    var shopId: String? = null
    @SerializedName("sort")
    var sort: Int = 0
    /**
     * 是否选中
     */
    var isChecked : Boolean ? = false
    companion object {
        const val LEVEL_1 = 1
        const val LEVEL_2 = 2

        const val TYPE_SHOW = 0
        const val TYPE_HIDE = 1

        fun convert(groupVO: ShopGroupVO?): ShopGroupVO {
            return if (groupVO == null) {
                ShopGroupVO()
            } else {
                ShopGroupVO().apply {
                    this.catPath = groupVO.catPath
                    this.children = groupVO.children
                    this.disable = groupVO.disable
                    this.shopCatDesc = groupVO.shopCatDesc
                    this.shopCatId = groupVO.shopCatId
                    this.shopCatName = groupVO.shopCatName
                    this.shopCatPic = groupVO.shopCatPic
                    this.shopCatPid = groupVO.shopCatPid
                    this.shopId = groupVO.shopId
                    this.sort = groupVO.sort
                }
            }
        }
    }

    /**
     * 是否展示
     */
    fun isGroupShow(): Boolean {
        return TYPE_SHOW == disable
    }

    fun setDisable(enable: Boolean) {
        disable = if (enable) TYPE_SHOW else TYPE_HIDE
    }


}