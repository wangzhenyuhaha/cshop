package com.lingmiao.shop.business.tools.bean
import com.chad.library.adapter.base.entity.AbstractExpandableItem
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.lingmiao.shop.business.tools.adapter.AreasAdapter
import com.google.gson.annotations.SerializedName
import com.lingmiao.shop.business.commonpop.bean.ItemData
import java.io.Serializable

data class TempRegionVo(
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("level")
    var level: Int? = 0,
    @SerializedName("local_name")
    var localName: String? = "",
    @SerializedName("parent_id")
    var parentId: Int? = 0,
    @SerializedName("selected_all")
    var selectedAll : Boolean ? = false,
    @SerializedName("children")
    var children: Map<String, Any>? = hashMapOf()
) : Serializable {
    constructor(item : RegionVo) : this() {
        id = item?.id;
        level = item?.level;
        localName = item?.localName;
        parentId = item?.parentId;
        selectedAll = item?.selectedAll;
        children = item?.children?.associateBy({it?.id.toString()}, {TempRegionVo(it)})?.toMap();
    }
}


data class TempArea(
    /**
     * 所有解释的区域
     */
    var allParsedAreas : MutableList<Map<String, Map<String, Any>>> ?= arrayListOf(),
    /**
     * 所有选中的区域
     */
    var selectedIds : MutableList<String>
) : Serializable {

    /**
     * 是否存在于所有已选中的区域中
     */
    fun existId(id : Int?) : Boolean {
        return selectedIds?.contains(String.format("%s", id));
    }
}

class FreightVo : ArrayList<FreightVoItem>()

data class FreightVoItem(
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("seller_id")
    var sellerId: Int? = 0,
    /**
     * 名字
     */
    @SerializedName("name")
    var name: String? = "",
    /**
     * 模板通途: TONGCHENG   QISHOU
     */
    @SerializedName("template_type")
    var templateType: String? = "",

    /**
     * 配送费用
     */
    //baseDistance 基础距离
    //basePrice  基础价格
    //feeType   费用配置类型 1.基础距离配送费计算 2.距离区间配送费计算
    //unitDistance 单位距离
    //unitPrice  单位费用
    @SerializedName("fee_setting")
    var feeSetting: String? = "",

    /**
     * 配送时间
     */
    //baseDistance  基础距离
    //baseTime  基础价格
    //isAllowTransTemp  是否允许转换配送模版（是否允许骑手转商家）
    //readyTime  商家准备时间
    //timeType  时间配置类型
    //transTempLimitTime  转换模版时间配置
    //unitDistance  单位距离
    //unitTime  单位费用
    @SerializedName("time_setting")
    var timeSetting: String? = "",
    /**
     * 模版类型，1 重量算运费 2 计件算运费
     */
    @SerializedName("type")
    var type: Int? = 0,
    /**
     * 起送价格
     */
    @SerializedName("base_ship_price")
    var baseShipPrice: String? = "",
    /**
     * 配送范围
     */
    @SerializedName("ship_range")
    var shipRange: String? = "",
    /**
     * 最低配送价格
     */
    @SerializedName("min_ship_price")
    var minShipPrice: Double? = 0.0,

    @SerializedName("items")
    var items: MutableList<Item>? = arrayListOf(),

    @SerializedName("local_template")
    var localTemplate: Any? = Any(),
    var tempArea : TempArea ? = null,

    @SerializedName("fee_setting_vo")
    var feeSettingVo: FeeSettingReqVo? = FeeSettingReqVo(),

    @SerializedName("time_setting_vo")
    var timeSettingVo: TimeSettingReqVo? = TimeSettingReqVo()
) : Serializable{

    companion object {
        val TYPE_KUAIDI = "KUAIDI";

        val TYPE_QISHOU = "QISHOU";

        val TYPE_LOCAL = "TONGCHENG";

        val TYPE_WEIGHT = 1;
        val TYPE_COUNT = 2;
        val TYPE_KM_COUNT = 3;
        val TYPE_KM_SECTION = 4;

    }

    fun isKuaiDiType() : Boolean {
        return TYPE_KUAIDI == templateType;
    }

    fun isLocalTemplateType() : Boolean {
        return TYPE_LOCAL == templateType;
    }

    fun getTemplateTypeName() : String {
        return if(isLocalTemplateType()) "同城" else "快递";
    }

    fun getTypeName() : String {
        return when(type) {
            TYPE_WEIGHT -> "按重量计算";
            TYPE_COUNT -> "按件数计算";
            TYPE_KM_COUNT -> "按公里数计算";
            TYPE_KM_SECTION -> "按公里段计算";
            else -> "";
        }
    }

    fun isWeightType() : Boolean {
        return type == TYPE_WEIGHT;
    }

    fun existAreaId(id : Int?) : Boolean {
        return tempArea?.existId(id) ?: false;
    }

}

