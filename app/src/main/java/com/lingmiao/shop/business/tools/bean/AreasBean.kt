package com.lingmiao.shop.business.tools.bean
import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lingmiao.shop.business.tools.adapter.AreasAdapter
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class Provinces(
    @SerializedName("provinces")
    var provinces: ProvincesX? = null
) : Serializable

data class ProvincesX(
    @SerializedName("province")
    var province: List<Province>? = listOf()
) : Serializable

data class Province(
    @SerializedName("cities")
    var cities: Cities? = Cities(),
    @SerializedName("ssqename")
    var ssqename: String? = "",
    @SerializedName("ssqid")
    var ssqid: String? = "",
    @SerializedName("ssqname")
    var ssqname: String? = "",
    var isCheck : Boolean = false
) : Serializable, AbstractExpandableItem<City>(), MultiItemEntity {
    override fun getLevel(): Int {
        return AreasAdapter.TYPE_LEVEL_0;
    }

    override fun getItemType(): Int {
        return AreasAdapter.TYPE_LEVEL_0;
    }

    fun shiftAllCheck(flag : Boolean) {
        isCheck = flag;
        cities?.apply {
            city?.forEach { city : City ->
                city.shiftAllCheck(flag);
            }
        }
    }

    fun isCheckAll() : Boolean {
        var isCheckAll = true;
        cities?.apply {
            city?.forEach { c: City ->
                if(!c?.isCheckAll()) {
                    isCheckAll = false;
                }
            }
        }
        return isCheckAll;
    }

    fun addItem() {
        cities?.apply {
            city?.forEach { city : City ->
                addSubItem(city);
                city?.addItem();
            }
        }
    }
}

data class Cities(
    @SerializedName("city")
    var city: List<City>? = listOf()
) : Serializable

data class City(
    @SerializedName("areas")
    var areas: Areas? = Areas(),
    @SerializedName("ssqename")
    var ssqename: String? = "",
    @SerializedName("ssqid")
    var ssqid: String? = "",
    @SerializedName("ssqname")
    var ssqname: String? = "",
    var isCheck : Boolean = false,
    var pPosition : Int ? = -1
) : Serializable, AbstractExpandableItem<Area>(), MultiItemEntity {
    override fun getLevel(): Int {
        return AreasAdapter.TYPE_LEVEL_1;
    }

    override fun getItemType(): Int {
        return AreasAdapter.TYPE_LEVEL_1;
    }

    fun shiftAllCheck(flag : Boolean) {
        isCheck = flag;
        areas?.apply {
            area?.forEach { area : Area ->
                area.shiftAllCheck(flag);
            }
        }
    }

    fun isCheckAll() : Boolean {
        var isCheckAll = true;
        areas?.apply {
            area?.forEach { a : Area ->
                if(!a?.isCheck) {
                    isCheckAll = false;
                }
            }
        }
        return isCheckAll;
    }

    fun addItem() {
        areas?.apply {
            area?.forEach { area : Area ->
                addSubItem(area);
            }
        }
    }
}

data class Areas(
    @SerializedName("area")
    var area: List<Area>? = listOf()
) : Serializable

data class Area(
    @SerializedName("ssqename")
    var ssqename: String? = "",
    @SerializedName("ssqid")
    var ssqid: String? = "",
    @SerializedName("ssqname")
    var ssqname: String? = "",
    var isCheck : Boolean = false,
    var pPosition : Int ? = -1,
    var cPosition : Int ? = -1
) : Serializable, MultiItemEntity {

    override fun getItemType(): Int {
        return AreasAdapter.TYPE_AREA;
    }

    fun shiftAllCheck(flag : Boolean) {
        isCheck = flag;
    }

}
