package com.lingmiao.shop

import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
//@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        assertEquals("com.lingmiao.shop", appContext.packageName)
//    }

    companion object {

        const val TYPE_EDIT = 0x001     //编辑
        const val TYPE_DISABLE = 0x002  //下架
        const val TYPE_QUANTITY = 0x004 //库存
        const val TYPE_REBATE = 0x007   //佣金
        const val TYPE_ENABLE = 0x008   //上架
        const val TYPE_DELETE = 0x010   //删除
        const val TYPE_SHARE = 0x013    //分享
    }

    @Test
    fun test() {
        val value = TYPE_SHARE or TYPE_DELETE;
        System.out.println(value != TYPE_SHARE)
    }
}