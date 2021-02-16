package com.james.common.base

import androidx.lifecycle.LifecycleObserver

interface BasePresenter: LifecycleObserver {
    fun onCreate()
    fun onStart()
    fun onResume()
    fun onPause()
    fun onStop()
    fun onDestroy()
}