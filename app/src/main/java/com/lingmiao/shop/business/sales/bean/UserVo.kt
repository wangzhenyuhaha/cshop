package com.lingmiao.shop.business.sales.bean
import com.google.gson.annotations.SerializedName
import java.io.Serializable


/**
Create Date : 2021/3/101:17 AM
Auther      : Fox
Desc        :
 **/
class UserVo : Serializable {
    @SerializedName("create_time")
    var createTime: Long? = 0
    @SerializedName("member_id")
    var memberId: Long? = 0
    @SerializedName("mobile")
    var mobile: String? = ""
    @SerializedName("nickname")
    var nickname: String? = ""
    @SerializedName("order_num")
    var orderNum: Long? = 0
    @SerializedName("real_name")
    var realName: String? = ""
    @SerializedName("remarks")
    var remarks: String? = ""
    @SerializedName("face")
    var face: String? = ""
    @SerializedName("full_address")
    var fullAddress: String? = "";
    @SerializedName("source_name")
    var sourceName: String? = ""
    /**
     * 是否选中
     */
    var isChecked : Boolean ? = false
}