/**
 * 指定配送区域
 */
data class Item(
    /**
     * 首重／首件
     */
    @SerializedName("first_company")
    var firstCompany: String? = "",
    /**
     * 运费
     */
    @SerializedName("first_price")
    var firstPrice: String? = "",
    /**
     * 续重／需件
     */
    @SerializedName("continued_company")
    var continuedCompany: String? = "",
    /**
     * 续费
     */
    @SerializedName("continued_price")
    var continuedPrice: String? = "",
    /**
     * 地区‘，‘分隔   示例参数：北京，山西，天津，上海
     */
    @SerializedName("area")
    var area: String? = "",
    /**
     * 地区id‘，‘分隔  示例参数：1，2，3，4
     */
    @SerializedName("area_id")
    var areaId: String? = "",
    @SerializedName("regions")
    var regions: List<Region>? = listOf(),
    var parsedArea : Map<String, Map<String, Any>> ? = null,
    var selectedIds : MutableList<String> ? = arrayListOf()
) : Serializable {

    fun getAreaStr() : String {
        val str = StringBuilder();
        regions?.forEachIndexed { index : Int, it : Region ->
            if(index > 0 && index < regions?.size ?: 0) {
                str.append("、")
            }
            str.append(it?.name);
            it?.children?.forEachIndexed { cIndex : Int, cItem : Region ->
                if(cIndex == 0 && it?.children?.size ?: 0 > 0) {
                    str.append("(");
                }
                str.append(cItem?.name);
                if(it?.children?.size == cIndex +1) {
                    str.append(")");
                }
            }
        }
        return str.toString();
    }

    fun existId(id : Int?) : Boolean {
        return selectedIds?.contains(String.format("%s", id)) ?: false;
    }

}

data class Region(
    @SerializedName("children")
    var children: ArrayList<Region>? = null,
    @SerializedName("name")
    var name: String? = ""
) : Serializable {

}

data class TempRegion(
    @SerializedName("children")
    var children: HashMap<String, TempRegion>? = hashMapOf(),
    @SerializedName("id")
    var id: String? = "",
    @SerializedName("isSelected")
    var isSelected: Boolean? = false,
    @SerializedName("level")
    var level: Int? = 0,
    @SerializedName("local_name")
    var localName: String? = "",
    @SerializedName("parent_id")
    var parentId: Int? = 0,
    @SerializedName("selected_all")
    var selectedAll: Boolean? = false
) : Serializable {

    fun isChecked() : Boolean {
        return selectedAll == true;
    }
}


