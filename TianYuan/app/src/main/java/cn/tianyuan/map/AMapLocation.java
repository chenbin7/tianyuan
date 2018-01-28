package cn.tianyuan.map;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

import cn.tianyuan.common.util.AndroidSharedPreferences;

/**
 * Created by Administrator on 2017/8/23.
 */

public class AMapLocation implements AMapLocationListener {
    private static final String TAG = AMapLocation.class.getSimpleName();
    private static final String SP_GPS = "gps";
    private static final String SP_ADDRESS = "address";
    private static final String SP_CITY = "city";
    private static final String SP_AD_CODE = "ad_code";

    private String city, address, gps, adCode;
    private double longitude = 0, latitude = 0;

    //声明AMapLocationClient类对象
    private AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    private AMapLocationClientOption mLocationOption = null;

    private static AMapLocation sAMapLocation = null;
    public static synchronized AMapLocation getInstance(){
        if(sAMapLocation == null){
            sAMapLocation = new AMapLocation();
        }
        return sAMapLocation;
    }

    private AMapLocation(){

    }

    public AMapLocation initAMap(Context context) {
        //初始化定位
        mLocationClient = new AMapLocationClient(context);
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Battery_Saving，低功耗模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Battery_Saving);
        //获取最近3s内精度最高的一次定位结果
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiActiveScan(false);
        //设置是否允许模拟位置,默认为true，允许模拟位置
        mLocationOption.setMockEnable(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        return sAMapLocation;
    }

    public void startLocation() {
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     D/AMapLocation: onLocationChanged: 5
     D/AMapLocation: onLocationChanged: latitude:30.546111
     D/AMapLocation: onLocationChanged: longitude:104.061137
     D/AMapLocation: onLocationChanged: accuracy = 35.0
     D/AMapLocation: onLocationChanged: addr = 四川省成都市武侯区云华路20号靠近腾讯成都大厦
     D/AMapLocation: onLocationChanged: country = 中国
     D/AMapLocation: onLocationChanged: province = 四川省
     D/AMapLocation: onLocationChanged: city = 成都市
     D/AMapLocation: onLocationChanged: district = 武侯区
     D/AMapLocation: onLocationChanged: street = 云华路
     D/AMapLocation: onLocationChanged: streetNum = 20号
     D/AMapLocation: onLocationChanged: cityCode = 028
     D/AMapLocation: onLocationChanged: adCode = 510107
     D/AMapLocation: onLocationChanged: aoiName = 腾讯成都大厦B楼
     D/AMapLocation: onLocationChanged: build =
     D/AMapLocation: onLocationChanged: floor =
     D/AMapLocation: onLocationChanged: time = 2017-09-06 14:58:53
     * @param amapLocation
     */
    @Override
    public void onLocationChanged(com.amap.api.location.AMapLocation amapLocation) {
        int type = amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
        Log.d(TAG, "onLocationChanged: "+type);
        latitude = amapLocation.getLatitude();//获取纬度
        longitude = amapLocation.getLongitude();//获取经度
        gps = longitude+","+latitude;
        Log.d(TAG, "onLocationChanged: gps:"+gps);
        AndroidSharedPreferences.getInstance().putString(SP_GPS, gps);
        address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
        AndroidSharedPreferences.getInstance().putString(SP_ADDRESS, address);
        Log.d(TAG, "onLocationChanged: address:"+address);
        city = amapLocation.getCity();//城市信息
        AndroidSharedPreferences.getInstance().putString(SP_CITY, city);
        Log.d(TAG, "onLocationChanged: city:"+city);
        adCode = amapLocation.getAdCode();
        AndroidSharedPreferences.getInstance().putString(SP_AD_CODE, adCode);
        Log.d(TAG, "onLocationChanged: adCode:"+adCode);
//        String country = amapLocation.getCountry();//国家信息
//        String province = amapLocation.getProvince();//省信息
//        float accuracy = amapLocation.getAccuracy();//获取精度信息
//        String district = amapLocation.getDistrict();//城区信息
//        String street = amapLocation.getStreet();//街道信息
//        String streetNum = amapLocation.getStreetNum();//街道门牌号信息
//        String aoiName = amapLocation.getAoiName();//获取当前定位点的AOI信息
//        Log.d(TAG, "onLocationChanged: aoiName = "+aoiName);

    }

    public String getAddress(){
        if(address == null)
            return AndroidSharedPreferences.getInstance().getString(SP_ADDRESS, "成都");
        else {
            return address;
        }
    }

    public String getCity(){
        if(city == null)
            return AndroidSharedPreferences.getInstance().getString(SP_CITY, "成都");
        else {
            return city;
        }
    }

    public String getGps(){
        if(latitude == 0 && longitude == 0) {
            return AndroidSharedPreferences.getInstance().getString(SP_GPS, "0.0,0.0");
        } else {
            return gps;
        }
    }

    public String getAdCity(){
        if(adCode == null) {
            return AndroidSharedPreferences.getInstance().getString(SP_AD_CODE, "510107");
        } else {
            return adCode;
        }
    }


}
