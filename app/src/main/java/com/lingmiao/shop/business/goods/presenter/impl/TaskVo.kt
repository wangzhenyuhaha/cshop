package com.lingmiao.shop.business.goods.presenter.impl

import com.blankj.utilcode.util.ThreadUtils
import java.util.concurrent.CountDownLatch

/**
Create Date : 2021/6/92:41 PM
Auther      : Fox
Desc        :
 **/
public interface TaskVo {

    abstract class TestTask<T>(var mLatch: CountDownLatch) : ThreadUtils.Task<T>() {
        abstract fun onTestSuccess(result: T)
        override fun onSuccess(result: T) {
            onTestSuccess(result)
            mLatch.countDown()
        }

        override fun onCancel() {
            println(Thread.currentThread().toString() + " onCancel: ")
            mLatch.countDown()
        }

        override fun onFail(t: Throwable) {
            println(Thread.currentThread().toString() + " onFail: " + t)
            mLatch.countDown()
        }

    }

    interface TestRunnable<T> {
        fun run(index: Int, latch: CountDownLatch?)
    }

    companion object {

        @Throws(java.lang.Exception::class)
        fun <T> asyncTest(threadCount: Int, runnable: TestRunnable<T>) {
            val latch =
                CountDownLatch(threadCount)
            for (i in 0 until threadCount) {
                runnable.run(i, latch)
            }
            latch.await()
        }

    }
}