data class RegionVo(
    @SerializedName("children")
    var children: MutableList<RegionVo>? = mutableListOf(),
    @SerializedName("id")
    var id: Int? = 0,
    @SerializedName("level")
    var level: Int? = 0,
    @SerializedName("local_name")
    var localName: String? = "",
    @SerializedName("parent_id")
    var parentId: Int? = 0,
    @SerializedName("selected_all")
    var selectedAll : Boolean ? = false,
    var isCheck : Boolean = false,
    var pPosition : Int ? = -1,
    var cPosition : Int ? = -1
) : AbstractExpandableItem<RegionVo>(), MultiItemEntity, ItemData, Serializable {

    override fun getLevel(): Int {
        return (level ?: 1) - 1;
    }

    fun isLastNode() : Boolean {
        return getLevel() == AreasAdapter.TYPE_AREA;
    }

    fun isCityLevel() : Boolean {
        return getLevel() == AreasAdapter.TYPE_LEVEL_1;
    }

    fun isProvinceLevel() : Boolean {
        return getLevel() == AreasAdapter.TYPE_LEVEL_0;
    }

    override fun getItemType(): Int {
        return getLevel();
    }

    fun shiftAllCheck(flag : Boolean) {
        when(getLevel()) {
            AreasAdapter.TYPE_LEVEL_0 -> {
                isCheck = flag;
                children?.apply {
                    forEach{ it : RegionVo ->
                        it.shiftAllCheck(flag);
                    }
                }
            }
            AreasAdapter.TYPE_LEVEL_1 -> {
                isCheck = flag;
                children?.apply {
                    forEach { area : RegionVo ->
                        area.shiftAllCheck(flag);
                    }
                }
            }
            AreasAdapter.TYPE_AREA -> {
                isCheck = flag;
            }
            else -> {

            }
        }
    }

    fun isCheckAll() : Boolean {
        var isCheckAll = true;
        when(getLevel()) {
            AreasAdapter.TYPE_LEVEL_0 -> {
                var isCheckAll = true;
                children?.apply {
                    forEach { c: RegionVo ->
                        if(!c?.isCheckAll()) {
                            isCheckAll = false;
                        }
                    }
                }
                return isCheckAll;
            }
            AreasAdapter.TYPE_LEVEL_1 -> {
                children?.apply {
                    forEach { a : RegionVo ->
                        if(!a?.isCheck) {
                            isCheckAll = false;
                        }
                    }
                }
            }
            AreasAdapter.TYPE_AREA -> {
                isCheckAll = isCheck;
            }
            else -> {

            }
        }
        return isCheckAll;
    }

    fun addItem(item: Item?, mTempArea: TempArea?) {
        if(item?.existId(id) == true) {
            shiftAllCheck(true);
        }
        when(getLevel()) {
            AreasAdapter.TYPE_LEVEL_0 -> {
                children?.apply {
                    forEach{ city : RegionVo ->
//                        LogUtils.dTag("addItem " + city?.level + " : " + city?.localName);
//                        addSubItem(city);
//                        city?.addItem(item, mTempArea);
                        if(item?.existId(city?.id) == true) {
                            city?.isCheck = true;
                            city?.shiftAllCheck(true);
                            addSubItem(city);
                            city?.addItem(item, mTempArea);
                        } else {
                            if(mTempArea?.existId(city?.id) != true) {
                                addSubItem(city);
                                city?.addItem(item, mTempArea);
                            }
                        }
                    }
                }
            }
            AreasAdapter.TYPE_LEVEL_1 -> {
                children?.apply {
                    forEach { area : RegionVo ->
//                        LogUtils.dTag("addItem " + area?.level + " : " + area?.localName);
                        if(item?.existId(area?.id) == true) {
                            area?.isCheck = true;
                            area?.shiftAllCheck(true);
                            addSubItem(area);
                            area?.addItem(item, mTempArea);
                        } else {
                            if(mTempArea?.existId(area?.id) != true) {
                                addSubItem(area);
                                area?.addItem(item, mTempArea);
                            }
                        }
                    }
                }
            }
            AreasAdapter.TYPE_AREA -> {
//                LogUtils.dTag("addItem " + level + " : " + localName);
            }
            else -> {

            }
        }
    }

    override fun getIValue(): String? {
        return id?.toString();
    }

    override fun getIName(): String? {
        return localName;
    }

    override fun getIHint(): String? {
        return localName;
    }

    override fun isItemChecked(): Boolean {
        return isCheck;
    }

    override fun shiftChecked(flag: Boolean?) {
        isCheck = flag ?: false;
    }
}

data class FeeSettingVo(
    @SerializedName("baseDistance")
    var baseDistance: String? = "",
    @SerializedName("basePrice")
    var basePrice: String? = "",
    @SerializedName("distanceSections")
    var distanceSections: MutableList<DistanceSection>? = arrayListOf(),
    /**
     * 1:基础距离计价
     * 2:距离区间计价
     */
    @SerializedName("feeType")
    var feeType: Int? = 0,
    @SerializedName("peekTimes")
    var peekTimes: MutableList<PeekTime>? = arrayListOf(),
    @SerializedName("unitDistance")
    var unitDistance: String? = "",
    @SerializedName("unitPrice")
    var unitPrice: String? = ""
) : Serializable {
    companion object {
        val FEE_TYPE_DISTANCE : Int = 1;
        val FEE_TYPE_SECTION : Int = 2;
    }

    fun isDistanceType() : Boolean {
        return feeType == FEE_TYPE_DISTANCE;
    }
}

data class DistanceSection(
    @SerializedName("startDistance")
    var startDistance: String? = "",
    @SerializedName("shipPrice")
    var shipPrice: String? = "",
    @SerializedName("endDistance")
    var endDistance: String? = ""
): Serializable {

}

