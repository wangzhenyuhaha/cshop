#mkt

## 目录
1. 命名规范
2. 网络库使用
3. 圆角控件
4. Dialog弹窗
5. 分页加载

## 1.命名规范

**命名约定：**
1. Activity： `模块名_activity_页面_(扩展，选填)`
2. Fragment： `模块名_fragment_页面_(扩展，选填)`
3. Dialog： `模块名_dialog_页面_(扩展，选填)`
4. Popup： `模块名_pop_页面_(扩展，选填)`
5. 自定义View： `模块名_view_页面_(扩展，选填)`
6. Adapter的Item：`模块名_adapter_页面_(扩展，选填)`


**模块前缀约定：**
1. 注册模块名前缀 `reg_`
2. 首页模块名前缀 `main_`
3. 商品模块名前缀 `goods_`
4. 钱包模块名前缀 `wallet_`
5. 商品模块名前缀 `order_`
6. 个人中心模块名前缀 `me_`
7. 团购模块前缀 `tuan_`
8. 物流工具前缀 `tools_`

---

## 2.网络库使用

推荐使用第1中写法

```kotlin
interface ApiService {
    /**
     * 使用统一返回数据结构，需要使用注解 @WithHiResponse
     */
    @GET("seller/goods")
    @WithHiResponse
    fun getGoodList(@Query("page_no") page_no: String, @Query("page_size") page_size: String): Call<GoodList>

    @GET("seller/goods")
    suspend fun getGoodList1(@Query("page_no") page_no: String, @Query("page_size") page_size: String): GoodList?
}


val mCoroutine: CoroutineSupport by lazy { CoroutineSupport() }

mCoroutine.launch {
    // 推荐使用方式1：返回 HiResponse<GoodList>数据结构，HiResponse内统一处理状态。
    val resp = Fetch.apiService().getGoodList("1", "10").awaitHiResponse()
    if (resp.isSuccess) {
        LogUtils.dTag("SplashActivity", resp)
    }

    //方式2，直接返回GoodList数据结构，接口返回失败会走 catch 逻辑。
    try {
        val resp1 = Fetch.apiService().getGoodList1("1", "10")
        LogUtils.dTag("SplashActivity", resp1)
    } catch (e: Exception) {
        // 对失败的接口做处理
    }
}
```

```
FORM 表单
    @FormUrlEncoded
    @PUT("xxx/xx/xxx/{demo}
    @WithHiResponse
    fun batchRebate(@Path(value = "demo") demo: String, @FieldMap map: MutableMap<String, Any>) : Call<Boolean>
```

## 3.圆角控件
ITextView 带圆角/边框背景的TextView.ILinearLayout等也是类似的.
```xml
 <com.james.common.view.ITextView
                android:layout_width="72dp"
                android:layout_height="30dp"
                android:text="取消订单"
                android:layout_marginLeft="10dp"
                android:textColor="#ff3870ea"
                android:textSize="12sp"
                app:ivBgColor="#fff"
                app:ivRadius="15dp"
                android:gravity="center"
                app:ivBorderWidth="1px"
                app:ivBorderColor="#ff3870ea"
                />

 <com.james.common.view.ITextView
                android:layout_width="72dp"
                android:layout_height="30dp"
                android:text="催付"
                android:layout_marginLeft="10dp"
                android:textColor="#fff"
                android:textSize="12sp"
                app:ivBgColor="#ff3870ea"
                app:ivRadius="15dp"
                android:gravity="center"
                app:ivBorderWidth="1px"
                app:ivBorderColor="#ff3870ea"
                />
```


## 4.Dialog弹窗
通用对话框弹窗: DialogUtils.showDialog
```kotlin
 DialogUtils.showDialog(this,"xxx","ni hao xxx",
            View.OnClickListener { showToast("cancel") },
            View.OnClickListener { showToast("confirm") })
```


## 5.分页加载

1. 页面继承 `BaseLoadMoreFragment`
2. 页面接口 View 继承 `BaseLoadMoreView`
3. BaseLoadMoreFragment 支持自动触发加载操作，会触发 `BaseLoadMoreView.executePageRequest(IPage)`，因此在此处调用接口，IPage 可以获取当前处于第几页等参数信息。
4. 接口调用成功后，需要在 Presenter 手动调用 `BaseLoadMoreView.onLoadMoreSuccess()`，里面封装了对下拉刷新控件的统一操作。
5. 接口调用失败后，需要在 Presenter 手动调用 `BaseLoadMoreView.onLoadMoreFailed(hasMore: Boolean, success: Boolean)`、`BaseLoadMoreView.onLoadMoreFailed()` 其中一种。

```kotlin
interface BaseLoadMoreView<T> : IRequestCommand {
    fun onLoadMoreSuccess(list: List<T>, hasMore: Boolean)
    fun onLoadMoreFailed()
    fun onLoadMoreFailed(hasMore: Boolean, success: Boolean)

    override fun executePageRequest(page: IPage)
}
```