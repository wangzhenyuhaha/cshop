package com.lingmiao.shop.business.tools

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lingmiao.shop.R
import com.lingmiao.shop.base.IConstant
import com.lingmiao.shop.business.tools.adapter.AreasAdapter
import com.lingmiao.shop.business.tools.bean.*
import com.lingmiao.shop.business.tools.event.UpdateItemEvent
import com.lingmiao.shop.business.tools.presenter.AreasPresenter
import com.lingmiao.shop.business.tools.presenter.impl.AreasPresenterImpl
import com.james.common.base.BaseActivity
import kotlinx.android.synthetic.main.tools_activity_areas.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AreasActivity : BaseActivity<AreasPresenter>(), AreasPresenter.View  {

    private lateinit var mList : MutableList<RegionVo>;
    private lateinit var mAreasAdapter : AreasAdapter;
    private var mEditItem : Item ? = null;
    private var mTempArea: TempArea ? = null;
    private var mPosition : Int ? = null;

    companion object {
        fun edit(context: Context, position : Int, item : Item?, tempArea: TempArea?, resultValue : Int) {
            if(context is Activity) {
                val intent = Intent(context, AreasActivity::class.java)
                intent.putExtra("tempItem", item)
                intent.putExtra("tempArea", tempArea);
                intent.putExtra("position", position);
                context.startActivityForResult(intent, resultValue)
            }
        }

        fun add(context: Context,tempArea: TempArea?, resultValue : Int) {
            if(context is Activity) {
                val intent = Intent(context, AreasActivity::class.java)
                intent.putExtra("tempArea", tempArea);
                context.startActivityForResult(intent, resultValue)
            }
        }
    }

    override fun initBundles() {
        mEditItem = intent?.getSerializableExtra("tempItem") as Item?;
        mTempArea = intent?.getSerializableExtra("tempArea") as TempArea?;
        mPosition = intent?.getIntExtra("position", -1);
    }

    override fun createPresenter(): AreasPresenter {
        return AreasPresenterImpl(this);
    }

    override fun useBaseLayout(): Boolean {
        return false
    }

    override fun getLayoutId(): Int {
        return R.layout.tools_activity_areas;
    }

    override fun useEventBus(): Boolean {
        return true;
    }

    override fun initView() {
        initTitle();
        initData();
        initAdapter();
        initRecycleView();
    }

    private fun initTitle() {
        val drawable = getDrawable(R.mipmap.goods_plus_blue)
        drawable?.setBounds(0, 0, 30, 30);

        toolbarView?.apply {
            setTitleContent(getString(R.string.tools_area_title))
            setRightListener(
                null,
                getString(R.string.yes),
                R.color.color_3870EA
            ) {
                var list = mAreasAdapter?.data as List<RegionVo> ;

                list = list.filter { it?.isProvinceLevel() };

                var rpMap : HashMap<String, Any> = hashMapOf();
                var rcMap : HashMap<String, Any>;
                var raMap : HashMap<String, TempRegionVo>;

                var regions: ArrayList<Region>? = arrayListOf();
                var aRegion : ArrayList<Region> ?;
                var regionItem : Region;

                list.forEachIndexed { index, regionVo ->
                    // 省
                    if(regionVo?.isCheck || regionVo?.selectedAll == true) {
                        regionItem = Region(null, regionVo?.localName);
                        regions?.add(regionItem);


                        regionVo.selectedAll = true;
                        regionVo.children = null;
                        rpMap.put(String.format("%s", regionVo?.id), TempRegionVo(regionVo));
                    } else {
                        rcMap = hashMapOf();
                        regionVo?.children?.forEachIndexed { index, cityVo ->
                            // 市
                            if(cityVo?.isCheck || cityVo?.selectedAll == true) {
                                regionItem = Region(null, cityVo?.localName);
                                regions?.add(regionItem);

                                cityVo.selectedAll = true;
                                cityVo.children = null;
                                rcMap.put(String.format("%s", cityVo.id), TempRegionVo(cityVo));
                            } else {
                                aRegion = arrayListOf();

                                raMap = hashMapOf();
                                // 区
                                cityVo?.children?.forEachIndexed { index, areaVo ->
                                    if(areaVo?.isCheck || areaVo?.selectedAll == true) {
                                        aRegion?.add(Region(null, areaVo?.localName));

                                        areaVo.children = null;
                                        raMap.put(String.format("%s", areaVo.id), TempRegionVo(areaVo));
                                    }
                                }
                                if(raMap != null && raMap?.size > 0) {
                                    rcMap.put(String.format("%s", cityVo?.id), TempRegionVo(cityVo?.id, cityVo?.level, cityVo?.localName, cityVo?.parentId, cityVo?.selectedAll, raMap));
                                }
                                if(aRegion != null && aRegion?.size ?:0 > 0) {
                                    regionItem = Region(aRegion, cityVo?.localName);
                                    regions?.add(regionItem);
                                }
                            }
                        }
                        if(rcMap != null && rcMap?.size > 0) {
                            rpMap.put(String.format("%s", regionVo?.id), TempRegionVo(regionVo?.id, regionVo?.level, regionVo?.localName, regionVo?.parentId, regionVo?.selectedAll, rcMap));
                        }
                    }
                }

                var intent = Intent();
                intent.putExtra(IConstant.BUNDLE_KEY_OF_ITEM, rpMap);
                intent.putExtra(IConstant.BUNDLE_KEY_OF_ITEM_ID, mPosition);
                intent.putExtra("regions", regions);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }
    }

    private fun initData() {
        mList = mPresenter.getJsonList(mEditItem, mTempArea);
    }

    private fun initAdapter() {
        mAreasAdapter = AreasAdapter(mList).apply {
            setOnItemChildClickListener { adapter, view, position ->
                val viewType = adapter.getItemViewType(position);
                if(viewType == AreasAdapter.TYPE_LEVEL_0) {
                    val bItem = adapter.data[position] as RegionVo;
                    if(view.id == R.id.iv_province_expand || view.id == R.id.ll_province) {
                        if(bItem.isExpanded) {
                            mAreasAdapter.collapse(position, false);
                        } else {
                            mAreasAdapter.expand(position, false)
                        }
                    } else if(view.id == R.id.iv_province_check) {
                        bItem.isCheck = !bItem.isCheck;
                        bItem.shiftAllCheck(bItem.isCheck);
                        mAreasAdapter.notifyDataSetChanged();
                    }

                } else if(viewType == AreasAdapter.TYPE_LEVEL_1) {
                    val bItem = adapter.data[position] as RegionVo
                    if(view.id == R.id.iv_city_expand || view.id == R.id.ll_city) {
                        if(bItem.isExpanded) {
                            mAreasAdapter.collapse(position, false);
                        } else {
                            mAreasAdapter.expand(position, false)
                        }
                    } else if(view.id == R.id.iv_city_check) {
                        bItem.isCheck = !bItem.isCheck;
                        bItem.shiftAllCheck(bItem.isCheck);
                        var p = mAreasAdapter.getItem(bItem?.pPosition!!) as RegionVo;
                        if(!bItem.isCheck) {
                            p.isCheck = false;
                        } else {
                            if(p.isCheckAll()) {
                                p.isCheck = true;
                            }
                        }
                        mAreasAdapter.notifyDataSetChanged();
                    }

                } else if(viewType == AreasAdapter.TYPE_AREA) {
                    val bItem = adapter.data[position] as RegionVo;
                    if(view.id == R.id.iv_area_check || view.id == R.id.ll_area) {
                        bItem.isCheck = !bItem.isCheck;
                        // 反选
                        var p = mAreasAdapter.getItem(bItem?.pPosition!!) as RegionVo;
                        var c = mAreasAdapter.getItem(bItem?.cPosition!!) as RegionVo;
                        if(!bItem.isCheck) {
                            p.isCheck = false;
                            c.isCheck = false;
                        } else {
                            if(c.isCheckAll()) {
                                c.isCheck = true;
                            }
                            if(p.isCheckAll()) {
                                p.isCheck = true;
                            }
                        }
                        mAreasAdapter.notifyDataSetChanged();
                    }
                }
            }
            setOnItemClickListener { adapter, view, position ->

            }
        }
    }

    private fun initRecycleView() {
        loadMoreRv.apply {
            layoutManager = initLayoutManager();
            adapter = mAreasAdapter;
        }
//        mAreasAdapter.expand(0);
    }

    private fun initLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRefreshEvent(event: UpdateItemEvent) {
        if(event?.position > -1) {

        }
    }
}