data class PeekTime(
    @SerializedName("endTimeCount")
    var endTimeCount: Int? = null,
    //结束时间
    @SerializedName("peekTimeEnd")
    var peekTimeEnd: String? = "",
    //加收价格
    @SerializedName("peekTimePrice")
    var peekTimePrice: String? = "",
    //开始时间
    @SerializedName("peekTimeStart")
    var peekTimeStart: String? = "",
    @SerializedName("startTimeCount")
    var startTimeCount: Int? = -1,
    //加收费用是否可操作(默认可操作)
    var enable: Boolean = true
): Serializable {

}

data class TimeSettingVo(
    @SerializedName("baseDistance")
    var baseDistance: String? = "",
    @SerializedName("baseTime")
    var baseTime: String? = "",
    @SerializedName("timeSections")
    var timeSections: MutableList<TimeSection>? = arrayListOf(),
    /**
     * 1:基础时间
     * 2:时间段配送
     */
    @SerializedName("timeType")
    var timeType: Int? = 0,
    @SerializedName("unitDistance")
    var unitDistance: String? = "",
    @SerializedName("unitTime")
    var unitTime: String? = "",
    @SerializedName("readyTime")
    var readyTime : Int? = 0,
    @SerializedName("transTempLimitTime")
    var transTempLimitTime : Int? = 0,
    @SerializedName("isAllowTransTemp")
    var isAllowTransTemp : Int? = 0

) : Serializable {
    companion object {
        // 公里数
        val TIME_TYPE_BASE = 1;
        // 时间段
        val TIME_TYPE_SECTION = 2;
    }

    fun isBaseTimeType() : Boolean {
        return timeType == TIME_TYPE_BASE;
    }

    fun convertReqVo() : TimeSettingReqVo {
        return TimeSettingReqVo(baseDistance, baseTime, convertTimeSection(), timeType,unitDistance,unitTime, readyTime,transTempLimitTime,isAllowTransTemp);
    }

    fun convertTimeSection() : MutableList<TimeSectionReq> {
        val items: MutableList<TimeSectionReq> = arrayListOf();
        timeSections?.forEachIndexed { index, timeSection ->
            items?.add(timeSection.toReqVo());
        }
        return items;
    }

}

data class TimeSection(
    @SerializedName("arriveTime")
    var arriveTime: String? = "",
    @SerializedName("shipTime")
    var shipTime: String? = "",
    @SerializedName("shipTimeType")
    var shipTimeType: Int? = 1,
    var arriveTimeCount : Int ? = null,
    var shipTimeCount : Int ? = null
) : Serializable {

    fun shiftTomorrow() {
        shipTimeType = 2;
    }

    fun shiftToday() {
        shipTimeType = 1;
    }

    fun isToday() : Boolean {
        return shipTimeType == 1;
    }

    fun getTimeType() : String {
        return if(isToday()) "当日" else "次日";
    }

    fun toReqVo() : TimeSectionReq {
        return TimeSectionReq(arriveTime, shipTime, shipTimeType)
    }
}

data class FeeSettingReqVo(
    //基础距离
    @SerializedName("base_distance")
    var baseDistance: String? = "",
    //基础价格
    @SerializedName("base_price")
    var basePrice: String? = "",
    //单位距离
    @SerializedName("unit_distance")
    var unitDistance: String? = "",
    //单位费用
    @SerializedName("unit_price")
    var unitPrice: String? = "",
    //
    @SerializedName("distance_sections")
    var distanceSections: List<DistanceSectionReq>? = listOf(),
    //费用配置类型 1.基础距离配送费计算 2.距离区间配送费计算
    @SerializedName("fee_type")
    var feeType: Int? = 0,
    //
    @SerializedName("peek_times")
    var peekTimes: List<PeekTimeReq>? = listOf()
) : Serializable {
    constructor(item : FeeSettingVo) : this() {
        baseDistance = item?.baseDistance;
        basePrice = item?.basePrice;
        unitDistance = item?.unitDistance;
        unitPrice = item?.unitPrice;
        feeType = item?.feeType;
        peekTimes = parsePeekTimes(item?.peekTimes);
        distanceSections = parseDistances(item?.distanceSections);
    }

    fun parsePeekTimes(times : MutableList<PeekTime>?) : MutableList<PeekTimeReq> {
        var list : MutableList<PeekTimeReq> = arrayListOf();
        if(times != null) {
            for(item in times) {
                list.add(PeekTimeReq(item));
            }
        }
        return list;
    }

    fun parseDistances(distances : List<DistanceSection>?) : MutableList<DistanceSectionReq> {
        var list : MutableList<DistanceSectionReq> = arrayListOf();
        if(distances != null) {
            for(item in distances) {
                list.add(DistanceSectionReq(item));
            }
        }
        return list;
    }
}

