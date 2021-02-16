package com.james.common.exception

/**
 * Author : Elson
 * Date   : 2020/7/12
 * Desc   : 自定义业务异常
 */
class BizException : IllegalStateException {

    constructor():super()
    constructor(message: String?):super(message)
    constructor(message: String?, cause: Throwable?):super(message,cause)
    constructor(cause: Throwable?):super(cause)
}