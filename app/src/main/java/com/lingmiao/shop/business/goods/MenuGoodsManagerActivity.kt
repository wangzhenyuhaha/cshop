package com.lingmiao.shop.business.goods

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.james.common.base.BaseVBActivity
import com.lingmiao.shop.business.goods.adapter.SimpleMenuAdapter
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.presenter.MenuGoodsManagerPre
import com.lingmiao.shop.business.goods.presenter.impl.MenuGoodsManagerPreImpl
import com.lingmiao.shop.databinding.ActivityMenuGoodsManagerBinding


class MenuGoodsManagerActivity :
    BaseVBActivity<ActivityMenuGoodsManagerBinding, MenuGoodsManagerPre>(),
    MenuGoodsManagerPre.View {

    //是否处于编辑状态 true 是  false 否
    private val isEdited: MutableLiveData<Boolean> = MutableLiveData()

    //置顶菜单
    private var firstTop: List<ShopGroupVO>? = null
    private var firstAdapter: SimpleMenuAdapter? = null
    var firstStart: Int = -1

    //常用菜单
    private var secondTop: List<ShopGroupVO>? = null
    private var secondAdapter: SimpleMenuAdapter? = null
    var secondStart: Int = -1

    //二级菜单
    private var thirdTop: List<ShopGroupVO>? = null
    private var thirdAdapter: SimpleMenuAdapter? = null


    override fun createPresenter() = MenuGoodsManagerPreImpl(this, this)

    override fun useLightMode() = false

    override fun getViewBinding() = ActivityMenuGoodsManagerBinding.inflate(layoutInflater)

    override fun initView() {

        mToolBarDelegate?.setMidTitle("菜单管理")
        mToolBarDelegate?.setRightText("编辑") {
            isEdited.value = isEdited.value == false
        }

        isEdited.observe(this, Observer {

        })

        //置顶菜单
        firstAdapter = SimpleMenuAdapter().apply {
        }

        val listener: OnItemDragListener = object : OnItemDragListener {

            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                //获取初始位置
                firstStart = pos
                for (i in firstAdapter?.data!!) {
                    Log.d("WZYTSTTSD", i.sort.toString())
                }
            }

            override fun onItemDragMoving(
                source: RecyclerView.ViewHolder?,
                from: Int,
                target: RecyclerView.ViewHolder?,
                to: Int
            ) {
                //nothing to do
            }

            override fun onItemDragEnd(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                //获取到达的位置
                //pos为Item到达的位置
                if (pos > 0) {

                    val pre = firstAdapter?.data?.get(pos - 1)
                    val item = firstAdapter?.data?.get(pos)
                    if (item != null && pre != null) {
                        mPresenter?.sort(1, item.shopCatId!!, pre.sort + 1)
                    }
                } else {
                    //到顶了
                    //获取第一个Item
                    val item = firstAdapter?.data?.get(pos)
                    if (item != null) {
                        mPresenter?.sort(1, item.shopCatId!!, 0)
                    }
                }
            }
        }

        val mItemDragAndSwipeCallback = ItemDragAndSwipeCallback(firstAdapter)
        val mItemTouchHelper = ItemTouchHelper(mItemDragAndSwipeCallback)
        mItemTouchHelper.attachToRecyclerView(mBinding.rvOne)
        firstAdapter?.setOnItemDragListener(listener)
        firstAdapter?.enableDragItem(mItemTouchHelper)

        mBinding.rvOne.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.rvOne.adapter = firstAdapter


        //常用菜单
        secondAdapter = SimpleMenuAdapter()
        mBinding.rvTwo.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.rvTwo.adapter = secondAdapter

        //二级菜单
        thirdAdapter = SimpleMenuAdapter()
        mBinding.rvThree.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.rvThree.adapter = thirdAdapter

// mLoadMoreRv.smoothScrollToPosition(toPosition)
        //先加载置顶菜单
        mPresenter?.loadLv1GoodsGroup(1)


    }

    //加载一级菜单成功
    override fun onLoadLv1GoodsGroupSuccess(list: List<ShopGroupVO>, isTop: Int) {

        if (isTop == 1) {
            firstTop = list

            firstAdapter?.replaceData(list)
            mPresenter?.loadLv1GoodsGroup(0)
        } else {
            secondTop = list
            secondAdapter?.replaceData(list)
            hideDialogLoading()
        }
    }

    fun handleSort(position: Int, item: ShopGroupVO) {
        handleSort(position, 0, item, 0)
    }

    private fun handleSort(position: Int, toPosition: Int, item: ShopGroupVO, sortValue: Int) {
//        mSelectPosition = position
//        // 移除
//        mAdapter.remove(position)
//        // 放置顶部
//        mAdapter.addData(toPosition, item)
//        // 更新第二条
//        mAdapter.notifyItemChanged(toPosition + 1)
//        // 滑到顶部
//        mLoadMoreRv.smoothScrollToPosition(toPosition)
//
//        mPresenter?.sort(isTop!!, item.shopCatId!!, sortValue)
    }

    override fun onSortSuccess(isTop: Int) {
        mPresenter?.loadLv1GoodsGroup(isTop)
    }

}