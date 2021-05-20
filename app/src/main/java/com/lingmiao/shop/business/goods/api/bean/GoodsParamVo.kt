package com.lingmiao.shop.business.goods.api.bean

import com.google.gson.annotations.SerializedName
import com.lingmiao.shop.business.goods.view.FlowViewVo
import java.io.Serializable

/**
Create Date : 2021/5/147:54 PM
Auther      : Fox
Desc        :
 **/
class GoodsParamVo : FlowViewVo, Serializable {

    @SerializedName("category_id")
    var categoryId: String? = ""
    /**
     * 参数分组id
     */
    @SerializedName("group_id")
    var groupId: Int? = 0

    /**
     * 是否可索引，0 不显示 1 显示
     */
    @SerializedName("is_index")
    var isIndex: Int? = null

    /**
     * 选择值，当参数类型是选择项2时，必填，逗号分隔
     */
//    @SerializedName("options")
//    var options: Any? = Any()

    @SerializedName("param_id")
    var paramId: Int? = 0
    /**
     * 参数名称
     */
    @SerializedName("param_name")
    var paramName: String? = null

    /**
     * 参数值
     */
    @SerializedName("param_value")
    var paramValue: String? = null

    /**
     * 参数类型1 输入项 2 选择项
     */
    @SerializedName("param_type")
    var paramType: Int? = 0

    /**
     * 是否必填是 1 否 0
     */
    @SerializedName("required")
    var required: Int? = 0


    @SerializedName("sort")
    var sort: Int? = 0

    override fun getFName(): String? {
        return paramName;
    }

    override fun getFId(): String? {
        return paramId?.toString();
    }

}