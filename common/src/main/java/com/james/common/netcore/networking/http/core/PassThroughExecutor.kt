package com.james.common.netcore.networking.http.core

import java.util.concurrent.Executor

/**
 * Author : Elson
 * Date   : 2020/7/11
 * Desc   : 透传执行任务，避免在 call.enqueue() 方法内执行线程调度，而在协程内统一调度线程。
 */
class PassThroughExecutor : Executor {

    override fun execute(command: Runnable?) {
        command?.apply {
            run()
        }
    }
}