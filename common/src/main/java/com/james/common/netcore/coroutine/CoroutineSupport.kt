package com.james.common.netcore.coroutine

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

/**
 * Author : Elson
 * Date   : 2020/7/7
 * Desc   : 协程
 */
class CoroutineSupport(parent: Job? = null) : CoroutineScope, LifecycleObserver {

    private val job: Job = if (parent == null) {
        SupervisorJob()
    } else {
        SupervisorJob(parent)
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() {
        job.cancel()
    }

    fun cancelChildren() {
        job.cancelChildren()
    }
}