package com.lingmiao.shop.business.goods.api.bean
import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.lingmiao.shop.business.commonpop.bean.ItemData
import com.lingmiao.shop.business.goods.pop.CateMenuPop
import com.lingmiao.shop.business.goods.pop.GoodsMenuPop


/**
 * @author elson
 * @date 2020/7/16
 * @Desc 商品分类
 */
data class CategoryVO(
    @SerializedName("seller_id")
    var sellerId: String = "",
    @SerializedName("category_id")
    var categoryId: String = "",
    @SerializedName("category_order")
    var categoryOrder: Int= 0,
    @SerializedName("category_path")
    var categoryPath: String= "",
    @SerializedName("goods_count")
    var goodsCount: Int= 0,
    @SerializedName("image")
    var image: String= "",
    @SerializedName("name")
    var name: String= "",
    @SerializedName("parent_id")
    var parentId: Int= 0,
    @SerializedName("children")
    var children : MutableList<CategoryVO>? = mutableListOf(),
    @Expose
    var parentLevel: Int = 0,
    @Expose
    var _loaded : Boolean = false,

    var selected: Boolean = false
) : AbstractExpandableItem<CategoryVO>(), MultiItemEntity, ItemData {
    override fun getIValue(): String? {
        return categoryId;
    }

    override fun getIName(): String? {
        return name;
    }

    override fun getIHint(): String? {
        return name;
    }

    override fun getItemType(): Int {
        return showLevel;
    }

    override fun getLevel(): Int {
        return (showLevel ?: 1) - 1;
    }

    var pPosition : Int = 0;
    /**
     * 是否选中
     */
    var isChecked : Boolean ? = false

    var showLevel: Int = 0;

    fun getMenuType(): Int {
        return when (showLevel) {
            0 -> (CateMenuPop.TYPE_GOODS_INFO or CateMenuPop.TYPE_SPEC or CateMenuPop.TYPE_EDIT or CateMenuPop.TYPE_CHILDREN)
            else -> (CateMenuPop.TYPE_GOODS_INFO or CateMenuPop.TYPE_SPEC or CateMenuPop.TYPE_EDIT)
        }
    }
}
