package com.lingmiao.shop.business.main.bean

import java.util.HashMap

/**
 * OCR识别类型枚举
 */
enum class DiscernTypeEnum(val index: Int, val text: String) {


    ID_CARD_FRONT(1, "身份证正面"),
    ID_CARD_BACK(2, "身份证反面"),
    DRIVER_LICENSE(3, "驾驶证"),
    VEHICLE_LICENSE_FRONT(4, "行驶证主页"),
    VEHICLE_LICENSE_BACK(5, "行驶证副页"),
    BANK_CARD(6, "银行卡"),
    GENERAL_BASIC(7, "通用印刷体"),  // 未实现
    BIZ_LICENSE(8, "营业执照");
}
