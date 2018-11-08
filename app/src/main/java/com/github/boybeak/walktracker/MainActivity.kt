package com.github.boybeak.walktracker

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.github.boybeak.permission.Callback
import com.github.boybeak.permission.PH

class MainActivity : AppCompatActivity() {

    //声明AMapLocationClient类对象
//    var mLocationClient: AMapLocationClient? = null
    //声明定位回调监听器
    /*var mLocationListener: AMapLocationListener = object : AMapLocationListener() {

        fun onLocationChanged(aMapLocation: AMapLocation) {
            Toast.makeText(this@MainActivity, "" + aMapLocation.getLatitude(), Toast.LENGTH_SHORT).show()
        }
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //初始化定位
//        mLocationClient = AMapLocationClient(applicationContext)
        //设置定位回调监听
        /*mLocationClient.setLocationListener(mLocationListener)
        val option = AMapLocationClientOption()
        option.setGpsFirst(true)
        mLocationClient.setLocationOption(option)

        PH.ask(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).go(this, object : Callback {
            override fun onGranted(permissions: List<String>) {
                mLocationClient.startLocation()
            }

            override fun onDenied(permission: String) {

            }
        })*/
    }

    override fun onDestroy() {
        super.onDestroy()
//        mLocationClient.stopLocation()
    }

}