data class TimeSettingReqVo(


    //baseDistance  基础距离
    @SerializedName("base_distance")
    var baseDistance: String? = "",

    //baseTime  基础价格
    @SerializedName("base_time")
    var baseTime: String? = "",
    @SerializedName("time_sections")
    var timeSections: List<TimeSectionReq>? = listOf(),

    //timeType  时间配置类型
    @SerializedName("time_type")
    var timeType: Int? = 0,

    //unitDistance  单位距离
    @SerializedName("unit_distance")
    var unitDistance: String? = "",

    //unitTime  单位费用
    @SerializedName("unit_time")
    var unitTime: String? = "",

    //readyTime  商家准备时间
    @SerializedName("ready_time")
    var readyTime : Int? = 0,

    //transTempLimitTime  转换模版时间配置
    @SerializedName("trans_temp_limit_time")
    var transTempLimitTime : Int? = 0,
    //isAllowTransTemp  是否允许转换配送模版（是否允许骑手转商家）
    @SerializedName("is_allow_trans_temp")
    var isAllowTransTemp : Int? = 0
    
) : Serializable{
    constructor(item : TimeSettingVo) : this() {
        baseDistance = item?.baseDistance;
        baseTime = item?.baseTime;
        timeSections = parseSections(item?.timeSections);
        timeType = item?.timeType;
        unitDistance = item?.unitDistance;
        unitTime = item?.unitTime;
        readyTime = item?.readyTime
    }

    fun parseSections(sections : List<TimeSection>?) : MutableList<TimeSectionReq> {
        var list : MutableList<TimeSectionReq> = arrayListOf();
        if(sections != null) {
            for(item in sections) {
                list.add(TimeSectionReq(item));
            }
        }
        return list;
    }

}

data class DistanceSectionReq(
    @SerializedName("end_distance")
    var endDistance: Any? = Any(),
    @SerializedName("ship_price")
    var shipPrice: Any? = Any(),
    @SerializedName("start_distance")
    var startDistance: String? = ""
) : Serializable {
    constructor(item : DistanceSection) : this() {
        endDistance = item?.endDistance;
        shipPrice = item?.shipPrice;
        startDistance = item?.startDistance;
    }
}

data class PeekTimeReq(
    @SerializedName("end_time_count")
    var endTimeCount: Int? = 0,
    @SerializedName("peek_time_end")
    var peekTimeEnd: String? = "",
    @SerializedName("peek_time_price")
    var peekTimePrice: String? = "",
    @SerializedName("peek_time_start")
    var peekTimeStart: String? = "",
    @SerializedName("start_time_count")
    var startTimeCount: Int? = 0
) : Serializable {
    constructor(item : PeekTime) : this() {
        endTimeCount = item?.endTimeCount;
        peekTimeEnd = item?.peekTimeEnd;
        peekTimePrice = item?.peekTimePrice;
        peekTimeStart = item?.peekTimeStart;
        startTimeCount = item?.startTimeCount;
    }
}

data class TimeSectionReq(
    @SerializedName("arrive_time")
    var arriveTime: String? = "",
    @SerializedName("ship_time")
    var shipTime: String? = "",
    @SerializedName("ship_time_type")
    var shipTimeType: Int? = 0
) : Serializable {
    constructor(item : TimeSection) : this() {
        arriveTime = item?.arriveTime;
        shipTime = item?.shipTime;
        shipTimeType = item?.shipTimeType;
    }
}

data class TimeValue(
    var name: String = "",
    var value: Int = 0
) {

    companion object {
        fun getTimeList() : MutableList<TimeValue> {
            val mTimeValueList : MutableList<TimeValue> = mutableListOf();
            var temp = 0
            var tempName = ""
            var tempValue = 0
            for(index in 0..47) {
                tempName = String.format("%s%s:%s", if(temp < 10) "0" else "", temp, if(index % 2 == 0) "00" else "30");
                tempValue = tempName.replace(":", "").toInt();
                mTimeValueList.add(TimeValue(tempName, tempValue));
                if((index + 1) % 2  == 0) {
                    temp += 1;
                }
            }
            return mTimeValueList;
        }

        fun getLastTimeCount() : Int {
            return 2350;
        }
    }

}

