package com.lingmiao.shop.business.main.bean
import com.google.gson.annotations.SerializedName


data class MessageCenterResponse(
    @SerializedName("data")
    var `data`: List<MessageCenter>?,
    @SerializedName("data_total")
    var dataTotal: Int?,
    @SerializedName("page_no")
    var pageNo: Int?,
    @SerializedName("page_size")
    var pageSize: Int?
)

data class MessageCenter(
    @SerializedName("id")
    var id: Int?,
    @SerializedName("is_delete")
    var isDelete: Int?,
    @SerializedName("is_read")
    var isRead: Int?,//是否已读 ：1已读 0 未读
    @SerializedName("notice_content")
    var noticeContent: String?,//站内信内容
    @SerializedName("send_time")
    var sendTime: Int?,//发送时间
    @SerializedName("shop_id")
    var shopId: Int?,//店铺ID
    @SerializedName("type")
    var type: String?,//消息类型
    @SerializedName("type_text")
    var typeText: String?//消息类型名称
)