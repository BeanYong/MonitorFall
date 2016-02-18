package com.anasit.beanyong.monitorfall.activities;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.anasit.beanyong.monitorfall.R;
import com.anasit.beanyong.monitorfall.entity.Monitor;
import com.anasit.beanyong.monitorfall.entity.MonitorManager;
import com.anasit.beanyong.monitorfall.services.MediaService;
import com.anasit.beanyong.monitorfall.util.AMapUtil;
import com.anasit.beanyong.monitorfall.util.SharedPreferencesUtil;

/**
 * Created by BeanYong on 2015/11/29.
 */
public class MapActivity extends BaseActivity implements LocationSource, AMapLocationListener,
        View.OnClickListener, GeocodeSearch.OnGeocodeSearchListener, AMap.OnMarkerClickListener {
    //地图相关
    private AMap aMap;
    private MapView mapView;
    private OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private String addressName;
    private GeocodeSearch geocoderSearch;
    private LatLonPoint latLonPoint = null;
    private Marker geoMarker;
    private Marker regeoMarker;
    private double mLatDouble, mLonDouble;//要搜索的纬度、经度
    private String mAddressName;//根据经纬度搜索到的地名

    //View相关
    private Button mChangeMode = null;
    private int mCurrentMode = 1;//当前的地图模式，1为定位模式，2为3D模式
    private int mMonitorId = 0;//监控设备的id

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 不显示程序的标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ActivityManager.addActivity(this);
        initView();
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        //获取老人经纬度位置
        Intent intent = getIntent();
        mLonDouble = intent.getDoubleExtra("lon", 0.0);
        mLatDouble = intent.getDoubleExtra("lat", 0.0);
        System.out.println("lon:"+mLonDouble+",lat:"+mLatDouble);
        if (mLonDouble != 0.0) {
            SharedPreferencesUtil.put(this, "oldManLat", mLatDouble);
            SharedPreferencesUtil.put(this, "oldManLon", mLonDouble);
        }

        initMap();
        initEvent();

        stopRing();
        mMonitorId = intent.getIntExtra("id", 0);
        if (mLatDouble != 0.0) {
            searchLL();
        }
    }

    private void stopRing() {
        Intent intent = new Intent(this, MediaService.class);
        intent.setAction(MediaService.ACTION_STOP);
        startService(intent);
    }

    private void initEvent() {
        mChangeMode.setOnClickListener(this);
    }

    private void initView() {
        mapView = (MapView) findViewById(R.id.map);
        mChangeMode = (Button) findViewById(R.id.btn_changeMode);
    }

    /**
     * 初始化
     */
    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
            aMap.setOnMarkerClickListener(this);//设置Marker的点击事件监听
            setUpMap();
        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        if (mLatDouble == 0.0) {
            aMap.setLocationSource(this);// 设置定位监听
            aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            // 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
        }
        //初始化经纬度搜索
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);


//        geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
//                .icon(BitmapDescriptorFactory
//                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
    }

    /**
     * 定位成功后回调函数
     */
    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        if (mLatDouble != 0.0) {//点击notification进入，不定位
            return;
        }
        if (mListener != null && amapLocation != null) {
            if (amapLocation != null && amapLocation.getErrorCode() == 0) {
                mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
                Toast.makeText(this, "我的位置：" + amapLocation.getAddress(), Toast.LENGTH_SHORT).show();
                deactivate();//关闭定位
            } else {
                String errText = "定位失败," + amapLocation.getErrorCode() + ": " + amapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    /**
     * 激活定位
     */
    @Override
    public void activate(OnLocationChangedListener listener) {
        mListener = listener;
        if (mlocationClient == null) {
            mlocationClient = new AMapLocationClient(this);
            mLocationOption = new AMapLocationClientOption();
            //设置定位监听
            mlocationClient.setLocationListener(this);
            //设置为高精度定位模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
            mlocationClient.startLocation();
        }
    }

    /**
     * 停止定位
     */
    @Override
    public void deactivate() {
        mListener = null;
        if (mlocationClient != null) {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_changeMode://切换地图模式
                changeMode();
                break;
        }
    }

    /**
     * 多监控经纬度搜索
     */
//    private void searchLL() {
//        Monitor monitor = MonitorManager.getInstance().getMonitorById(mMonitorId);
//        latLonPoint = new LatLonPoint(monitor.getLatitude(), monitor.getLongitude());
//        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
//                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
//        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
//    }

    /**
     * 经纬度搜索
     */
    private void searchLL() {
        latLonPoint = new LatLonPoint(mLatDouble, mLonDouble);
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 切换地图模式
     */
    private void changeMode() {
        if (mCurrentMode == 1) {//切换到3D模式
            aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_ROTATE);
            mCurrentMode = 2;
            mChangeMode.setText("定位模式");
        } else {//切换到定位模式
            aMap.setMyLocationType(AMap.LOCATION_TYPE_LOCATE);
            mCurrentMode = 1;
            mChangeMode.setText("3D模式");
        }
    }

    /**
     * marker点击时跳动一下
     *
     * @param marker 要跳动的marker
     * @param lati   纬度
     * @param lon    经度
     */
    public void jumpPoint(final Marker marker, final double lati, final double lon) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        LatLng latLng = new LatLng(lati, lon);
        Projection proj = aMap.getProjection();
        Point startPoint = proj.toScreenLocation(latLng);
        startPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * lon + (1 - t)
                        * startLatLng.longitude;
                double lat = t * lati + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
        Toast.makeText(MapActivity.this, mAddressName + "\n北纬：" + mLatDouble + " 度\n东经：" + mLonDouble + " 度", Toast.LENGTH_SHORT).show();
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == 0) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                mAddressName = "老人的位置：" + result.getRegeocodeAddress().getFormatAddress()
                        + "附近";
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(latLonPoint), 15));
                regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));
                Toast.makeText(MapActivity.this, mAddressName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MapActivity.this, "未找到结果", Toast.LENGTH_SHORT).show();
            }
        } else if (rCode == 27) {
            Toast.makeText(MapActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
        } else if (rCode == 32) {
            Toast.makeText(MapActivity.this, "Key不可用", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(MapActivity.this, "网络无法访问", Toast.LENGTH_SHORT).show();
        }

        //初始化老人经纬度数据
        mLatDouble = 0.0;
        mLonDouble = 0.0;
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
//        if (rCode == 0) {
//            if (result != null && result.getGeocodeAddressList() != null
//                    && result.getGeocodeAddressList().size() > 0) {
//                GeocodeAddress address = result.getGeocodeAddressList().get(0);
//                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
//                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 15));
//                geoMarker.setPosition(AMapUtil.convertToLatLng(address
//                        .getLatLonPoint()));
//                addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
//                        + address.getFormatAddress();
//                Toast.makeText(MapActivity.this, addressName, Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(MapActivity.this, "未找到结果", Toast.LENGTH_SHORT).show();
//            }
//        } else if (rCode == 27) {
//            Toast.makeText(MapActivity.this, "网络未连接", Toast.LENGTH_SHORT).show();
//        } else if (rCode == 32) {
//            Toast.makeText(MapActivity.this, "Key不可用", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(MapActivity.this, "网络无法访问", Toast.LENGTH_SHORT).show();
//        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.equals(regeoMarker)) {
            if (aMap != null) {
                jumpPoint(regeoMarker, mLatDouble, mLonDouble);
            }
        }
        return false;
    }

    //==================================================生命周期============================================================

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
        deactivate();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }

        ActivityManager.removeActivity(this);
    }
}
