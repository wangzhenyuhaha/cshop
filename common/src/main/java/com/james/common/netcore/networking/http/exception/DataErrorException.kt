package com.james.common.netcore.networking.http.exception

import java.io.IOException

/**
 * Author : Elson
 * Date   : 2020/7/9
 * Desc   : 数据解析异常
 */
class DataErrorException(message: String?) : IOException(message)