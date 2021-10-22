package com.lingmiao.shop.business.main.bean

data class TabChangeEvent(var type: Int, var status: String? = "",var startTime:Long? = null,var endTime:Long? = null) {}