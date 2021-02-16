package com.james.common.netcore.networking.http.exception

import java.io.IOException

/**
 * Author : Elson
 * Date   : 2020/7/9
 * Desc   : http状态码异常
 */
class HttpErrorCodeException(val code: Int, val body: String): IOException("$code-$body")