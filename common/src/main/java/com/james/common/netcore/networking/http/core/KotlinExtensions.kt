@file:JvmName("KotlinExtensions")

package com.james.common.netcore.networking.http.core

/*
 * Copyright (C) 2018 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.james.common.netcore.networking.http.exception.DataErrorException
import com.james.common.netcore.networking.http.exception.HttpErrorCodeException
import kotlinx.coroutines.*
import retrofit2.*
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * @author elson, email:liuqi2elson@163.com
 * @date 2020/7/10
 * @Desc 扩展框架协程，捕获异常统一处理。
 */

inline fun <reified T> Retrofit.create(): T = create(T::class.java)


/**
 * 等待请求返回。
 *
 */
suspend fun <T : Any?> Call<T>.awaitHiResponse(): HiResponse<T>  {
    return awaitHiResponseInner(false)
}

@Throws(CancellationException::class, Exception::class)
suspend fun <T : Any?> Call<T>.awaitHiResponseWithException(): HiResponse<T>  {
    return awaitHiResponseInner(true)
}


@Throws(CancellationException::class, Exception::class)
private suspend fun <T : Any?> Call<T>.awaitHiResponseInner(throwsException: Boolean): HiResponse<T> {

    try {
        val resp: HiResponse<T> = suspendCancellableCoroutine { continuation ->
            continuation.invokeOnCancellation {
                try {
                    cancel()
                } catch (ex: Throwable) {
                    //do nothing...
                }
            }
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    if (response.isSuccessful) {
                        val body: T? = response.body()
                        if (body == null) {
                            val invocation = call.request().tag(Invocation::class.java)!!
                            val method = invocation.method()
                            val e = IOException(
                                "Response from "
                                        + method.declaringClass.name + '.' + method.name
                                        + " was null but response body type was declared as non-null"
                            )
                            if (!call.isCanceled) {
                                continuation.resumeWithException(e)
                            }
                        } else {
                            if (!call.isCanceled) {
                                if (body is HiResponse<*>) {
                                    continuation.resume(body as HiResponse<T>)
                                } else if (body is Unit) {
                                    continuation.resume(HiResponse<T>(0, "", body))
                                }
                            }
                        }
                    } else {
                        if (!call.isCanceled) {
                            continuation.resumeWithException(HttpException(response))
                        }
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    if (!call.isCanceled) {
                        continuation.resumeWithException(t)
                    }
                }
            })
        }

        return resp
    } catch (ce: CancellationException) {
        ce.printStackTrace()
        throw ce

    } catch (e: Exception) {
        e.printStackTrace()

        val resp = HiResponse<Any>()
        resp.msg = ""
        resp.isException = true
        if (e is DataErrorException) {
            resp.code = HiResponseCode.DATA_ERROR
        } else if (e is HttpException) {
            resp.code = e.code()
            val msg = e.response()?.errorBody()?.string()
            resp.msg = msg
        } else if (e is IOException || e is HttpErrorCodeException) {
            resp.code = HiResponseCode.NET_FAILED
        } else {
            resp.code = HiResponseCode.UNKNOW_FAILED
        }

        if (throwsException) {
            throw e
        }
        return resp as HiResponse<T>
    }

}
