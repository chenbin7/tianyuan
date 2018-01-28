package cn.tianyuan.map;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.tianyuan.BaseActivity;
import cn.tianyuan.R;
import cn.tianyuan.user.addr.AddrDataBeen;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by Administrator on 2017/10/17.
 */

public class MapActivity extends BaseActivity {
    private static final String TAG = MapActivity.class.getSimpleName();

    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.addr_block)
    RelativeLayout mAddrBlock;
    @BindView(R.id.addr_title)
    TextView mAddrTitle;
    @BindView(R.id.addr_detail)
    TextView mAddrDetail;
    @BindView(R.id.search)
    EditText mSearchText;
    @BindView(R.id.addr_list)
    RecyclerView mAddrList;
    @BindView(R.id.addr_list_block)
    RelativeLayout mAddrListBlock;

    AMap aMap;
    String keyWord;
    int currentPage;
    PoiSearch.Query query;
    PoiSearch poiSearch;
    ArrayList<PoiItem> searchResultList;
    Marker selectMarker;
    int selectPosition;
    AddrAdapter mAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        mAddrBlock.setVisibility(View.GONE);
        mAddrListBlock.setVisibility(View.GONE);
        mapView.onCreate(savedInstanceState);
        aMap = mapView.getMap();
        initMap(aMap);
        mAddrList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new AddrAdapter();
        mAddrList.setAdapter(mAdapter);
        mAddrList.addItemDecoration(new SpaceItemDecoration(10));
        mAdapter.setOnItemClickListener(new AddrAdapter.OnItemClickListener() {
            @Override
            public void onRecyclerItemClick(PoiItem item, int position) {
                showMarks();
                selectPosition = position;
                moveToSelectMarkInfo(item, position);
                mAddrListBlock.setVisibility(View.GONE);
            }
        });
        mSearchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if(text.isEmpty()){
                    mAddrListBlock.setVisibility(View.GONE);
                } else {
                    if(mAddrListBlock.getVisibility() == View.VISIBLE){
                        return;
                    }
                    mAdapter.clear();
                    mAddrListBlock.setVisibility(View.VISIBLE);
                    mAddrListBlock.setBackgroundColor(Color.BLACK);
                    mAddrListBlock.setAlpha(0f);
                    ObjectAnimator.ofFloat(mAddrListBlock, "alpha", 0.0f, 0.7f).setDuration(200).start();
                }
            }
        });
    }

    private void initMap(AMap aMap) {
        //实时交通
        aMap.setTrafficEnabled(false);
        //卫星图层
        aMap.setMapType(AMap.MAP_TYPE_NORMAL);
        //显示当前位置
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.showMyLocation(true);
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        //地图比例
        UiSettings mapUI = aMap.getUiSettings();
        mapUI.setZoomControlsEnabled(true);
        mapUI.setScaleControlsEnabled(true);
        CameraUpdate mCameraUpdate = CameraUpdateFactory.zoomTo(17);
        aMap.animateCamera(mCameraUpdate, 1000, null);
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                int index = markers.indexOf(marker);
                if(index < 0)
                    return false;
                Log.d(TAG, "onMarkerClick: "+index);
                selectPosition = index;
                setAddr(marker);
                return false;
            }
        });
    }

    private void setAddr(Marker marker) {
        selectMarker = marker;
        Observable.just(0)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(i -> {
                    String title = marker.getTitle();
                    String detail = marker.getSnippet();
                    if(title == null || detail == null){
                        return;
                    }
                    if (detail.trim().equals("")) {
                        detail = title;
                    }
                    mAddrBlock.setVisibility(View.VISIBLE);
                    mAddrTitle.setText(title);
                    mAddrDetail.setText(detail);
                });
    }

    public void doSearchQuery(View v) {
        hiddenKeyboard();
        clearMarkers();
        keyWord = mSearchText.getText().toString().trim();
        Log.d(TAG, "doSearchQuery: " + keyWord);
        if(keyWord.isEmpty())
            return;
        currentPage = 0;
        query = new PoiSearch.Query(keyWord, "120000", AMapLocation.getInstance().getCity());
        query.setPageSize(20);// 设置每页最多返回多少条poiitem
        query.setPageNum(currentPage);// 设置查第一页
        poiSearch = new PoiSearch(this, query);
        poiSearch.setOnPoiSearchListener(new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int errorCode) {
                Log.d(TAG, "onPoiSearched: " + poiResult + " " + errorCode);
                searchResultList = poiResult.getPois();
                mAddrListBlock.setBackgroundResource(R.drawable.bg_login_reg);
                mAddrListBlock.setAlpha(1.0f);
                mAdapter.setData(searchResultList);
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {
                Log.d(TAG, "onPoiItemSearched: " + poiItem + "  " + i);
            }
        });
        poiSearch.searchPOIAsyn();// 异步搜索
    }

    private void showMarks(){
        PoiItem item;
        for (int i = 0; i < searchResultList.size(); i++) {
            item = searchResultList.get(i);
            LatLng latLng = new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude());
            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(item.getTitle()).snippet(item.getSnippet());
            Marker marker = aMap.addMarker(markerOptions);
            markers.add(marker);
        }
    }

    private void moveToSelectMarkInfo(PoiItem item, int index){
        LatLng latLng = new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude());
        CameraPosition position = new CameraPosition(latLng, 18, 30, 0);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(position);
        aMap.animateCamera(cameraUpdate, 1000, null);
        Marker marker = markers.get(index);
        marker.showInfoWindow();
        setAddr(marker);
    }


    List<Marker> markers = new ArrayList<>();
    private void clearMarkers() {
        for (int i = 0; i < markers.size(); i++) {
            markers.get(i).destroy();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
        if (selectMarker != null) {
            setAddr(selectMarker);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    public void onCommit(View v) {
        Intent intent = new Intent();
        PoiItem poi = searchResultList.get(selectPosition);
        AddrDataBeen addr = convertAddr(poi);
        intent.putExtra("addr", addr);
        doFinish(RESULT_OK, intent);
    }

    private AddrDataBeen convertAddr(PoiItem item){
        AddrDataBeen addr = new AddrDataBeen();
        addr.pName         = item.getProvinceName();
        addr.cityName      = item.getCityName();
        addr.adName        = item.getAdName();
        addr.address       = item.getSnippet()+item.getTitle();
        addr.communityName = item.getTitle();
        return addr;
    }

}
