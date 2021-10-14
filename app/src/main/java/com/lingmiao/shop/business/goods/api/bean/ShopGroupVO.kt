package com.lingmiao.shop.business.goods.api.bean

import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
 * @author elson
 * @date 2020/7/18
 * @Desc 店铺菜单
 */
class ShopGroupVO : AbstractExpandableItem<ShopGroupVO>(), MultiItemEntity, Serializable {

    @SerializedName("cat_path")
    var catPath: String? = null
    @SerializedName("children")
    var children: MutableList<ShopGroupVO>? = mutableListOf()
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
    @SerializedName("is_event")
    var isEvent: Int = 0
    @SerializedName("is_top")
    var isTop: Int = 0
    @SerializedName("sort")
    var sort: Int = 0
    @SerializedName("goods_num")
    var goods_num: Int = 0
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
                    this.isEvent = groupVO.isEvent
                    this.children = groupVO.children
                    this.disable = groupVO.disable
                    this.shopCatDesc = groupVO.shopCatDesc
                    this.shopCatId = groupVO.shopCatId
                    this.shopCatName = groupVO.shopCatName
                    this.shopCatPic = groupVO.shopCatPic
                    this.shopCatPid = groupVO.shopCatPid
                    this.shopId = groupVO.shopId
                    this.isTop = 1;
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



    var pPosition : Int = 0;

    var showLevel: Int = 0;

    var name : String? = "";

    override fun getItemType(): Int {
        return showLevel;
    }

    override fun getLevel(): Int {
        return (showLevel ?: 1) - 1;
    }


    var thumbnail : String? = ""


}