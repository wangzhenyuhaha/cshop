package com.james.common.utils.exts

/**
 * Acthor : Elson
 *
 */
abstract class Interceptor {

    private var mInterceptor :  Interceptor? = null

    // 关联下一个拦截器
    fun setNextInterceptor(interceptor: Interceptor?) {
        mInterceptor = interceptor
    }

    abstract fun intercept(success: ((Any?) -> Unit)?, failed: ((Any?) -> Unit)?)
}


fun doIntercept(vararg interceptors: Interceptor, failed: ((Any?) -> Unit)?, success: ((Any?) -> Unit)?) {
    if (interceptors.isEmpty()) {
        success?.invoke(null)
        return
    }
    // 将拦截器链式调用
    interceptors.forEachIndexed { index, interceptor ->
        if (index == interceptors.size - 1) {
            interceptor.setNextInterceptor(null)
        } else {
            interceptor.setNextInterceptor(interceptors[index + 1])
        }
    }
    interceptors.first().intercept(success, failed)
}