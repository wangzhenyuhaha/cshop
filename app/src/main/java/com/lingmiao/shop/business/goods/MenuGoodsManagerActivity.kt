package com.lingmiao.shop.business.goods

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.viewModels
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.james.common.base.BaseVBActivity
import com.james.common.utils.DialogUtils
import com.james.common.utils.exts.gone
import com.james.common.utils.exts.singleClick
import com.james.common.utils.exts.visiable
import com.lingmiao.shop.R
import com.lingmiao.shop.business.goods.adapter.SimpleMenuAdapter
import com.lingmiao.shop.business.goods.adapter.SimpleMenuTwoAdapter
import com.lingmiao.shop.business.goods.api.bean.ShopGroupVO
import com.lingmiao.shop.business.goods.fragment.GoodsMenuFragment
import com.lingmiao.shop.business.goods.presenter.MenuGoodsManagerPre
import com.lingmiao.shop.business.goods.presenter.impl.MenuGoodsManagerPreImpl
import com.lingmiao.shop.databinding.ActivityMenuGoodsManagerBinding

@SuppressLint("NotifyDataSetChanged")
class MenuGoodsManagerActivity :
    BaseVBActivity<ActivityMenuGoodsManagerBinding, MenuGoodsManagerPre>(),
    MenuGoodsManagerPre.View {

    private val viewModel by viewModels<MenuGoodsManagerViewModel>()

    //查询全部二级菜单
    private lateinit var allSecondMenu: ShopGroupVO

    //是否已经加载Fragment
    private var isFragmentExited: Boolean = false

    //是否处于编辑状态 true 是  false 否
    private val isEdited: MutableLiveData<Boolean> = MutableLiveData()

    //置顶菜单
    private var firstAdapter: SimpleMenuAdapter? = null
    var firstStart: Int = -1

    //常用菜单
    private var secondAdapter: SimpleMenuAdapter? = null
    var secondStart: Int = -1

    //二级菜单
    private var thirdAdapter: SimpleMenuTwoAdapter? = null

    override fun createPresenter() = MenuGoodsManagerPreImpl(this, this)

    override fun useLightMode() = false

    override fun getViewBinding() = ActivityMenuGoodsManagerBinding.inflate(layoutInflater)

    override fun initView() {

        mToolBarDelegate?.setMidTitle("菜单管理")

        allSecondMenu = ShopGroupVO().also {
            it.isButton = true
            it.shopCatName = "全部"
            it.catPath = "NULL"
        }

        //默认完成状态中
        isEdited.value = false

        isEdited.observe(this, {
            if (it) {
                mToolBarDelegate?.setRightText("完成") { isEdited.value = isEdited.value == false }
                //编辑状态中
                //置顶
                mBinding.menuAddOne.visiable()
                //常用
                mBinding.menuAddTwo.visiable()
                //二级
                if (viewModel.item.value?.isTop == 0) {
                    mBinding.menuAddThree.visiable()
                }


                //置顶菜单
                firstAdapter?.data?.let { list ->
                    for (i in list) {
                        i.isdeleted = true
                    }
                }
                firstAdapter?.notifyDataSetChanged()

                //常用菜单
                secondAdapter?.data?.let { list ->
                    for (i in list) {
                        i.isdeleted = true
                    }
                }
                secondAdapter?.notifyDataSetChanged()

                //二级菜单
                thirdAdapter?.data?.let { list ->
                    for (i in list) {
                        i.isdeleted = true
                    }
                }
                thirdAdapter?.notifyDataSetChanged()

            } else {
                mToolBarDelegate?.setRightText("编辑") { isEdited.value = isEdited.value == false }
                //完成状态中
                //置顶
                mBinding.menuAddOne.gone()
                //常用
                mBinding.menuAddTwo.gone()
                //二级
                mBinding.menuAddThree.gone()


                //置顶菜单
                firstAdapter?.data?.let { list ->
                    for (i in list) {
                        i.isdeleted = false
                    }
                }
                firstAdapter?.notifyDataSetChanged()


                //常用菜单
                secondAdapter?.data?.let { list ->
                    for (i in list) {
                        i.isdeleted = false
                    }
                }
                secondAdapter?.notifyDataSetChanged()

                //二级菜单
                thirdAdapter?.data?.let { list ->
                    for (i in list) {
                        i.isdeleted = false
                    }
                }
                thirdAdapter?.notifyDataSetChanged()

            }
        })

        //设置置顶菜单
        initFirstMenu()

        //设置常用菜单
        initSecondMenu()

        //设置二级菜单
        initThirdMenu()

        //加载已有菜单数据
        showDialogLoading()
        mPresenter?.loadLv1GoodsGroup(1, true, 1)
    }

    //置顶菜单
    private fun initFirstMenu() {

        firstAdapter = SimpleMenuAdapter().apply {
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.deleteIv -> {
                        DialogUtils.showDialog(context as Activity,
                            "删除提示", "删除后不可恢复，确定要删除该菜单吗？",
                            "取消", "确定删除",
                            null, {
                                mPresenter?.deleteGoodsGroup(
                                    firstAdapter?.getItem(position),
                                    position,
                                    1
                                )
                            })
                    }
                }
            }
        }

        //添加置顶菜单
        mBinding.menuAddOne.singleClick {
            MenuEditActivity.openActivity(this, ShopGroupVO.LEVEL_1, null, null)
        }

        //知道菜单排序
        val listenerOne: OnItemDragListener = object : OnItemDragListener {

            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                //获取初始位置
                firstStart = pos
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
        firstAdapter?.setOnItemDragListener(listenerOne)
        firstAdapter?.enableDragItem(mItemTouchHelper)

        mBinding.rvOne.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.rvOne.adapter = firstAdapter

        //查看商品
        firstAdapter?.setOnItemClickListener { adapter, _, position ->
            if (isEdited.value == true) {
                //编辑中
                MenuEditActivity.openActivity(
                    this,
                    ShopGroupVO.LEVEL_1,
                    firstAdapter?.getItem(position)?.shopCatPid,
                    firstAdapter?.getItem(position)
                )
            } else {
                if ((adapter.data[position] as ShopGroupVO).disable != 1) {
                    //不显示
                    //提示该菜单未显示
                    DialogUtils.showDialog(this,
                        "查看商品",
                        "当前菜单未在小程序显示，是否查看该菜单下的商品？",
                        "取消",
                        "查看",
                        null,
                        {
                            firstAdapter?.setGroupId((adapter.data[position] as ShopGroupVO).catPath)
                            firstAdapter?.notifyDataSetChanged()
                            secondAdapter?.setGroupId("")
                            secondAdapter?.notifyDataSetChanged()
                            viewModel.setShopGroup(adapter.data[position] as ShopGroupVO)
                            //默认选中全部
                            thirdAdapter?.setGroupId("NULL")
                        }
                    )
                } else {
                    //显示
                    //提示该菜单未显示
                    firstAdapter?.setGroupId((adapter.data[position] as ShopGroupVO).catPath)
                    firstAdapter?.notifyDataSetChanged()
                    secondAdapter?.setGroupId("")
                    secondAdapter?.notifyDataSetChanged()
                    //更新选中的一级菜单
                    viewModel.setShopGroup(adapter.data[position] as ShopGroupVO)
                    //默认选中全部
                    thirdAdapter?.setGroupId("NULL")
                }


            }
        }

    }

    //常用菜单
    private fun initSecondMenu() {

        secondAdapter = SimpleMenuAdapter().apply {
            setOnItemChildClickListener { _, view, position ->
                when (view.id) {
                    R.id.deleteIv -> {
                        DialogUtils.showDialog(context as Activity,
                            "删除提示", "删除后不可恢复，确定要删除该菜单吗？",
                            "取消", "确定删除",
                            null, {
                                mPresenter?.deleteGoodsGroup(
                                    secondAdapter?.getItem(position),
                                    position,
                                    0
                                )
                            })
                    }
                }
            }
        }

        //添加常用菜单
        mBinding.menuAddTwo.singleClick {
            UserMenuEditActivity.openActivity(this, "0", null, 0)
        }

        //常用菜单排序
        val listenerTwo: OnItemDragListener = object : OnItemDragListener {

            override fun onItemDragStart(viewHolder: RecyclerView.ViewHolder?, pos: Int) {
                //获取初始位置
                secondStart = pos
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

                    val pre = secondAdapter?.data?.get(pos - 1)
                    val item = secondAdapter?.data?.get(pos)
                    if (item != null && pre != null) {
                        mPresenter?.sort(0, item.shopCatId!!, pre.sort + 1)
                    }
                } else {
                    //到顶了
                    //获取第一个Item
                    val item = secondAdapter?.data?.get(pos)
                    if (item != null) {
                        mPresenter?.sort(0, item.shopCatId!!, 0)
                    }
                }
            }
        }

        val mItemDragAndSwipeCallback = ItemDragAndSwipeCallback(secondAdapter)
        val mItemTouchHelper = ItemTouchHelper(mItemDragAndSwipeCallback)
        mItemTouchHelper.attachToRecyclerView(mBinding.rvTwo)
        secondAdapter?.setOnItemDragListener(listenerTwo)
        secondAdapter?.enableDragItem(mItemTouchHelper)

        mBinding.rvTwo.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mBinding.rvTwo.adapter = secondAdapter

        //查看商品
        secondAdapter?.setOnItemClickListener { adapter, _, position ->
            if (isEdited.value == true) {
                //编辑中
                UserMenuEditActivity.openActivity(
                    this,
                    secondAdapter?.getItem(position)?.shopCatPid,
                    secondAdapter?.getItem(position),
                    0
                )
            } else {

                if ((adapter.data[position] as ShopGroupVO).disable != 1) {
                    //不显示
                    //提示该菜单未显示
                    DialogUtils.showDialog(this,
                        "查看商品",
                        "当前菜单未在小程序显示，是否查看该菜单下的商品？",
                        "取消",
                        "查看",
                        null,
                        {
                            firstAdapter?.setGroupId("")
                            firstAdapter?.notifyDataSetChanged()
                            secondAdapter?.setGroupId((adapter.data[position] as ShopGroupVO).catPath)
                            secondAdapter?.notifyDataSetChanged()
                            viewModel.setShopGroup(adapter.data[position] as ShopGroupVO)
                            //默认选中全部
                            thirdAdapter?.setGroupId("NULL")
                        }
                    )
                } else {
                    //显示
                    //提示该菜单未显示
                    firstAdapter?.setGroupId("")
                    firstAdapter?.notifyDataSetChanged()
                    secondAdapter?.setGroupId((adapter.data[position] as ShopGroupVO).catPath)
                    secondAdapter?.notifyDataSetChanged()
                    //更新选中的一级菜单
                    viewModel.setShopGroup(adapter.data[position] as ShopGroupVO)
                    //默认选中全部
                    thirdAdapter?.setGroupId("NULL")

                }

            }
        }

    }

    //二级菜单
    private fun initThirdMenu() {

        //添加二级菜单
        mBinding.menuAddThree.singleClick {
            DialogUtils.showInputDialog(this, "菜单名称", "", "请输入", "取消", "保存", null) {
                viewModel.savedItem.value?.shopCatId?.let { it1 -> mPresenter?.addGroup(it, it1) }
            }
        }

        //二级菜单
        thirdAdapter = SimpleMenuTwoAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val temp = adapter.data[position] as ShopGroupVO
                when (view.id) {
                    R.id.deleteIv -> {
                        if (!temp.isButton) {
                            DialogUtils.showDialog(context as Activity,
                                "删除提示", "删除后不可恢复，确定要删除该菜单吗？",
                                "取消", "确定删除",
                                null, {
                                    mPresenter?.deleteGoodsGroup(
                                        thirdAdapter?.getItem(position),
                                        position,
                                        2
                                    )
                                })
                        }
                    }
                }
            }
        }

        thirdAdapter?.setOnItemClickListener { adapter, _, position ->

            val item = adapter.data[position] as ShopGroupVO

            if (viewModel.item.value?.isTop == 0) {
                if (isEdited.value == true) {
                    //编辑中
                    if (!item.isButton) {
                        DialogUtils.showInputDialog(
                            context as Activity,
                            "菜单名称",
                            "",
                            "请输入",
                            (adapter.data[position] as ShopGroupVO).shopCatName,
                            "取消",
                            "保存",
                            null
                        ) {
                            //更新云端二级菜单名称
                            //获取当前对应Item

                            item.shopCatName = it
                            mPresenter?.updateSecondGroup(item, it, position)
                        }
                    }

                } else {
                    thirdAdapter?.setGroupId((adapter.data[position] as ShopGroupVO).catPath)
                    thirdAdapter?.notifyDataSetChanged()
                    item.isSecondMenu = true
                    //设置当前选中的菜单未这个
                    viewModel.setShopGroupOnlyFirst(item)
                }


            }
        }

        mBinding.rvThree.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        mBinding.rvThree.adapter = thirdAdapter

        //监听选中菜单的变化
        viewModel.item.observe(this, {

            if (it.children == null || (it.children?.isEmpty() == true)) {
                //没有二级菜单,只显示全部
                if (!it.isSecondMenu) {
                    val list: List<ShopGroupVO> = listOf(allSecondMenu)
                    thirdAdapter?.replaceData(list)
                    thirdAdapter?.notifyDataSetChanged()
                }

            } else {
                //有二级菜单，显示二级菜单
                val list: List<ShopGroupVO> = it.children!!
                //清除保存的数据(这个数据是选中的二级菜单)（该数据保存在thirdAdapter中）
                thirdAdapter?.setGroupId("")

                //判断是否处于编辑状态
                if (isEdited.value == true) {
                    //编辑状态
                    //二级菜单
                    list.let { it1 ->
                        for (i in it1) {
                            i.isdeleted = true
                        }
                    }
                } else {
                    list.let { it1 ->
                        for (i in it1) {
                            i.isdeleted = false
                        }
                    }
                }
                thirdAdapter?.replaceData(listOf<ShopGroupVO>())
                thirdAdapter?.addData(allSecondMenu)
                thirdAdapter?.addData(list)
                thirdAdapter?.notifyDataSetChanged()
            }
        })
    }

    //加载一级菜单成功
    override fun onLoadLv1GoodsGroupSuccess(
        list: List<ShopGroupVO>,
        isTop: Int,
        isSecond: Boolean,
        type: Int
    ) {

        when (type) {
            1 -> {
                if (isTop == 1) {
                    if (isEdited.value == true) {
                        //编辑状态
                        //置顶菜单
                        list.let {
                            for (i in it) {
                                i.isdeleted = true
                            }
                        }
                    } else {
                        list.let {
                            for (i in it) {
                                i.isdeleted = false
                            }
                        }
                    }

                    firstAdapter?.replaceData(list)
                    if (isSecond) {
                        mPresenter?.loadLv1GoodsGroup(0, false, 1)
                    }

                } else {

                    if (isEdited.value == true) {
                        //编辑状态
                        //常用菜单
                        list.let {
                            for (i in it) {
                                i.isdeleted = true
                            }
                        }
                    } else {
                        list.let {
                            for (i in it) {
                                i.isdeleted = false
                            }
                        }
                    }

                    secondAdapter?.replaceData(list)
                }

                //加载Fragment
                if (!isFragmentExited) {
                    //添加Fragment
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        add<GoodsMenuFragment>(R.id.fragment)
                    }
                    isFragmentExited = true
                    firstAdapter?.data?.get(0)?.let { viewModel.setShopGroup(it) }
                    //默认选中全部
                    thirdAdapter?.setGroupId("NULL")
                    firstAdapter?.setGroupId((firstAdapter?.data?.get(0) as ShopGroupVO).catPath)
                    firstAdapter?.notifyDataSetChanged()
                }
            }
            2 -> {
                //加载更新二级菜单
                if (isTop == 1) {
                    if (isEdited.value == true) {
                        //编辑状态
                        //置顶菜单
                        list.let {
                            for (i in it) {
                                i.isdeleted = true
                            }
                        }
                    } else {
                        list.let {
                            for (i in it) {
                                i.isdeleted = false
                            }
                        }
                    }
                    firstAdapter?.replaceData(list)
                    if (isSecond) {
                        mPresenter?.loadLv1GoodsGroup(0, false, 2)
                    }
                } else {

                    if (isEdited.value == true) {
                        //编辑状态
                        //常用菜单
                        list.let {
                            for (i in it) {
                                i.isdeleted = true
                            }
                        }
                    } else {
                        list.let {
                            for (i in it) {
                                i.isdeleted = false
                            }
                        }
                    }

                    secondAdapter?.replaceData(list)

                    //开始更新二级菜单的UI
                    //已选中的一级菜单ID
                    val tempId = viewModel.savedItem.value?.shopCatId
                    firstAdapter?.data?.let {
                        for (i in it) {
                            if (i.shopCatId == tempId) {
                                viewModel.setShopGroup(i)
                            }
                        }
                    }

                    secondAdapter?.data?.let {
                        for (i in it) {
                            if (i.shopCatId == tempId) {
                                viewModel.setShopGroup(i)
                            }
                        }
                    }

                }

            }
        }
        hideDialogLoading()
    }

    //排序成功
    override fun onSortSuccess(isTop: Int) {
        mPresenter?.loadLv1GoodsGroup(isTop, false, 1)
    }

    //删除一级菜单成功
    override fun onDeleteGroupSuccess(position: Int, isTop: Int) {
        //isTop 1 置顶   0  常用   2  二级
        when (isTop) {
            0 -> {
                if (position < secondAdapter?.data?.size!!) {
                    secondAdapter?.data?.removeAt(position)
                    secondAdapter?.notifyDataSetChanged()
                }
            }
            1 -> {
                if (position < firstAdapter?.data?.size!!) {
                    firstAdapter?.data?.removeAt(position)
                    firstAdapter?.notifyDataSetChanged()
                }
            }
            else -> {
                if (position < thirdAdapter?.data?.size!!) {
                    thirdAdapter?.data?.removeAt(position)
                    thirdAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun updateSecondGroupSuccess(newName: String, position: Int) {
        thirdAdapter?.data?.get(position)?.shopCatName = newName
        thirdAdapter?.notifyDataSetChanged()

        //更新保存的一级菜单中的二级菜单数据
        mPresenter?.loadLv1GoodsGroup(1, true, 1)
    }

    override fun addGroupSuccess() {
        //更新保存的一级菜单数据,更新成功后继续更新UI
        mPresenter?.loadLv1GoodsGroup(1, isSecond = true, type = 2)
    }

    override fun onResume() {
        super.onResume()
        mPresenter?.loadLv1GoodsGroup(1, true, 1)
    }
}