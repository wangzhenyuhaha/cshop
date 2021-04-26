package com.lingmiao.shop.business.goods.api.bean
import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.google.gson.annotations.SerializedName
import com.lingmiao.shop.business.commonpop.bean.ItemData


/**
 * @author elson
 * @date 2020/7/16
 * @Desc 商品分类
 */
data class CategoryVO(

    @SerializedName("category_id")
    var categoryId: String? = null,
    @SerializedName("category_order")
    var categoryOrder: Int?= null,
    @SerializedName("category_path")
    var categoryPath: String?= null,
    @SerializedName("goods_count")
    var goodsCount: Int?= null,
    @SerializedName("image")
    var image: String?= null,
    @SerializedName("name")
    var name: String?= null,
    @SerializedName("parent_id")
    var parentId: String?= null
) : AbstractExpandableItem<MenuVo>(), MultiItemEntity, ItemData {
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

}
