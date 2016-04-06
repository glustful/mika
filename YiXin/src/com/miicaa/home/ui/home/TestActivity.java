package com.miicaa.home.ui.home;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.CancelableCallback;
import com.amap.api.maps2d.AMap.OnCameraChangeListener;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.miicaa.home.R;

@EActivity
public class TestActivity extends Activity implements LocationSource,
AMapLocationListener,SensorEventListener,OnCameraChangeListener{

	
	static String TAG = "TestActivity";
	float zoom = 18;
	/*高德地图*/
//	@ViewById(R.id.mapView)
	MapView mapView;
	TextView textView;
	
	
	private AMap aMap;
	OnLocationChangedListener mListener;
	LocationManagerProxy mAMapLocationManager;
	
	@Extra
	Double longitude;
	@Extra
	Double latitude;
	@Extra
	String locationStr;

	LatLng latLng;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		mapView = (MapView)findViewById(R.id.mapView);
		textView = (TextView)findViewById(R.id.textView);
		textView.setText(locationStr);
		mapView.onCreate(savedInstanceState);
		if(longitude != null && latitude != null){
			latLng = new LatLng(latitude, longitude);
		}
		init();
		changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 18, 0, 0)),
				null);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();
		deactivate();
		mapView.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
		Log.d(TAG, TAG + "is destroy");
	}
	
    private void init(){
    	if(aMap == null){
    		aMap = mapView.getMap();
    		setUpMap();
    	}
    }
    
    /**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		// 自定义系统定位小蓝点
		MyLocationStyle myLocationStyle = new MyLocationStyle();
		myLocationStyle.myLocationIcon(BitmapDescriptorFactory
				.fromResource(R.drawable.location));// 设置小蓝点的图标
		myLocationStyle.strokeColor(Color.TRANSPARENT);// 设置圆形的边框颜色
		myLocationStyle.radiusFillColor(Color.TRANSPARENT);// 设置圆形的填充颜色
		// myLocationStyle.anchor(int,int)//设置小蓝点的锚点
//		myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
		aMap.setMyLocationStyle(myLocationStyle);
		aMap.addMarker(new MarkerOptions().position(latLng).icon(
				BitmapDescriptorFactory
						.fromResource(R.drawable.location)));
//		aMap.setLocationSource(this);// 设置定位监听
//		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
//		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
	   // aMap.setMyLocationType()
		//设置摄像机位置改变监听
//		aMap.setOnCameraChangeListener(this);
	}



	/**
	 * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
	 */
	private void changeCamera(CameraUpdate update, CancelableCallback callback) {
//		boolean animated = ((CompoundButton) findViewById(R.id.animate))
//				.isChecked();
//		if (animated) {
//			aMap.animateCamera(update, 1000, callback);
//		} else {
			aMap.moveCamera(update);
//		}
	}
	
	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation aLocation) {
		if (mListener != null && aLocation != null) {
			mListener.onLocationChanged(aLocation);// 显示系统小蓝点
			textView.setText(aLocation.getAddress());
			LatLng latLng = new LatLng(aLocation.getLatitude(), aLocation.getLongitude());
			//第一个参数是缩放，第二个是倾斜角度，第三个是旋转角度
			CameraUpdate update = CameraUpdateFactory.
					newCameraPosition(new CameraPosition(latLng  , zoom, 0, 0));
			aMap.animateCamera(update,1000,null);
			Log.d(TAG, "the location is changed!");
		}
	}
	

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(this);
			/*
			 * mAMapLocManager.setGpsEnable(false);
			 * 1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true Location
			 * API定位采用GPS和网络混合定位方式
			 * ，第一个参数是定位provider，第二个参数时间最短是2000毫秒，第三个参数距离间隔单位是米，第四个参数是定位监听者
			 */
			mAMapLocationManager.requestLocationUpdates(
					LocationProviderProxy.AMapNetwork, 2000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destory();
		}
		mAMapLocationManager = null;
		Log.d(TAG, "location source is stop!");
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		
	}

	@Override
	public void onCameraChange(CameraPosition arg0) {
		
	}

	@Override
	public void onCameraChangeFinish(CameraPosition arg0) {
//		zoom = arg0.zoom;
//		Log.d(TAG, "this zoom is ..."+zoom);
	}
	
	
}
